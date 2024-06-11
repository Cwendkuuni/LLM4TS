package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.SimpleJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> repository;
 @Mock
 private JpaEntityInformation<TestEntity, Long> entityInformation;
 @Mock
 private EntityManager entityManager;
 @Mock
 private DataSource dataSource;
 @Mock
 private SessionFactoryImpl sessionFactory;
 @Mock
 private JdbcTemplate jdbcTemplate;
 @Mock
 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 repository = new EnhancedJpaRepository<>(entityInformation, entityManager) {
 @Override
 public DataSource getDataSource(EntityManager entityManager) {
 return dataSource;
 }
 };

 repository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
 repository.entityManager = entityManager;
 repository.jdbcTemplate = jdbcTemplate;
 }

 @Test
 public void testConstructor() {
 // test with mocked dependencies
 assertNotNull(repository);
 }

 @Test
 public void testGetEntityManager() {
 // test getting entity manager from repository
 assertSame(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 // test getting jdbc template from repository
 assertSame(jdbcTemplate, repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 // test getting named parameter jdbc template from repository
 assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
 }

 @Test
 public void testFindAll() {
 // test finding all entities from database using jdbc template
 List<TestEntity> entities = repository.findAll();
 assertNotNull(entities);
 assertEquals(0, entities.size());

 // mock entities and return them from findAll method
 when(jdbcTemplate.query(anyString(), anyMap()))
 .thenReturn(new Object[]{new TestEntity(), new TestEntity(), new TestEntity()});

 entities = repository.findAll();
 assertEquals(3, entities.size());
 }

 @Test
 public void testFindAllById() {
 // test finding entities by id from database using named parameter jdbc template
 List<TestEntity> entities = repository.findAllById(List.of(1L, 2L));
 assertNotNull(entities);
 assertEquals(2, entities.size());
 assertEquals(null, entities.get(0).getId());
 assertEquals(1L, entities.get(1).getId());

 // mock entities and return them from findAll method
 when(namedParameterJdbcTemplate.query(anyString(), anyMap()))
 .thenReturn(new Object[][]{new Object[]{1L, new TestEntity()}, new Object[]{2L, new TestEntity()}});

 entities = repository.findAllById(List.of(1L, 2L));
 assertEquals(2, entities.size());
 assertEquals(1L, entities.get(0).getId());
 assertEquals(2L, entities.get(1).getId());
 }

 @Test
 public void testFindById() {
 // test finding entity by id from database using jdbc template
 TestEntity entity = repository.findById(1L).orElse(null);
 assertNull(entity);

 // mock entity and return it from findOne method
 when(jdbcTemplate.queryForObject(anyString(), anyMap(), any()))
 .thenReturn(new TestEntity());

 entity = repository.findById(1L).orElse(null);
 assertNotNull(entity);
 assertEquals(1L, entity.getId());
 }

 @Test
 public void testSave() {
 // test saving entity using jdbc template
 TestEntity entity = new TestEntity();
 entity.setName("test");
 entity.setId(1L);

 repository.save(entity);
 verify(jdbcTemplate).update(anyString(), entity.getId());

 // test saving entity using named parameter jdbc template
 TestEntity entity2 = new TestEntity();
 entity2.setName("test2");
 entity2.setId(2L);

 repository.save(entity2);
 verify(namedParameterJdbcTemplate).update(anyString(), entity2.getId());
 }

 @Test
 public void testDeleteById() {
 // test deleting entity by id using jdbc template
 repository.deleteById(1L);
 verify(jdbcTemplate).update(anyString(), 1L);
 }

 @Test
 public void testCount() {
 // test counting entities using jdbc template
 long count = repository.count();
 assertEquals(0, count);

 // mock count and return it from count method
 when(jdbcTemplate.queryForObject(anyString(), anyMap(), any()))
 .thenReturn(1L);

 count = repository.count();
 assertEquals(1, count);
 }

 @Test
 public void testFindByName() {
 // test finding entities by name from database using named parameter jdbc template
 List<TestEntity> entities = repository.findByName("test");
 assertNotNull(entities);
 assertEquals(0, entities.size());

 // mock entities and return them from find method
 when(namedParameterJdbcTemplate.query(anyString(), anyMap()))
 .thenReturn(new Object[][]{new Object[]{1L, new TestEntity()}, new Object[]{2L, new TestEntity()}});

 entities = repository.findByName("test");
 assertEquals(2, entities.size());
 assertEquals("test", entities.get(0).getName());
 assertEquals("test", entities.get(1).getName());
 }
}