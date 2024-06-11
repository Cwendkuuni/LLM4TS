package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    JpaEntityInformation entityInformation;

    @Mock
    EntityManager entityManager;

    @Mock
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    EntityManagerFactory entityManagerFactory;
    
    @Mock
    SessionFactoryImpl sessionFactoryImpl;
    
    @Mock
    ConnectionProvider connectionProvider;
    
    @Mock
    DataSource dataSource;

    @Test
    public void testEnhancedJpaRepositoryConstruction() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertEquals(entityInformation, repository.getEntityInformation());
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        EntityManager em = repository.getEntityManager();
        assertEquals(entityManager, em);
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);
        NamedParameterJdbcTemplate npjt = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, npjt);
    }
}