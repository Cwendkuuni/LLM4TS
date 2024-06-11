package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);

        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(null, entityManager);
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);

        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(null, entityManager);

        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(null, entityManager);

        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);

        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(null, entityManager);

        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

}