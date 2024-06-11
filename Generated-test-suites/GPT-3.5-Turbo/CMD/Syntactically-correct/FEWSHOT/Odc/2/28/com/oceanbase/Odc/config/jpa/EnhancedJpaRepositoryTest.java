package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
        assertTrue(jdbcTemplate instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
        assertTrue(namedParameterJdbcTemplate instanceof NamedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);

        ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
        DatasourceConnectionProviderImpl datasourceConnectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(mock(DataSource.class));

        JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }
}