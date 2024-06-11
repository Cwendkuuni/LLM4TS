package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setup() {
        context = new AnnotationConfigApplicationContext(JpaConfig.class);
    }

    @Test
    public void testInitialization() {
        EnhancedJpaRepository repository = context.getBean(EnhancedJpaRepository.class);
        assertNotNull(repository);
        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EnhancedJpaRepository repository = context.getBean(EnhancedJpaRepository.class);
        DataSource dataSource = repository.getDataSource(repository.getEntityManager());
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository repository = context.getBean(EnhancedJpaRepository.class);
        EntityManager entityManager = repository.getEntityManager();
        assertNotNull(entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository repository = context.getBean(EnhancedJpaRepository.class);
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository repository = context.getBean(EnhancedJpaRepository.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }
}