
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.sql.Connection;
import javax.sql.Statement;
import javax.sql.ResultSet;

import java.io.Serializable;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @Mock
    private JpaEntityInformation<T, ?> entityInformation;
    
    @Mock
    private EntityManager entityManager;
    
    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private DatasourceConnectionProviderImpl datasourceConnectionProvider;

    @Mock
    private ConnectionProvider connectionProvider;
    
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Mock
    private DataSource dataSource;
    
    private EnhancedJpaRepository<T,ID> rep;

    @Before
    public void setUp() throws Exception {
    	when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
    	when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProvider);
    	when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);
    	
    	rep = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }
    
    @Test
    public void testGetEntityManager() {
    	EntityManager result = rep.getEntityManager();
    	assertEquals(entityManager, result); 
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = rep.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate.getClass(), result.getClass());
    }
    
    @Test
    public void testGetJdbcTemplate() {
    	JdbcTemplate result = rep.getJdbcTemplate();
    	assertEquals(namedParameterJdbcTemplate.getJdbcOperations().getClass(), result.getClass());
    }

}
