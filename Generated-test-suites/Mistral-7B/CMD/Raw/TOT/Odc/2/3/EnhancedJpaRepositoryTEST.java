package com.oceanbase.odc.config.jpa;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Long> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private EnhancedJpaRepository<Object, Long> enhancedJpaRepository;

    @Test
    public void testGetEntityManager() {
        when(entityInformation.getJavaType()).thenReturn(Object.class);
        when(entityInformation.getEntityClass()).thenReturn(Object.class);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(entityManager.getEntityManagerFactory()).thenReturn(new EntityManagerFactoryImpl());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(entityManager.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl());

        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull(dataSource);

        EntityManager entityManager = enhancedJpaRepository.getEntityManager();
        assertNotNull(entityManager);
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        when(entityInformation.getJavaType()).thenReturn(Object.class);
        when(entityInformation.getEntityClass()).thenReturn(Object.class);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(entityManager.getEntityManagerFactory()).thenReturn(new EntityManagerFactoryImpl());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(entityManager.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl());
        when(entityManager.unwrap(org.hibernate.Session.class)).thenReturn(new org.hibernate.impl.SessionImpl());
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcOperations.class));

        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        when(entityInformation.getJavaType()).thenReturn(Object.class);
        when(entityInformation.getEntityClass()).thenReturn(Object.class);
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(entityManager.getEntityManagerFactory()).thenReturn(new EntityManagerFactoryImpl());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(entityManager.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl());
        when(entityManager.unwrap(org.hibernate.Session.class)).thenReturn(new org.hibernate.impl.SessionImpl());
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcOperations.class));

        EnhancedJpaRepository<Object, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}