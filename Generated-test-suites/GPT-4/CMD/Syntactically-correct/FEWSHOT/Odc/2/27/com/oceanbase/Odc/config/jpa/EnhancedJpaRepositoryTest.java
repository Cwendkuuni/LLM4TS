package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> repository;
    private EntityManager entityManager;
    private JpaEntityInformation<Object, Serializable> entityInfo;
    private SessionFactoryImpl sessionFactory;
    private ServiceRegistry serviceRegistry;
    private DatasourceConnectionProviderImpl connectionProvider;
    private DataSource dataSource;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        entityManagerFactory = mock(EntityManagerFactory.class);
        entityInfo = mock(JpaEntityInformation.class);
        sessionFactory = mock(SessionFactoryImpl.class);
        serviceRegistry = mock(ServiceRegistry.class);
        connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(serviceRegistry);
        when(serviceRegistry.getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<Object, Serializable>(entityInfo, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

}