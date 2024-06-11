package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<TestEntity, Serializable> testEntityInformation;

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private EnhancedJpaRepository<TestEntity, Serializable> enhancedJpaRepository;

 @Before
 public void setupMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testConstructor() {
 EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(testEntityInformation, entityManager);
 assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
 assertNotNull(enhancedJpaRepository.getEntityManager());
 assertNotNull(enhancedJpaRepository.getDataSource());
 assertNotNull(enhancedJpaRepository.getJdbcTemplate());
 }

 @Test
 public void testGetDataSource() {
 when(entityManager.getEntityManagerFactory()).thenReturn(createEntityManagerFactoryMock());
 Session session = setupSession();
 when(entityManager.getSessionFactory().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(createConnectionProviderMock());
 DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
 assertNotNull(dataSource);
 }

 @Test
 public void testGetEntityManager() {
 assertSame(entityManager, enhancedJpaRepository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
 assertNotNull(jdbcTemplate);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
 assertNotNull(namedParameterJdbcTemplate);
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("TestName");
 when(entityManager.merge(testEntity)).thenReturn(testEntity);
 enhancedJpaRepository.save(testEntity);
 assertEquals(testEntity, enhancedJpaRepository.findOne(testEntity.getId()));
 }

 @Test
 public void testFindAll() {
 List<TestEntity> testEntities = new ArrayList<>();
 TestEntity testEntity1 = new TestEntity();
 testEntity1.setName("TestName1");
 TestEntity testEntity2 = new TestEntity();
 testEntity2.setName("TestName2");
 testEntities.add(testEntity1);
 testEntities.add(testEntity2);
 when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(setupQueryMockWithEntities(testEntities));
 List<TestEntity> result = enhancedJpaRepository.findAll();
 assertEquals(testEntities.size(), result.size());
 for (TestEntity entity : testEntities) {
 assertEquals(entity, result.get(result.indexOf(entity)));
 }
 }

 @Test
 public void testFindOne() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("TestName");
 Long id = testEntity.getId();
 TestEntity expectedEntity = new TestEntity();
 expectedEntity.setName("TestName");
 expectedEntity.setId(id);
 when(entityManager.find(TestEntity.class, id)).thenReturn(expectedEntity);
 TestEntity result = enhancedJpaRepository.findOne(id);
 assertEquals(expectedEntity, result);
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("TestName");
 Long id = testEntity.getId();
 TestEntity expectedEntity = new TestEntity();
 expectedEntity.setName("TestName");
 expectedEntity.setId(id);
 when(entityManager.find(TestEntity.class, id)).thenReturn(expectedEntity);
 TestEntity result = enhancedJpaRepository.findById(id).orElse(null);
 assertEquals(expectedEntity, result);
 }

 @Test
 public void testFindAllByProperty() {
 TestEntity testEntity1 = new TestEntity();
 testEntity1.setName("TestName1");
 TestEntity testEntity2 = new TestEntity();
 testEntity2.setName("TestName2");
 List<TestEntity> testEntities = new ArrayList<>();
 testEntities.add(testEntity1);
 testEntities.add(testEntity2);
 Map<String, Object> filter = Map.of("propertyName", "TestValue");
 when(namedParameterJdbcTemplate.query(setupNamedQueryMock(), filter)).thenReturn(setupRowMapperMock(testEntities));
 List<TestEntity> result = enhancedJpaRepository.findAllByProperty(filter);
 assertEquals(testEntities.size(), result.size());
 for (TestEntity entity : testEntities) {
 assertEquals(entity, result.get(result.indexOf(entity)));
 }
 }

 @After
 public void teardown() {
 // Do any necessary cleanup here
 }

 private EntityManagerFactory createEntityManagerFactoryMock() {
 return mock(EntityManagerFactory.class);
 }

 private ConnectionProvider createConnectionProviderMock() {
 return mock(ConnectionProvider.class);
 }

 private Session setupSession() {
 return mock(Session.class);
 }

 private Transaction setupTransactionMock() {
 return mock(Transaction.class);
 }

 private Query createQueryMock() {
 return mock(Query.class);
 }

 private Query setResultListMock(List<?> resultList) {
 when(query.list()).thenReturn(resultList);
 return query;
 }

 private Query setupQueryMockWithEntities(List<?> entities) {
 Query query = mock(Query.class);
 setupQueryMock();
 setResultListMock(entities);
 return query;
 }

 private NamedQuery setupNamedQueryMock() {
 return mock(NamedQuery.class);
 }

 private RowMapper setupRowMapperMock(List<?> entities) {
 RowMapper rowMapper = mock(RowMapper.class);
 when(rowMapper.mapRow(row, rowNumber)).thenAnswer(i -> entities.get(i.getIntValue(0) - 1));
 return rowMapper;
 }

}

class TestEntity {
 private Long id;
 private String name;

 public TestEntity() {
 }

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

}