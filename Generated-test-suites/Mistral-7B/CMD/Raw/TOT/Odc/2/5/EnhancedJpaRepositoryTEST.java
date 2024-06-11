package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.WorkEnum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Before
    public void setup() {
        entityManager = mock(EntityManager.class);
        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        jdbcTemplate = mock(JdbcTemplate.class);
        dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(createMockSessionFactory());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(createMockSessionFactory());

        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(createMockSessionFactory());
        when(entityManager.unwrap(DatasourceConnectionProviderImpl.class)).thenReturn(createMockConnectionProvider());
        when(entityManager.unwrap(ConnectionProvider.class)).thenReturn(createMockConnectionProvider());

        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(namedParameterJdbcTemplate.getDataSource()).thenReturn(dataSource);

        new EnhancedJpaRepository(createMockJpaEntityInformation(), entityManager);
    }

    @Test
    public void testSave() {
        // Given
        T testEntity = mock(T.class);
        ID testId = mock(ID.class);

        // When
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(createMockJpaEntityInformation(), entityManager);
        repository.save(testEntity);

        // Then
        verify(entityManager).persist(testEntity);
    }

    @Test
    public void testFindAll() {
        // Given
        List<T> testEntities = new ArrayList<>();
        testEntities.add(mock(T.class));

        when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.anyMap())).thenReturn(testEntities.iterator());

        // When
        List<T> entities = new EnhancedJpaRepository<>(createMockJpaEntityInformation(), entityManager).findAll();

        // Then
        verify(jdbcTemplate).query(ArgumentMatchers.anyString(), ArgumentMatchers.anyMap());
        assertEquals("Size should be equal", testEntities.size(), entities.size());
    }

    @Test
    public void testFindById() {
        // Given
        ID testId = mock(ID.class);
        T testEntity = mock(T.class);

        when(entityManager.find(T.class, testId)).thenReturn(testEntity);

        // When
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(createMockJpaEntityInformation(), entityManager);
        T entity = repository.findById(testId).get();

        // Then
        verify(entityManager).find(T.class, testId);
        assertEquals("The returned entity should be the same", testEntity, entity);
    }

    @Test
    public void testDeleteById() {
        // Given
        ID testId = mock(ID.class);
        T testEntity = mock(T.class);

        when(entityManager.find(T.class, testId)).thenReturn(testEntity);
        when(entityManager.remove(testEntity)).thenReturn(testEntity);

        // When
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(createMockJpaEntityInformation(), entityManager);
        repository.deleteById(testId);

        // Then
        verify(entityManager).find(T.class, testId);
        verify(entityManager).remove(testEntity);
    }

    @Test
    public void testNamedQuery() {
        // Given
        String queryName = "findByName";
        String queryString = "SELECT * FROM test_entity WHERE name = :name";
        List<T> testEntities = new ArrayList<>();
        T testEntity = mock(T.class);
        Map<String, Object> namedMap = Map.of("name", "testName");

        when(jdbcTemplate.query(queryString, namedMap)).thenReturn(testEntities.iterator());

        // When
        List<T> entities = new EnhancedJpaRepository<>(createMockJpaEntityInformation(), entityManager)
                .findAll(queryName, Map.of("name", "testName"));

        // Then
        verify(jdbcTemplate).query(queryString, namedMap);
        assertEquals("Size should be equal", testEntities.size(), entities.size());
    }

    @Test
    public void testNamedProcedureCall() {
        // Given
        String procedureName = "testProcedure";
        String procedureCallString = "CALL test_procedure(:testParam)";
        Map<String, Object> namedMap = Map.of("testParam", "testValue");

        // When
        new EnhancedJpaRepository<>(createMockJpaEntityInformation(), entityManager)
                .getNamedParameterJdbcTemplate()
                .execute(procedureCallString, namedMap);

        // Then
        verify(namedParameterJdbcTemplate).execute(procedureCallString, namedMap);
    }

    @Test
    public void testTransactionManagement() {
        // Given
        ID testId = mock(ID.class);
        T testEntity = mock(T.class);
        when(entityManager.getTransaction()).thenReturn(mock(Transaction.class));

        // When
        EnhancedJpaRepository<T, ID> repository = new EnhancedJpaRepository<>(createMockJpaEntityInformation(), entityManager);
        repository.save(testEntity);
        repository.getEntityManager().getTransaction().commit();

        // Then
        verify(entityManager, times(1)).getTransaction();
        verify(entityManager).getTransaction().begin();
        verify(entityManager).getTransaction().commit();
    }

    private JpaEntityInformation<T, ?> createMockJpaEntityInformation() {
        return mock(JpaEntityInformation.class);
    }

    private SessionFactoryImpl createMockSessionFactory() {
        return mock(SessionFactoryImpl.class);
    }

    private ConnectionProvider createMockConnectionProvider() {
        return mock(ConnectionProvider.class);
    }

    private WorkEnum createMockWork() {
        return mock(WorkEnum.class);
    }
}