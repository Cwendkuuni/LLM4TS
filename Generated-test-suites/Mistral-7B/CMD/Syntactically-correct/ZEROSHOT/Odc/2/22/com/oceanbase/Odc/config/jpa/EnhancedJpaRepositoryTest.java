import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.runner.JUnitRunner;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaTestConfiguration;

@RunWith(JUnitRunner.class)
@SpringBootTest
@SpringJUnitConfig(classes = JpaTestConfiguration.class)
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> repository;

 @Autowired
 private EntityManager entityManager;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.repository = new EnhancedJpaRepository<>(TestEntity.class.getJavaClass(), Long.class,
 entityManager);
 }

 @After
 public void tearDown() {
 this.repository = null;
 }

 // Test method for constructor and getters
 @org.junit.Test
 public void testConstructorAndGetters() {
 assertNotNull(repository);
 assertEquals(entityManager, repository.getEntityManager());
 assertNotNull(repository.getNamedParameterJdbcTemplate());
 assertNotNull(repository.getJdbcTemplate());
 }

 // Test method for getDataSource
 @org.junit.Test
 public void testGetDataSource() {
 DataSource dataSource = repository.getDataSource(entityManager);
 assertNotNull(dataSource);
 }
}

 class TestEntity {
 // Add your entity class fields and getters here
}