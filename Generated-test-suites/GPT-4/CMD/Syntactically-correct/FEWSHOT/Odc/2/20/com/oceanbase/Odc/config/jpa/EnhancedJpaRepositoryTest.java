package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    @SuppressWarnings("unchecked")
    private final JpaEntityInformation<Object, Serializable> entityInformation = Mockito.mock(JpaEntityInformation.class);
    private final EntityManager entityManager = Mockito.mock(EntityManager.class);

    private final EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
    private final SessionFactoryImpl sessionFactory = Mockito.mock(SessionFactoryImpl.class);
    private final ConnectionProvider connectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
    private final DataSource dataSource = Mockito.mock(DataSource.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

    @Test
    public void testGetEntityManager() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        Mockito.when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        Mockito.when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetJdbcTemplate() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        Mockito.when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        JdbcTemplate jdbcTemplate = (JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations();
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }
    
}