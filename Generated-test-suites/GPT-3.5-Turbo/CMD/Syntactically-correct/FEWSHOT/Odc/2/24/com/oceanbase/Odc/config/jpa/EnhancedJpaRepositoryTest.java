package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    
    @Test
    public void testConstructor() {
        JpaEntityInformation entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        
        assertNotNull(repository);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
        assertNotNull(repository.getEntityManager());
    }
    
    @Test
    public void testGetDataSource() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        SessionFactoryImpl sf = Mockito.mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        Mockito.when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(connectionProvider.getDataSource()).thenReturn(dataSource);
        
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        assertEquals(dataSource, repository.getDataSource(entityManager));
    }
    
    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        
        assertEquals(entityManager, repository.getEntityManager());
    }
    
    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        Mockito.when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        repository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        
        assertEquals(jdbcTemplate, repository.getJdbcTemplate());
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        repository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

}

class TestEntity {
    // Test entity class for testing purposes
}