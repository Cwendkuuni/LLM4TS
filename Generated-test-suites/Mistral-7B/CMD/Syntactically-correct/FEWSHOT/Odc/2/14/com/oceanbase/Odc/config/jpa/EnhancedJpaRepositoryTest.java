package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.LocalContainerEntityManagerFactoryBean;
import org.hibernate.jpa.junit.EntityManagerTest;
import org.hibernate.jpa.testing.TestForPersistence;
import org.hibernate.jpa.testing.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.TestEntity;

@RunWith(EntityManagerTest.SpringJUnitTestClassRunner.class)
@SpringJUnitConfig(TestEnhancedJpaRepository.TestConfig.class)
@TestForPersistence(entityClasses = TestEntity.class)
@ComponentScan("com.oceanbase.odc.config.jpa")
@ActiveProfiles("test")
@EnableTransactionManagement
public class TestEnhancedJpaRepository {

    @Configuration
    @Import(JpaConfig.class)
    static class TestConfig {

        @Bean
        public DataSource dataSource() {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUrl("jdbc:mysql://localhost:3306/testdb");
            ds.setUsername("root");
            ds.setPassword("password");
            ds.setTestOnBorrow(false);
            ds.setTestOnReturn(false);
            ds.setTestWhileIdle(false);
            return ds;
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(dataSource());
            em.setPackagesToScan("com.oceanbase.odc.config.jpa.model");
            em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();
            postProcessor.setBeanFactory(em.getBeanFactory());
            em.setBeanFactory(new AnnotationConfigApplicationContext() {{
                registerBeanPostProcessor(postProcessor);
                setValidatingBeanFactory(false);
            }});
            return em;
        }

        @Bean
        public DataSourceTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }
    }

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private TestEntity testEntity;

    @Before
    public void setUp() {
        testEntity = new TestEntity();
        testEntity.setName("Test Entity");
    }

    @After
    public void tearDown(TestEntity paramTestEntity) {
        if (paramTestEntity != null) {
            repository.deleteById(paramTestEntity.getId());
        }
    }

    @Test
    @Transactional
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    @Transactional
    public void testGetJdbcTemplate() {
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    @Transactional
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}