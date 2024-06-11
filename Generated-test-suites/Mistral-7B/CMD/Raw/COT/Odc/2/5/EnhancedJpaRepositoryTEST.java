import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Assert;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> jpaEntityInformation;

 @Mock
 private EntityManager entityManager;

 @Mock
 private ServiceRegistry serviceRegistry;

 @Mock
 private SessionFactoryImpl sessionFactoryImpl;

 @Mock
 private ConnectionProvider connectionProvider;

 @Mock
 private DataSource dataSource;

 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

 @Test
 public void testConstructor() {
 // Basic functionality test
 EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(jpaEntityInformation, entityManager);
 Assert.assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
 Assert.assertNotNull(enhancedJpaRepository.getEntityManager());

 // Test constructor with mocked dependencies
 EnhancedJpaRepository enhancedJpaRepositoryWithMocks = new EnhancedJpaRepository(jpaEntityInformation, entityManager) {
 private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
 private final EntityManager entityManager;

 EnhancedJpaRepository(JpaEntityInformation<Object, Serializable> jpaEntityInformation, EntityManager entityManager) {
 super(jpaEntityInformation, entityManager);
 this.entityManager = entityManager;
 this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource(entityManager));
 }

 @Override
 protected DataSource getDataSource(EntityManager entityManager) {
 return dataSource;
 }
 };

 Assert.assertEquals(enhancedJpaRepositoryWithMocks.getNamedParameterJdbcTemplate(), enhancedJpaRepositoryWithMocks.getNamedParameterJdbcTemplate());
 Assert.assertEquals(enhancedJpaRepositoryWithMocks.getEntityManager(), entityManager);
 }

 @Test
 public void testGetEntityManager() {
 // Basic functionality test
 Assert.assertEquals(enhancedJpaRepository.getEntityManager(), entityManager);

 // Test getEntityManager with mocked dependencies
 when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactoryImpl);
 when(sessionFactoryImpl.getServiceRegistry()).thenReturn(serviceRegistry);
 when(serviceRegistry.getService(ConnectionProvider.class)).thenReturn(connectionProvider);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);

 EnhancedJpaRepository enhancedJpaRepositoryWithMocks = mock(EnhancedJpaRepository.class);
 when(enhancedJpaRepositoryWithMocks.getEntityManager()).thenCallRealMethod();

 Assert.assertEquals(enhancedJpaRepositoryWithMocks.getEntityManager(), entityManager);
 }

 @Test
 public void testGetJdbcTemplate() {
 // Basic functionality test
 Assert.assertNotNull(enhancedJpaRepository.getJdbcTemplate());

 // Test getJdbcTemplate with mocked dependencies
 when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));

 Assert.assertEquals(enhancedJpaRepository.getJdbcTemplate(), enhancedJpaRepository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 // Basic functionality test
 Assert.assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());

 // Test getNamedParameterJdbcTemplate with mocked dependencies
 when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));

 Assert.assertEquals(enhancedJpaRepository.getNamedParameterJdbcTemplate(), enhancedJpaRepository.getNamedParameterJdbcTemplate());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullEntityManager() {
 EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(jpaEntityInformation, null);
 enhancedJpaRepository.getDataSource(entityManager);
 }

 @Test
 public void testGetDataSource() {
 // Basic functionality test
 EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(jpaEntityInformation, entityManager);
 Assert.assertNotNull(enhancedJpaRepository.getDataSource(entityManager));

 // Test getDataSource with mocked dependencies
 when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactoryImpl);
 when(sessionFactoryImpl.getServiceRegistry()).thenReturn(serviceRegistry);
 when(serviceRegistry.getService(ConnectionProvider.class)).thenReturn(connectionProvider);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);

 EnhancedJpaRepository enhancedJpaRepositoryWithMocks = mock(EnhancedJpaRepository.class);
 when(enhancedJpaRepositoryWithMocks.getDataSource(entityManager)).thenCallRealMethod();

 Assert.assertEquals(enhancedJpaRepositoryWithMocks.getDataSource(entityManager), dataSource);
 }
}