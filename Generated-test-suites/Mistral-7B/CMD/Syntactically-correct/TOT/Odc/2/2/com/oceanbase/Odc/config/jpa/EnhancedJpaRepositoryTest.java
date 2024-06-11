import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.entity.TestEntity;
import com.oceanbase.odc.config.jpa.repository.TestEntityRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
 private TestEntityRepository testEntityRepository;

 @InjectMocks
 private EnhancedJpaRepository<TestEntity, Long> testSubject;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 testEntityRepository = mock(TestEntityRepository.class);
 when(testEntityRepository.findAll(Mockito.any(Pageable.class))).thenReturn(Mockito.mock(Page.class));
 enhancedJpaRepository = new EnhancedJpaRepository<>(TestEntity.class.getJavaClass().getDeclaredField("id"), testEntityRepository);
 }

 @Test
 public void testFindAll() {
 Page<TestEntity> entities = testSubject.findAll(Mockito.any(Pageable.class));
 assertEquals(0, entities.getTotalElements());
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setId(1L);
 when(testEntityRepository.findById(Mockito.eq(1L))).thenReturn(java.util.Optional.ofNullable(testEntity));
 TestEntity entity = testSubject.findById(1L).orElse(null);
 assertEquals(testEntity, entity);
 }

 @Test(expected = RuntimeException.class)
 public void testFindById_NotFound() {
 when(testEntityRepository.findById(Mockito.eq(1L))).thenReturn(java.util.Optional.empty());
 testSubject.findById(1L).orElse(null);
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 testEntity = testSubject.save(testEntity);
 assertEquals("Test Entity", testEntity.getName());
 }

 @Test(expected = RuntimeException.class)
 public void testSave_ValidationError() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName(null);
 testSubject.save(testEntity);
 }

 @Test
 public void testDeleteById() {
 testSubject.deleteById(1L);
 // verify that delete query was executed
 }

 @Test(expected = RuntimeException.class)
 public void testDeleteById_NotFound() {
 testSubject.deleteById(1L);
 }

 @Test
 public void testNamedQuery() {
 String query = "SELECT e FROM TestEntity e WHERE e.name = :name";
 Page<TestEntity> entities = testSubject.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")), query, Map.of("name", "Test Entity"));
 assertEquals(1, entities.getTotalElements());
 }

 @Test(expected = RuntimeException.class)
 public void testNamedQuery_InvalidQuery() {
 String query = "SELECT e FROM TestEntity e WHERE e.name = :name and 1=1";
 testSubject.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")), query, Map.of("name", "Test Entity"));
 }
}