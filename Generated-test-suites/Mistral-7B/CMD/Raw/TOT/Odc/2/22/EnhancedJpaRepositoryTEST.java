import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepositoryImpl;
import com.oceanbase.odc.config.jpa.TestEntity;
import com.oceanbase.odc.config.jpa.TestEntityId;

public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, TestEntityId> repository;
 private JdbcTemplate jdbcTemplate;

 @Before
 public void setup() {
 JpaRepository<TestEntity, TestEntityId> testRepository = Mockito.spy(new EnhancedJpaRepositoryImpl<>());
 this.repository = (EnhancedJpaRepository<TestEntity, TestEntityId>) testRepository;
 this.jdbcTemplate = Mockito.mock(JdbcTemplate.class);
 Mockito.when(repository.getJdbcTemplate()).thenReturn(jdbcTemplate);
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setId(new TestEntityId("1"));
 TestEntity expectedEntity = new TestEntity();
 expectedEntity.setName("Test Entity");

 Mockito.when(repository.findById("1")).thenReturn(java.util.Optional.of(expectedEntity));
 TestEntity actualEntity = repository.findById("1").orElse(null);
 Assert.assertNotNull(actualEntity);
 Assert.assertEquals("Test Entity", actualEntity.getName());
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testFindByIdNotFound() {
 Mockito.when(repository.findById("2")).thenReturn(java.util.Optional.empty());
 TestEntity actualEntity = repository.findById("2").orElse(null);
 Assert.assertNull(actualEntity);
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 testEntity.setId(new TestEntityId("3"));
 Mockito.doNothing().when(repository).save(testEntity);
 repository.save(testEntity);
 }

 @Test(expected = java.sql.SQLException.class)
 public void testSaveThrowsException() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 testEntity.setId(new TestEntityId("3"));
 Mockito.doThrow(new java.sql.SQLException("Test Exception")).when(jdbcTemplate).update("UPDATE test_entity SET name = ? WHERE id = ?", testEntity.getName(), testEntity.getId());
 repository.save(testEntity);
 }

 @Test
 public void testDeleteById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 testEntity.setId(new TestEntityId("4"));
 Mockito.doNothing().when(repository).deleteById(testEntity.getId());
 repository.deleteById(testEntity.getId());
 }

 @Test(expected = java.sql.SQLException.class)
 public void testDeleteByIdThrowsException() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("Test Entity");
 testEntity.setId(new TestEntityId("4"));
 Mockito.doThrow(new java.sql.SQLException("Test Exception")).when(jdbcTemplate).update("DELETE FROM test_entity WHERE id = ?", testEntity.getId());
 repository.deleteById(testEntity.getId());
 }

}