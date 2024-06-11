
package com.oceanbase.odc.config.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    EntityManager entityManager;

    @Mock
    DataSource dataSource;

    @Mock
    EntityManagerFactory entityManagerFactory;

    @InjectMocks
    EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

    @Test
    public void getEntityManagerTest() {
        Assert.assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void getJdbcTemplate() {
        Mockito.when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();
        Assert.assertNotNull(result);
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        Mockito.when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        Assert.assertNotNull(result);
    }

    @Test
    public void getDataSource() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        ServiceRegistry serviceRegistry = mock(ServiceRegistry.class);
        when(sessionFactory.getServiceRegistry()).thenReturn(serviceRegistry);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(serviceRegistry.getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        DataSource result = enhancedJpaRepository.getDataSource(entityManager);
        Assert.assertNotNull(result);
    }
}
