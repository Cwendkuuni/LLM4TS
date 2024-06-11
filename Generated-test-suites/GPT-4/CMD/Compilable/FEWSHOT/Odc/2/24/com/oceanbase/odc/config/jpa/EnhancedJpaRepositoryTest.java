
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);

        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);

        NamedParameterJdbcTemplate template = mock(NamedParameterJdbcTemplate.class);
        when(template.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));

        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(factory);

        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(factory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        DatasourceConnectionProviderImpl dsProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dsProvider);

        DataSource ds = mock(DataSource.class);
        when(dsProvider.getDataSource()).thenReturn(ds);

        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);

        assertEquals(template.getJdbcOperations().getClass(), repository.getJdbcTemplate().getClass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);

        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(factory);

        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(factory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        DatasourceConnectionProviderImpl dsProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dsProvider);

        DataSource ds = mock(DataSource.class);
        when(dsProvider.getDataSource()).thenReturn(ds);

        EnhancedJpaRepository repository = new EnhancedJpaRepository(entityInformation, entityManager);

        assertEquals(NamedParameterJdbcTemplate.class, repository.getNamedParameterJdbcTemplate().getClass());
    }

}
