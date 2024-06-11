
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    
    private EntityManager entityManager;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JpaEntityInformation entityInformation;
    private EnhancedJpaRepository repository;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        entityInformation = mock(JpaEntityInformation.class);

        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = mock(DataSource.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl datasourceProvider = mock(DatasourceConnectionProviderImpl.class);
        when(entityManager.getEntityManagerFactory())
            .thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
            .thenReturn(datasourceProvider);
        when(datasourceProvider.getDataSource())
            .thenReturn(dataSource);

        assertEquals(dataSource, repository.getDataSource(entityManager));
    }

    @Test(expected = ClassCastException.class)
    public void testGetDataSourceWithException() {
        when(entityManager.getEntityManagerFactory()).thenThrow(new ClassCastException());
        repository.getDataSource(entityManager);
    }
}
