package com.oceanbase.odc.config.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import static org.mockito.Mockito.*;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock 
    private JpaEntityInformation<Object, Serializable> jpaEntityInformation;

    @Mock 
    private EntityManager entityManager;
 
    @Mock 
    private SessionFactoryImpl sessionFactory;

    @Mock 
    private ConnectionProvider connectionProvider;

    @Mock 
    private DataSource dataSource;

    @Test
    public void testEntityManager() {
       when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
       when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
       when(connectionProvider.getDataSource()).thenReturn(dataSource);

       EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository = new 
       EnhancedJpaRepository<>(jpaEntityInformation, entityManager);

       assertTrue("Entity Manager", enhancedJpaRepository.getEntityManager() != null);
    }

    @Test
    public void testJdbcTemplate() {
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository = new 
        EnhancedJpaRepository<>(jpaEntityInformation, entityManager);

        assertTrue("JdbcTemolate", enhancedJpaRepository.getJdbcTemplate() != null);
    }

    @Test
    public void testNamedParameterJdbcTemplate() {
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository = new 
        EnhancedJpaRepository<>(jpaEntityInformation, entityManager);

        assertTrue("Named Parameter JdbcTemplate", enhancedJpaRepository.getNamedParameterJdbcTemplate() != null);
    }
}
