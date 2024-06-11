package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EnhancedJpaRepositoryTest {
    
    private EnhancedJpaRepository<TestEntity, Long> repository;
    private EntityManager mockEntityManager;

    @Before
    public void setUp() {
        JpaEntityInformation<TestEntity, ?> entityInformation = Mockito.mock(JpaEntityInformation.class);
        mockEntityManager = Mockito.mock(EntityManager.class);
        repository = new EnhancedJpaRepository<>(entityInformation, mockEntityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(mockEntityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl mockSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl mockConnectionProvider = Mockito.mock(DatasourceConnectionProviderImpl.class);
        Mockito.when(mockEntityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mockSessionFactory);
        Mockito.when(mockSessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(mockConnectionProvider);
        
        DataSource dataSource = repository.getDataSource(mockEntityManager);
        
        assertNotNull(dataSource);
    }
}