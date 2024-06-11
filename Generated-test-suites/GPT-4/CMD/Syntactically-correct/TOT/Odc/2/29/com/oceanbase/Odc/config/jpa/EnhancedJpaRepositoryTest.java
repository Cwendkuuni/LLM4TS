package com.oceanbase.odc.config.jpa;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository repository;
    private JpaEntityInformation entityInformation;
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {

        entityManagerFactory = mock(EntityManagerFactory.class);
        entityManager = mock(EntityManager.class);
        sessionFactory = mock(SessionFactoryImpl.class);
        dataSource = mock(DataSource.class);
        connectionProvider = mock(ConnectionProvider.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        entityInformation = mock(JpaEntityInformation.class);

        repository = new EnhancedJpaRepository(entityInformation, entityManager);

    }
    
    @Test
    public void testGetEntityManager() {
        EntityManager result = repository.getEntityManager();
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }
    
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
        
        assertNotNull(result);
    }
}