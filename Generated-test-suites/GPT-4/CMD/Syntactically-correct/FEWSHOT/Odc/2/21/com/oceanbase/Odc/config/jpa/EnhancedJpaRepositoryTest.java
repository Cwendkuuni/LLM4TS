
package com.oceanbase.odc.config.jpa;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    EntityManagerFactory entityManagerFactory;
    SessionFactoryImpl sf;
    DatasourceConnectionProviderImpl connectionProvider;
    EntityManager entityManager;
    JpaEntityInformation entityInformation;

    @Before
    public void setup() {
        entityManagerFactory = mock(EntityManagerFactory.class);
        sf = mock(SessionFactoryImpl.class);
        connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        entityManager = mock(EntityManager.class);
        entityInformation = (JpaEntityInformation) mock(JpaEntityInformation.class);
        
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(mock(DataSource.class));
    }
    
    @Test
    public void testGetDataSource() {
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(repo.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
        assertEquals(entityManager, repo.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(repo.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository repo = new EnhancedJpaRepository(entityInformation, entityManager);
        assertEquals(NamedParameterJdbcTemplate.class, repo.getNamedParameterJdbcTemplate().getClass());
    }
}
