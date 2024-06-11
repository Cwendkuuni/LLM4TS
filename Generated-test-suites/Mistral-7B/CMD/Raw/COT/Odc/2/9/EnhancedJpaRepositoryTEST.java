import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> repository;
 private JdbcTemplate jdbcTemplate;
 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
 private EntityManager entityManager;
 private DataSource dataSource;

 @Before
 public void setup() {
 JpaEntityInformation<TestEntity, Long> entityInformation = Mockito.mock(JpaEntityInformation.class);
 entityManager = Mockito.mock(EntityManager.class);
 dataSource = Mockito.mock(DataSource.class);

 jdbcTemplate = Mockito.mock(JdbcTemplate.class);
 namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

 repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
 repository.setDataSource(dataSource);
 repository.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
 repository.setEntityManager(entityManager);
 }

 @Test
 public void testConstructor() {
 // Test for the constructor with correct arguments
 assert(repository != null);
 }

 @Test
 public void testGetDataSource() {
 // Test for getDataSource method
 Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(org.hibernate.SessionFactory.class));
 Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(((DatasourceConnectionProviderImpl) Mockito.mock(org.hibernate.engine.jdbc.connections.spi.ConnectionProvider.class)).getDataSource()).thenReturn(dataSource);

 Assert.assertEquals(dataSource, repository.getDataSource());
 }

 @Test
 public void testGetEntityManager() {
 // Test for getEntityManager method
 Assert.assertEquals(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 // Test for getJdbcTemplate method
 Assert.assertEquals(jdbcTemplate, repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 // Test for getNamedParameterJdbcTemplate method
 Assert.assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullEntityManager() {
 // Test for getDataSource with null entityManager
 repository.setEntityManager(null);
 repository.getDataSource();
 }

 @Test(expected = NullPointerException.class)
 public void testGetNamedParameterJdbcTemplateWithNullDataSource() {
 // Test for getNamedParameterJdbcTemplate with null dataSource
 repository.setDataSource(null);
 repository.getNamedParameterJdbcTemplate();
 }

}