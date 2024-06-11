
package com.oceanbase.odc.config.jpa;

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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {
    @Mock
    private JpaEntityInformation entityInformation;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityManagerFactory emFactory;
    @Mock
    private SessionFactoryImpl sessionFactory;
    @Mock
    private DatasourceConnectionProviderImpl connectionProvider;
    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository repository;

    @Before
    public void setUp() {
        when(entityManager.getEntityManagerFactory()).thenReturn(emFactory);
        when(emFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void getEntityManager() {
        EntityManager result = repository.getEntityManager();
        assertNotNull(result);
    }

    @Test
    public void getJdbcTemplate() {
        JdbcTemplate result = repository.getJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void getNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }
}
