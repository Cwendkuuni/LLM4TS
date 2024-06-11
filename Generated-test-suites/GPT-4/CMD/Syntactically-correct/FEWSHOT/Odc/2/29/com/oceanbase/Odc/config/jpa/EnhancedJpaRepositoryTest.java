
package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
    private EnhancedJpaRepository mockRepo;
    private JpaEntityInformation entityInformation;
    private EntityManager entityManager;
    private DataSource dataSource;
    private SessionFactoryImpl sessionFactory;

    @Before
    public void setUp() {
        entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        dataSource = mock(DataSource.class);
        sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        mockRepo = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager mgr = mockRepo.getEntityManager();
        assertEquals(entityManager, mgr);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mockRepo.getJdbcTemplate();
        assertEquals(NamedParameterJdbcTemplate.class, jdbcTemplate.getClass());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mockRepo.getNamedParameterJdbcTemplate();
        assertEquals(NamedParameterJdbcTemplate.class, namedParameterJdbcTemplate.getClass());
    }
}
