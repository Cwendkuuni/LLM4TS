package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    @InjectMocks
    EnhancedJpaRepository<T, ID> enhancedJpaRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    SessionFactoryImpl sf;

    @Mock
    JpaEntityInformation<T, ?> entityInformation;

    @Mock
    DatasourceConnectionProviderImpl datasourceConnectionProvider;

    @Mock
    DataSource dataSource;

    @Before
    public void setUp() {
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProvider);
        when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);
    }

    @Test
    public void getEntityManagerTest() {
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertEquals(result, entityManager);
    }

    @Test
    public void getJdbcTemplateTest() {
        Object result = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(result);
        assertEquals(result.getClass(), JdbcTemplate.class);
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
        assertEquals(result.getClass(), NamedParameterJdbcTemplate.class);
    }
}