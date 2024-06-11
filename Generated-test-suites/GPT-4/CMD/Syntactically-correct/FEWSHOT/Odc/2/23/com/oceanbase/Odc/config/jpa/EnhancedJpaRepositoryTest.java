package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> repository;
    private EntityManager entityManager;
    private SessionFactoryImpl sessionFactory;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private DataSource dataSource;
    private JpaEntityInformation<Object, ?> information;
    
    @Before
    public void setUp() {
        entityManager = mock(EntityManager.class);
        sessionFactory = mock(SessionFactoryImpl.class);
        dataSource = mock(DataSource.class);
        information = mock(JpaEntityInformation.class);
        entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(any())).thenReturn(new DatasourceConnectionProviderImpl(dataSource));
        repository = new EnhancedJpaRepository<>(information, entityManager);
    }

    @Test
    public void shouldReturnEntityManager() {
        EntityManager actualEntityManager = repository.getEntityManager();
        assertSame(entityManager, actualEntityManager);
    }

    @Test
    public void getJdbcTemplateShouldReturnObject() {
        JdbcTemplate actualTemplate = repository.getJdbcTemplate();
        assertNotNull(actualTemplate);
    }

    @Test
    public void getNamedParameterJdbcTemplateShouldReturnObject() {
        NamedParameterJdbcTemplate foundTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(foundTemplate);
    }
}