package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
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
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {
    
    @Test
    public void testEnhancedJpaRepository() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager);
        
        assertNotNull(repository);
    }
    
    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        
        ConnectionProvider cp = mock(ConnectionProvider.class);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
        
        DataSource dataSource = mock(DataSource.class);
        when(((DatasourceConnectionProviderImpl) cp).getDataSource()).thenReturn(dataSource);
        
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<T, ID>(null, entityManager);
        assertNotNull(repository.getDataSource(entityManager));
    }
    
    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager);
        
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);
        
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager);

        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JpaEntityInformation<T, ?> entityInformation = mock(JpaEntityInformation.class);

        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager);

        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}