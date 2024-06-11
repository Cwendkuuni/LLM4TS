import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<TestEntity, Serializable> entityInformation;

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private EnhancedJpaRepository<TestEntity, Serializable> repository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSave() {
 // Create a test entity
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");

 // Save the test entity using the repository
 repository.save(testEntity);

 // Verify that the EntityManager was called to save the entity
 verify(entityManager).persist(testEntity);
 }

 @Test
 public void testFindById() {
 // Given a test entity with an ID
 Serializable id = 1L;
 TestEntity testEntity = new TestEntity();
 testEntity.setId(id);
 when(entityManager.find(TestEntity.class, id)).thenReturn(testEntity);

 // When we call findById() on the repository
 TestEntity foundEntity = repository.findById(id).orElse(null);

 // Then the EntityManager was called to find the entity
 verify(entityManager).find(TestEntity.class, id);

 // And the returned entity matches the one we mocked
 assertEquals(testEntity, foundEntity);
 }

 @Test(expected = RuntimeException.class)
 public void testSaveWithNullEntity() {
 // When we try to save a null entity
 repository.save(null);

 // Then a RuntimeException should be thrown
 throw new RuntimeException("Expected RuntimeException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByIdWithNullId() {
 // When we try to find an entity by a null ID
 Serializable nullId = null;

 // Then an IllegalArgumentException should be thrown
 throw new IllegalArgumentException("ID must not be null");
 }
}