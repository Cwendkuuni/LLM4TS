import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaRepository<TestEntity, Long> testRepository;

 @InjectMocks
 private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

 @Mock
 private EntityManager entityManager;

 @Mock
 private DataSource dataSource;

 @Mock
 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

 @Mock
 private JdbcTemplate jdbcTemplate;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
 when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
 when(entityManager.unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dataSource);
 when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
 when(testRepository.getEntityManager()).thenReturn(entityManager);
 when(testRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
 when(testRepository.getJdbcTemplate()).thenReturn(jdbcTemplate);
 }

 @Test
 public void testGetEntityManager() {
 // Test getting EntityManager from the repository
 TestEntity testEntity = new TestEntity();
 assertSame(entityManager, enhancedJpaRepository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 // Test getting JdbcTemplate from the repository
 TestEntity testEntity = new TestEntity();
 assertSame(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 // Test getting NamedParameterJdbcTemplate from the repository
 TestEntity testEntity = new TestEntity();
 assertSame(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
 }

 // Add more test cases for various methods and scenarios here

 // For example, test findAll method with empty and populated lists
 @Test
 public void testFindAll() {
 // Given
 TestEntity testEntity1 = new TestEntity();
 TestEntity testEntity2 = new TestEntity();
 testRepository.save(testEntity1);
 testRepository.save(testEntity2);

 // When
 Iterable<TestEntity> result = enhancedJpaRepository.findAll();

 // Then
 assertThat(result.size()).isEqualTo(2);
 }

 // Test findAll method with an empty list
 @Test
 public void testFindAllEmpty() {
 // Given (no entities saved)

 // When
 Iterable<TestEntity> result = enhancedJpaRepository.findAll();

 // Then
 assertThat(result.isEmpty()).isTrue();
 }

 // Test save method with a new entity
 @Test
 public void testSaveNewEntity() {
 // Given
 TestEntity testEntity = new TestEntity();

 // When
 enhancedJpaRepository.save(testEntity);

 // Then
 verify(entityManager).persist(testEntity);
 }

 // Test save method with an existing entity
 @Test
 public void testSaveExistingEntity() {
 // Given
 TestEntity testEntity = new TestEntity();
 testRepository.save(testEntity);

 // When
 enhancedJpaRepository.save(testEntity);

 // Then
 verify(entityManager, never()).persist(testEntity);
 }

 // Test deleteById method
 @Test
 public void testDeleteById() {
 // Given
 TestEntity testEntity = new TestEntity();
 testRepository.save(testEntity);
 Long id = testEntity.getId();

 // When
 enhancedJpaRepository.deleteById(id);

 // Then
 verify(namedParameterJdbcTemplate).update("delete from TestEntity where id = :id", new String[]{"id"}, new Object[]{id});
 }

 // Test delete method
 @Test
 public void testDelete() {
 // Given
 TestEntity testEntity = new TestEntity();
 testRepository.save(testEntity);

 // When
 enhancedJpaRepository.delete(testEntity);

 // Then
 verify(entityManager).remove(testEntity);
 }
}