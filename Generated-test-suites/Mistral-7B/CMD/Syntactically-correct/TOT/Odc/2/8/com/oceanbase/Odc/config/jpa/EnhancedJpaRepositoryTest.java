import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepositoryTest;
import com.oceanbase.odc.config.jpa.entity.TestEntity;

public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;
 private JdbcTemplate jdbcTemplate;

 @BeforeEach
 public void setup() {
 LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
 em.setPackagesToScan("com.oceanbase.odc.config.jpa.entity");
 em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
 em.setDataSource(new EmbeddedDatabaseBuilder()
 .setType(EmbeddedDatabaseType.HSQL)
 .addScript("schema.sql")
 .build());
 AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
 context.register(EnhancedJpaRepositoryTest.class);
 context.refresh();
 enhancedJpaRepository = context.getBean(EnhancedJpaRepository.class);
 jdbcTemplate = context.getBean(JdbcTemplate.class);
 }

 @Test
 public void testFindById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setId(1L);
 testEntity.setName("TestEntity1");
 enhancedJpaRepository.save(testEntity);

 TestEntity fetchedEntity = enhancedJpaRepository.findById(1L).orElseThrow(() -> new RuntimeException("Entity not found"));

 Assertions.assertEquals(testEntity.getName(), fetchedEntity.getName());
 }

 @Test
 public void testSave() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("TestEntity2");

 enhancedJpaRepository.save(testEntity);

 TestEntity savedEntity = enhancedJpaRepository.findById(1L).orElseThrow(() -> new RuntimeException("Entity not found"));

 Assertions.assertEquals(testEntity.getName(), savedEntity.getName());
 }

 @Test
 public void testFindAll() {
 TestEntity testEntity1 = new TestEntity();
 testEntity1.setName("TestEntity3");
 TestEntity testEntity2 = new TestEntity();
 testEntity2.setName("TestEntity4");

 enhancedJpaRepository.saveAll(Arrays.asList(testEntity1, testEntity2));

 List<TestEntity> testEntities = enhancedJpaRepository.findAll();

 Assertions.assertEquals(testEntities.size(), 2);
 }

 @Test
 public void testDeleteById() {
 TestEntity testEntity = new TestEntity();
 testEntity.setId(1L);
 testEntity.setName("TestEntity5");
 enhancedJpaRepository.save(testEntity);

 enhancedJpaRepository.deleteById(1L);

 TestEntity deletedEntity = enhancedJpaRepository.findById(1L).orElse(null);

 Assertions.assertNull(deletedEntity);
 }

 @Test
 public void testFindByName() {
 TestEntity testEntity1 = new TestEntity();
 testEntity1.setName("TestEntity6");
 TestEntity testEntity2 = new TestEntity();
 testEntity2.setName("TestEntity7");
 testEntity2.setId(2L);
 enhancedJpaRepository.saveAll(Arrays.asList(testEntity1, testEntity2));

 List<TestEntity> testEntities = enhancedJpaRepository.findByName("TestEntity6");

 Assertions.assertEquals(testEntities.size(), 1);
 }

 @Test
 public void testNamedParamQuery() {
 TestEntity testEntity = new TestEntity();
 testEntity.setName("TestEntity8");
 testEntity.setId(3L);
 enhancedJpaRepository.save(testEntity);

 String query = "SELECT e FROM TestEntity e WHERE e.name = :name";
 List<TestEntity> testEntities = jdbcTemplate.query(query, Map.of("name", "TestEntity8"));

 Assertions.assertEquals(testEntities.size(), 1);
 }

 @Test
 public void testExceptionScenario() {
 try {
 enhancedJpaRepository.findById(Long.MAX_VALUE);
 } catch (RuntimeException e) {
 // Expected
 }
 }
}