import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> repository;
 private TestEntity testEntity;
 private EntityManager entityManagerMock;
 private JdbcTemplate jdbcTemplateMock;
 private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;

 @Before
 public void setup() {
 testEntity = new TestEntity();
 testEntity.setId(1L);
 testEntity.setName("Test Entity");

 entityManagerMock = Mockito.mock(EntityManager.class);
 jdbcTemplateMock = Mockito.mock(JdbcTemplate.class);
 namedParameterJdbcTemplateMock = Mockito.mock(NamedParameterJdbcTemplate.class);

 repository = new EnhancedJpaRepository<>(TestEntityJpaInformation.getInstance(), entityManagerMock) {
 @Override
 public JdbcTemplate getJdbcTemplate() {
 return jdbcTemplateMock;
 }

 @Override
 public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
 return namedParameterJdbcTemplateMock;
 }
 };

 Mockito.when(entityManagerMock.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(entityManagerMock.unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(entityManagerMock.getDataSource()).thenReturn(Mockito.mock(DataSource.class));
 Mockito.when(entityManagerMock.getEntityManager()).thenReturn(entityManagerMock);
 Mockito.when(entityManagerMock.find(TestEntity.class, 1L)).thenReturn(testEntity);
 Mockito.when(entityManagerMock.saveAndFlush(testEntity)).thenReturn(testEntity);
 Mockito.when(entityManagerMock.findAll()).thenReturn(new ArrayList<>());
 Mockito.when(entityManagerMock.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(Mockito.mock(javax.persistence.Query.class));

 Mockito.when(namedParameterJdbcTemplateMock.getJdbcOperations()).thenReturn(jdbcTemplateMock);
 }

 @Test
 public void testFindById() {
 TestEntity expectedTestEntity = testEntity;
 TestEntity actualTestEntity = repository.findById(1L).orElseThrow(RuntimeException::new);
 assertEquals(expectedTestEntity, actualTestEntity);
 }

 @Test
 public void testSaveAndFlush() {
 TestEntity testEntityToSave = new TestEntity();
 TestEntity savedTestEntity = repository.saveAndFlush(testEntityToSave);
 assertEquals(testEntityToSave, savedTestEntity);
 }

 @Test
 public void testFindAll() {
 List<TestEntity> expectedTestEntities = new ArrayList<>();
 expectedTestEntities.add(testEntity);
 List<TestEntity> actualTestEntities = repository.findAll();
 assertEquals(expectedTestEntities, actualTestEntities);
 }

 @Test
 public void testFindAllWithPageable() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<TestEntity> expectedPage = new PageImpl<>(new ArrayList<>());
 expectedPage.setContent(new ArrayList<>());
 Page<TestEntity> actualPage = repository.findAll(pageable);
 assertEquals(expectedPage, actualPage);
 }

 @Test
 public void testNamedParameterQuery() {
 String queryString = "SELECT e FROM TestEntity e WHERE e.name = :name";
 Map<String, Object> paramMap = Map.of("name", "Test Entity");
 List<TestEntity> expectedTestEntities = new ArrayList<>();
 expectedTestEntities.add(testEntity);
 List<TestEntity> actualTestEntities = repository.getNamedParameterJdbcTemplate().query(queryString, paramMap, TestEntity.class);
 assertEquals(expectedTestEntities, actualTestEntities);
 }

 @Test(expected = RuntimeException.class)
 public void testFindByIdNotFound() {
 assertNull(repository.findById(2L));
 }
}