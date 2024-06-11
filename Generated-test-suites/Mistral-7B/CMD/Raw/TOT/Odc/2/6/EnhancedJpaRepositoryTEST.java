import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepositoryTests;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

import static org.junit.Assert.*;

public class EnhancedJpaRepositoryTests {

 private EnhancedJpaRepository<TestEntity, Long> repository;
 private TestEntityRepository testEntityRepository;
 private EntityManager entityManager;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 testEntityRepository = Mockito.mock(TestEntityRepository.class);
 entityManager = Mockito.mock(EntityManager.class);
 repository = new EnhancedJpaRepository<>(TestEntity.class.getJavaClass().getAnnotation(JpaEntity.class), testEntityRepository, entityManager);
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setId(1L);
 Mockito.when(entityManager.find(TestEntity.class, 1L)).thenReturn(testEntity);
 TestEntity foundEntity = repository.findOne(1L);
 assertEquals(testEntity, foundEntity);
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 Mockito.when(entityManager.persist(testEntity)).thenReturn(testEntity);
 TestEntity savedEntity = repository.save(testEntity);
 assertEquals(testEntity, savedEntity);
 }

 @Test
 public void testFindAll() {
 TestEntity testEntity1 = new TestEntity();
 TestEntity testEntity2 = new TestEntity();
 testEntity1.setName("Test Entity 1");
 testEntity2.setName("Test Entity 2");
 Mockito.when(entityManager.createQuery("FROM TestEntity", TestEntity.class)).thenReturn(Mockito.mock(javax.persistence.TypedQuery.class));
 Mockito.when(Mockito.mock(javax.persistence.TypedQuery.class).getResultList()).thenReturn(java.util.Arrays.asList(testEntity1, testEntity2));
 java.util.List<TestEntity> allEntities = repository.findAll();
 assertEquals(2, allEntities.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByIdInvalidId() {
 TestEntity testEntity = new TestEntity();
 repository.findOne(-1L);
 }

 @Test
 public void testDeleteById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setId(1L);
 Mockito.doNothing().when(entityManager).remove(testEntity);
 repository.delete(1L);
 }

 @Test
 public void testNamedParameterQuery() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.anyMapOf(String.class, Object.class))).thenReturn(new Object[]{testEntity});
 TestEntity foundEntity = repository.findByName("Test Entity");
 assertEquals(testEntity, foundEntity);
 }

 @Test
 public void testNamedParameterUpdate() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 testEntity.setId(1L);
 Mockito.when(entityManager.merge(testEntity)).thenReturn(testEntity);
 repository.updateNamedParameter("updateTestEntityName", testEntity);
 }

}