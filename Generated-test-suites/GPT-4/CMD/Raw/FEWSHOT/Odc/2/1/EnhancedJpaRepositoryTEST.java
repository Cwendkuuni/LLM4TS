
package com.oceanbase.odc.config.jpa;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testJpaRepository() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository service = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(service);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository service = new EnhancedJpaRepository(entityInformation, entityManager);
        assertEquals(entityManager, service.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(emf);

        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        ServiceRegistry sr = mock(ServiceRegistry.class);
        when(sf.getServiceRegistry()).thenReturn(sr);

        DatasourceConnectionProviderImpl cp = mock(DatasourceConnectionProviderImpl.class);
        when(sr.getService(ConnectionProvider.class)).thenReturn(cp);

        DataSource dataSource = mock(DataSource.class);
        when(cp.getDataSource()).thenReturn(dataSource);

        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository service = new EnhancedJpaRepository(entityInformation, entityManager);
        try {
            JdbcTemplate jdbcTemplate = service.getJdbcTemplate();
            assertNotNull(jdbcTemplate);
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation entityInformation = mock(JpaEntityInformation.class);
        EnhancedJpaRepository service = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(service.getNamedParameterJdbcTemplate());
    }
}
