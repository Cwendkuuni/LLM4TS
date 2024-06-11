package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private SessionFactoryImpl sessionFactory;
    @Mock
    private ConnectionProvider connectionProvider;
    @Mock
    private DataSource ds;
    @InjectMocks
    private EnhancedJpaRepository<MockEntity, Serializable> enhancedJpaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(ds);
    }

    @Test
    public void getEntityManagerTest() {
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void getJdbcTemplateTest() {
        assertEquals(true, enhancedJpaRepository.getJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        assertEquals(true, enhancedJpaRepository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }

    private class MockEntity extends SimpleJpaRepository<MockEntity, Serializable> {

        public MockEntity() {
            super(MockEntity.class, entityManager);
        }

    }

}
