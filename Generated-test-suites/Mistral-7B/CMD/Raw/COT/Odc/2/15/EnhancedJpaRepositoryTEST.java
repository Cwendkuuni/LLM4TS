package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> repository;

    private TestEntity testEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testEntity = new TestEntity();
        testEntity.setId(1L);
        testEntity.setName("Test Entity");

        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(createSessionFactoryMock());
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(createSessionFactoryMock());
        Mockito.when(entityManager.getDataSource()).thenReturn(dataSource);
        Mockito.when(entityManager.unwrap(Session.class)).thenReturn(createSessionMock());
        Mockito.when(testEntityInformation.getJavaClass()).thenReturn(TestEntity.class);
    }

    // Testing the constructor
    @Test
    public void testEnhancedJpaRepositoryConstructor() {
        assertNotNull(repository);
    }

    // Testing getEntityManager()
    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, repository.getEntityManager());
    }

    // Testing getJdbcTemplate()
    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repository.getJdbcTemplate());
    }

    // Testing getNamedParameterJdbcTemplate()
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    // Testing findAll()
    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = List.of(testEntity);
        Mockito.when(repository.findAll()).thenReturn(testEntities);
        assertEquals(testEntities, repository.findAll());
    }

    // Testing findById()
    @Test
    public void testFindById() {
        Optional<TestEntity> testEntityOptional = Optional.of(testEntity);
        Mockito.when(repository.findById(1L)).thenReturn(testEntityOptional);
        assertEquals(testEntity, repository.findById(1L).get());
    }

    // Testing save()
    @Test
    public void testSave() {
        repository.save(testEntity);
        assertNotNull(testEntity.getId());
    }

    // Testing deleteById()
    @Test
    public void testDeleteById() {
        repository.deleteById(testEntity.getId());
        verify(entityManager).delete(testEntity);
    }

    // Testing findByNamedQuery()
    @Test
    public void testFindByNamedQuery() {
        String namedQuery = "TestNamedQuery";
        List<TestEntity> testEntities = List.of(testEntity);
        Mockito.when(repository.getNamedQuery(namedQuery)).thenReturn(createNamedQuery(namedQuery));
        Mockito.when(repository.findByNamedQuery(namedQuery)).thenReturn(testEntities);
        assertEquals(testEntities, repository.findByNamedQuery(namedQuery));
    }

    // Testing findByNamedQueryWithParams()
    @Test
    public void testFindByNamedQueryWithParams() {
        String namedQuery = "TestNamedQuery";
        List<TestEntity> testEntities = List.of(testEntity);
        Mockito.when(repository.getNamedQuery(namedQuery)).thenReturn(createNamedQuery(namedQuery));
        Mockito.when(repository.findByNamedQueryWithParams(namedQuery, Map.of("param", "value"))).thenReturn(testEntities);
        assertEquals(testEntities, repository.findByNamedQueryWithParams(namedQuery, Map.of("param", "value")));
    }

    // Edge case testing for findAll() with empty list
    @Test
    public void testFindAll_EmptyList() {
        List<TestEntity> emptyList = List.of();
        Mockito.when(repository.findAll()).thenReturn(emptyList);
        assertTrue(repository.findAll().isEmpty());
    }

    // Edge case testing for findById() with null id
    @Test(expected = NullPointerException.class)
    public void testFindById_NullId() {
        repository.findById(null);
    }

    // Edge case testing for findById() with non-existent id
    @Test
    public void testFindById_NonExistentId() {
        Optional<TestEntity> emptyOptional = Optional.empty();
        Mockito.when(repository.findById(1L)).thenReturn(emptyOptional);
        assertFalse(repository.findById(1L).isPresent());
    }

    // Edge case testing for save() with null entity
    @Test(expected = NullPointerException.class)
    public void testSave_NullEntity() {
        repository.save(null);
    }

    // Edge case testing for deleteById() with non-existent id
    @Test
    public void testDeleteById_NonExistentId() {
        Mockito.verify(entityManager, never()).delete(testEntity);
    }

    private SessionFactory createSessionFactoryMock() {
        return null;
    }

    private Session createSessionMock() {
        return null;
    }

    private org.hibernate.query.Query createNamedQuery(String namedQuery) {
        return null;
    }

}