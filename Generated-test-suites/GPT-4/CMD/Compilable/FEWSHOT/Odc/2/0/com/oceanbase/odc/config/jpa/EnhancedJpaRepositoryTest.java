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
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    @Mock
    private EntityManager entityManager;
        
    @Mock
    private JpaEntityInformation entityInformation;
    
    @Mock
    private SessionFactoryImpl sessionFactory;
    
    @Mock
    private EntityManagerFactory entityManagerFactory;
    
    @Mock
    private DatasourceConnectionProviderImpl datasourceConnectionProvider;

    @Mock
    private DataSource dataSource;
    
    private EnhancedJpaRepository repository;

    @Before
    public void setUp() {
       when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
       when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
       when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProvider);
       when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);
       
       repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }
    
    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(new NamedParameterJdbcTemplate(dataSource).getJdbcOperations(), repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(new NamedParameterJdbcTemplate(dataSource), repository.getNamedParameterJdbcTemplate());
    }

}