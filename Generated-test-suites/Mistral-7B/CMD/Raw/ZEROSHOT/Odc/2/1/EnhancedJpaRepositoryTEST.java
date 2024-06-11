package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> repository;
 private TestEntityRepository testEntityRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 testEntityRepository = MockitoAnnotations.spy(TestEntityRepository.class);
 repository = new EnhancedJpaRepository<TestEntity, Long>(TestEntity.class, testEntityRepository) {
 @Override
 public DataSource getDataSource(EntityManager entityManager) {
 return Mockito.mock(DataSource.class);
 }
 };
 }

 @Test
 public void testGetEntityManager() {
 EntityManager entityManager = repository.getEntityManager();
 assertNotNull(entityManager);
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
 assertNotNull(jdbcTemplate);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
 assertNotNull(namedParameterJdbcTemplate);
 }

 @Test
 public void testFindAll() {
 List<TestEntity> testEntities = new ArrayList<>();
 testEntities.add(new TestEntity(1L, "Test1"));
 testEntities.add(new TestEntity(2L, "Test2"));
 when(testEntityRepository.findAll()).thenReturn(testEntities);

 List<TestEntity> result = repository.findAll();
 assertEquals(testEntities, result);
 }

 @Test
 public void testFindAllPageable() {
 List<TestEntity> testEntities = new ArrayList<>();
 Page<TestEntity> page = Mockito.mock(Page.class);
 TestEntity testEntity = new TestEntity(1L, "Test1");
 testEntities.add(testEntity);
 when(testEntityRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
 when(page.getContent()).thenReturn(testEntities);

 Page<TestEntity> result = repository.findAll(Mockito.any(Pageable.class));
 assertEquals(page, result);
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity(1L, "Test1");
 when(testEntityRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testEntity));

 TestEntity result = repository.findById(1L).get();
 assertEquals(testEntity, result);
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity(null, "Test3");

 repository.save(testEntity);
 TestEntity savedTestEntity = testEntityRepository.findById(1L).get();
 assertEquals(testEntity, savedTestEntity);
 }

 @Test
 public void testDeleteById() {
 TestEntity testEntity = new TestEntity(1L, "Test1");
 repository.save(testEntity);

 repository.deleteById(1L);
 verify(testEntityRepository).deleteById(1L);
 }

 @Test
 public void testCount() {
 long count = 3L;
 when(testEntityRepository.count()).thenReturn(count);

 long result = repository.count();
 assertEquals(count, result);
 }

 @Test
 public void testFindByName() {
 TestEntity testEntity1 = new TestEntity(1L, "Test1");
 TestEntity testEntity2 = new TestEntity(2L, "Test1");
 when(testEntityRepository.findByName("Test1")).thenReturn(testEntity1);

 TestEntity result = repository.findByName("Test1");
 assertEquals(testEntity1, result);
 }

 @Test
 public void testFindByNameWithNamedParam() {
 TestEntity testEntity1 = new TestEntity(1L, "Test1");
 TestEntity testEntity2 = new TestEntity(2L, "Test1");
 when(testEntityRepository.findByName(Mockito.eq("name"))).thenReturn(testEntity1);

 TestEntity result = repository.findByName("name", Map.of("name", "Test1"));
 assertEquals(testEntity1, result);
 }

 @Test
 public void testFindByNameWithPageableAndNamedParam() {
 TestEntity testEntity1 = new TestEntity(1L, "Test1");
 TestEntity testEntity2 = new TestEntity(2L, "Test1");
 Page<TestEntity> page = Mockito.mock(Page.class);
 List<TestEntity> testEntities = new ArrayList<>();
 testEntities.add(testEntity1);
 testEntities.add(testEntity2);
 when(testEntityRepository.findByName(Mockito.eq("name"), Mockito.any(Pageable.class))).thenReturn(page);
 when(page.getContent()).thenReturn(testEntities);

 Page<TestEntity> result = repository.findByName("name", Mockito.any(Pageable.class), Map.of("name", "Test1"));
 assertEquals(page, result);
 }

 @Test
 public void testFindAllByNamedParam() {
 TestEntity testEntity1 = new TestEntity(1L, "Test1");
 TestEntity testEntity2 = new TestEntity(2L, "Test2");
 List<TestEntity> testEntities = new ArrayList<>();
 testEntities.add(testEntity1);
 testEntities.add(testEntity2);
 when(testEntityRepository.findAll(Mockito.anyMapOf("name", String.class))).thenReturn(testEntities);

 List<TestEntity> result = repository.findAll(Map.of("name", "Test1"));
 assertEquals(testEntities, result);
 }

 @Test
 public void testFindAllByNamedParamWithPageable() {
 TestEntity testEntity1 = new TestEntity(1L, "Test1");
 TestEntity testEntity2 = new TestEntity(2L, "Test2");
 Page<TestEntity> page = Mockito.mock(Page.class);
 List<TestEntity> testEntities = new ArrayList<>();
 testEntities.add(testEntity1);
 testEntities.add(testEntity2);
 when(testEntityRepository.findAll(Mockito.anyMapOf("name", String.class), Mockito.any(Pageable.class)))
 .thenReturn(page);
 when(page.getContent()).thenReturn(testEntities);

 Page<TestEntity> result = repository.findAll(Map.of("name", "Test1"), Mockito.any(Pageable.class));
 assertEquals(page, result);
 }
}