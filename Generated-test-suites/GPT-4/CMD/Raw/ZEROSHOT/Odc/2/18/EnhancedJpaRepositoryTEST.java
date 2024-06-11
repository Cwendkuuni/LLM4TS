
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private DatasourceConnectionProviderImpl connectionProvider;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager output = repository.getEntityManager();
        assertNotNull(output);
        assertEquals(output, entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate output = repository.getJdbcTemplate();
        assertNotNull(output);
        assertTrue(output instanceof JdbcTemplate);
    }
  
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate output = repository.getNamedParameterJdbcTemplate();
        assertNotNull(output);
        assertTrue(output instanceof NamedParameterJdbcTemplate);
    }

    @Test
    public void testGetDataSource() {
        DataSource output = repository.getDataSource(entityManager);
        assertNotNull(output);
        assertEquals(output, dataSource);
    }
}
