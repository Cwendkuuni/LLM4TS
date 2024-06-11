package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    private EntityManager manager = mock(EntityManager.class);
    private EntityManagerFactory factory = mock(EntityManagerFactory.class);
    private SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
    private ConnectionProvider provider = mock(ConnectionProvider.class);

    @Test
    public void testJpaRepository() {

        JpaEntityInformation<MockEntity, Serializable> entityInformation = mock(JpaEntityInformation.class);

        when(manager.getEntityManagerFactory()).thenReturn(factory);
        when(factory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(provider);

        EnhancedJpaRepository<MockEntity, Serializable> repository = new EnhancedJpaRepository<MockEntity, Serializable>(
                entityInformation, manager);
    }

    @Test
    public void testGetEntityManager() {

        JpaEntityInformation<MockEntity, Serializable> entityInformation = mock(JpaEntityInformation.class);

        when(manager.getEntityManagerFactory()).thenReturn(factory);
        when(factory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(provider);

        EnhancedJpaRepository<MockEntity, Serializable> repository = new EnhancedJpaRepository<MockEntity, Serializable>(
                entityInformation, manager);
        assertEquals(manager, repository.getEntityManager());

    }

    @Test
    public void testGetJdbcTemplate() {

        JpaEntityInformation<MockEntity, Serializable> entityInformation = mock(JpaEntityInformation.class);

        when(manager.getEntityManagerFactory()).thenReturn(factory);
        when(factory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(provider);

        EnhancedJpaRepository<MockEntity, Serializable> repository = new EnhancedJpaRepository<MockEntity, Serializable>(
                entityInformation, manager);

        assertTrue(repository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        JpaEntityInformation<MockEntity, Serializable> entityInformation = mock(JpaEntityInformation.class);

        when(manager.getEntityManagerFactory()).thenReturn(factory);
        when(factory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(provider);

        EnhancedJpaRepository<MockEntity, Serializable> repository = new EnhancedJpaRepository<MockEntity, Serializable>(
                entityInformation, manager);

        assertTrue(repository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    private class MockEntity implements Serializable{
    }

}