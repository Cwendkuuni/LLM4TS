
package com.oceanbase.odc.config.jpa;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation jpaEntityInformation;

    @Mock
    private EntityManager entityManager;
    
    @Mock
    private EntityManagerFactory entityManagerFactory;
    
    @Mock
    private SessionFactoryImpl sessionFactory;
    
    @Mock
    private DatasourceConnectionProviderImpl connectionProvider;
    
    @Mock 
    private DataSource dataSource;
    
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    private EnhancedJpaRepository enhancedJpaRepository;

    @Before
    public void setup() {
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(new JdbcTemplate());
        enhancedJpaRepository = new EnhancedJpaRepository(jpaEntityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();
        assertEquals(new JdbcTemplate(), result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, result);
    }
}
