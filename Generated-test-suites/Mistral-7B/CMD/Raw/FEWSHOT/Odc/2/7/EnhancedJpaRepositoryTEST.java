package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringBootTest;
import org.springframework.test.context.runner.AnnotationDriver;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaRepositoryTest;

@RunWith(AnnotationDriver.class)
@SpringBootTest
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest extends JpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> testRepository;

    private SessionFactory sessionFactory;

    @Before
    public void setUp() {
        sessionFactory = buildSessionFactory();
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = testRepository.getEntityManager();
        assertNotNull(entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = testRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = testRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.HSQL)
                    .addScript("schema.sql")
                    .build();
        }
    }
}

@Configuration
@ActiveProfiles("test")
static class TestJpaConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(Mockito.mock(DataSource.class));
        em.setPackagesToScan("com.oceanbase.odc.config.jpa");
        return em;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .build();
    }
}

class JpaRepositoryTest {
    protected SessionFactory buildSessionFactory() {
        return new AnnotationConfigApplicationContext(TestJpaConfig.class)
                .getBean(LocalContainerEntityFactoryBean.class).getObject();
    }
}

class TestEntity {
    // Add your test entity class here
}