package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<TestEntity, Long> entityInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testDataSource() {
        DataSource mockDataSource = mock(DataSource.class);
        SessionFactoryImpl mockSf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl mockConnectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mockSf);
        when(mockSf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(mockConnectionProvider);
        when(mockConnectionProvider.getDataSource()).thenReturn(mockDataSource);

        assertEquals(mockDataSource, repository.getDataSource(entityManager));
    }
}