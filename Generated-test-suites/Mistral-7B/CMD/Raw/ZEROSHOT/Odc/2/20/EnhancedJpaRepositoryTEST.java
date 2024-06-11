package com.oceanbase.odc.config.jpa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito. Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    private JpaEntityInformation<TestEntity, Long> testEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(testEntityInformation.getJavaType()).thenReturn(TestEntity.class);
        when(testEntityInformation.getIdentifierPropertyName()).thenReturn("id");
        when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.unwrap(EntityManager.class)).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(javax.persistence.EntityManagerFactory.class));
        when(entityManager.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        when(DataSourceUtils.getConnection(dataSource, null)).thenReturn(mock(java.sql.Connection.class));
        when(entityManager.findAll()).thenReturn(new ArrayList<>());
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testFindAll() {
        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(new TestEntity(1L, "test1"));
        testEntities.add(new TestEntity(2L, "test2"));
        when(namedParameterJdbcTemplate.query(TestEntity.QUERY_ALL, new Object[0], TestEntityMapper.mapRowToEntity::mapRowToEntity)).thenReturn(testEntities);
        assertEquals(testEntities, enhancedJpaRepository.findAll());
    }

    @Test
    public void testFindById() {
        TestEntity testEntity = new TestEntity(1L, "test1");
        Optional<TestEntity> optionalTestEntity = Optional.of(testEntity);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);
        when(namedParameterJdbcTemplate.queryForObject(TestEntity.QUERY_FIND_BY_ID, new Object[]{1L}, TestEntityMapper.mapRowToEntity::mapRowToEntity)).thenReturn(testEntity);
        assertEquals(testEntity, enhancedJpaRepository.findById(1L).orElse(null));
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity(null, "test3");
        testEntity.setId(1L);
        when(entityManager.persist(testEntity)).thenReturn(testEntity);
        when(namedParameterJdbcTemplate.update(TestEntity.QUERY_INSERT, TestEntityMapper.mapEntityToInsertParams(testEntity))).thenReturn(1);
        enhancedJpaRepository.save(testEntity);
        assertEquals(testEntity, enhancedJpaRepository.findById(1L).orElse(null));
    }

    @Test
    public void testDeleteById() {
        TestEntity testEntity = new TestEntity(1L, "test1");
        when(entityManager.findById(1L)).thenReturn(Optional.of(testEntity));
        when(entityManager.remove(testEntity)).thenReturn(testEntity);
        when(namedParameterJdbcTemplate.update(TestEntity.QUERY_DELETE_BY_ID, new Object[]{1L})).thenReturn(1);
        enhancedJpaRepository.deleteById(1L);
        assertEquals(0, enhancedJpaRepository.findAll().size());
    }

    @Test
    public void testFindAllByPage() {
        List<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(new TestEntity(1L, "test1"));
        testEntities.add(new TestEntity(2L, "test2"));
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestEntity> page = new PageImpl<>(testEntities, pageable, testEntities.size());
        when(namedParameterJdbcTemplate.query(TestEntity.QUERY_ALL, new Object[0], TestEntityMapper.mapRowToEntityPage::mapRowToEntity)).thenReturn(page);
        assertEquals(page, enhancedJpaRepository.findAll(pageable));
    }
}

class TestEntity {
    private Long id;
    private String name;

    public TestEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String QUERY_ALL = "SELECT * FROM test_entity";
    public static String QUERY_FIND_BY_ID = "SELECT * FROM test_entity WHERE id = :id";
    public static String QUERY_INSERT = "INSERT INTO test_entity (name) VALUES (:name)";
    public static String QUERY_DELETE_BY_ID = "DELETE FROM test_entity WHERE id = :id";

    public static TestEntity mapRowToEntity(Object[] rowData, String[] rowNames) {
        Long id = ((Number) rowData[0]).longValue();
        String name = (String) rowData[1];
        return new TestEntity(id, name);
    }

    public static Map<String, Object> mapEntityToInsertParams(TestEntity testEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", testEntity.getName());
        return params;
    }

    public static TestEntity mapRowToEntityPage(Object[] rowData, String[] rowNames, Page<TestEntity> page) {
        Long id = ((Number) rowData[0]).longValue();
        String name = (String) rowData[1];
        TestEntity testEntity = new TestEntity(id, name);
        testEntity.setPage(page);
        return testEntity;
    }
}