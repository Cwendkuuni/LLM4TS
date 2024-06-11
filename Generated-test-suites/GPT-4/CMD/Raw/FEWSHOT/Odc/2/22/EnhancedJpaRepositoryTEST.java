package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest<T, ID extends Serializable> {

    private EntityManager entityManager;
    private JpaEntityInformation<T, ?> entityInformation;
    private EnhancedJpaRepository<T, ID> enhancedJpaRepository;

    @Before
    public void setUp() throws SQLException {
        entityManager = Mockito.mock(EntityManager.class);
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        SessionFactoryImpl sessionFactory = Mockito.mock(SessionFactoryImpl.class);
        ConnectionProvider connectionProvider = Mockito.mock(ConnectionProvider.class);
        DatasourceConnectionProviderImpl datasourceConnectionProviderImpl = Mockito.mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        Mockito.when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        Mockito.when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        Mockito.when(((DatasourceConnectionProviderImpl) connectionProvider).getDataSource()).thenReturn(dataSource);
        enhancedJpaRepository = new EnhancedJpaRepository<T, ID>(entityInformation, entityManager);
    }

    @Test
    public void getEntityManagerTest() {
        EntityManager actualResult = enhancedJpaRepository.getEntityManager();
        assertEquals(entityManager, actualResult);
    }

    @Test
    public void getJdbcTemplateTest() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }
}