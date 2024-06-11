package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepositoryImpl;

public class EnhancedJpaRepositoryTest {

    private JpaEntityInformation<EnhancedJpaRepositoryImpl, Integer> entityInformation;
    private EntityManager entityManager;
    private EnhancedJpaRepository<EnhancedJpaRepositoryImpl, Integer> repository;

    @Before
    public void setup() {
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testEntityManager() {
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() throws HibernateException {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(Mockito.mock(ConnectionProvider.class));
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDataSourceNullEntityManager() {
        assertNull(repository.getDataSource(null));
    }
}