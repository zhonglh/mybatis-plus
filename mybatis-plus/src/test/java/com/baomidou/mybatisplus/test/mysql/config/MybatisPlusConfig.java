package com.baomidou.mybatisplus.test.mysql.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.test.mysql.MysqlMetaObjectHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * <p>
 * Mybatis Plus Config
 * </p>
 *
 * @author Caratacus
 * @since 2017/4/1
 */
@Configuration
@MapperScan("com.baomidou.mybatisplus.test.base.mapper")
public class MybatisPlusConfig {

    @Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, GlobalConfig globalConfig) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        /* 数据源 */
        sqlSessionFactory.setDataSource(dataSource);
        /* entity扫描,mybatis的Alias功能 */
        sqlSessionFactory.setTypeAliasesPackage("com.baomidou.mybatisplus.test.base.entity");
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        /* 驼峰转下划线 */
        configuration.setMapUnderscoreToCamelCase(true);
        /* 分页插件 */
        PaginationInterceptor pagination = new PaginationInterceptor();
        configuration.addInterceptor(pagination);

        /** 乐观锁插件 */
        configuration.addInterceptor(new OptimisticLockerInterceptor());

        sqlSessionFactory.setConfiguration(configuration);
//        pagination.setLocalPage(true);
//        OptimisticLockerInterceptor optLock = new OptimisticLockerInterceptor();
//        sqlSessionFactory.setPlugins(new Interceptor[]{
//            pagination,
//            optLock,
//            new PerformanceInterceptor()
//        });
        globalConfig.setMetaObjectHandler(new MysqlMetaObjectHandler());
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }

//    /**
//     * 无效配置 --
//     * 乐观锁插件
//     * @return OptimisticLockerInterceptor
//     */
//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInterceptor();
//    }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig conf = new GlobalConfig();
        conf.setDbConfig(new GlobalConfig.DbConfig());
        /* 逻辑删除注入器 */
        LogicSqlInjector logicSqlInjector = new LogicSqlInjector();
        conf.setSqlInjector(logicSqlInjector);
        conf.setDbConfig(new GlobalConfig.DbConfig()
            .setIdType(IdType.ID_WORKER)
            .setLogicDeleteValue("1")
            .setLogicNotDeleteValue("0"));
        return conf;
    }
}
