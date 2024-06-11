import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.SimpleJpaRepository;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.WorkItem;
import org.hibernate.jdbc.WorkItemDelegate;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<TestEntity, Long> repository;
 @Mock private DataSource dataSource;
 @Mock private EntityManager entityManager;
 @Mock private JpaEntityInformation<TestEntity, Long> entityInformation;
 @Mock private ConnectionProvider connectionProvider;
 @Mock private SessionFactoryImpl sessionFactory;
 @Mock private DatasourceConnectionProviderImpl datasourceConnectionProvider;
 @Mock private WorkItem workItem;
 @Mock private WorkItemDelegate workItemDelegate;
 @Mock private PlatformTransactionManager transactionManager;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(connectionProvider);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);
 when(sessionFactory.getServiceRegistry().getService(PlatformTransactionManager.class))
 .thenReturn(transactionManager);
 when(entityManager.getEntityManagerFactory())
 .thenReturn(new SessionFactoryImpl());
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(sessionFactory);
 when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);
 this.repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
 }

 @Test
 public void testGetDataSource() {
 DataSource actualDataSource = repository.getDataSource(entityManager);
 verify(entityManager).getEntityManagerFactory();
 verify(sessionFactory).getServiceRegistry();
 verify(connectionProvider).getDataSource();
 assertEquals(actualDataSource, dataSource);
 }

 @Test
 public void testGetEntityManager() {
 EntityManager actualEntityManager = repository.getEntityManager();
 assertEquals(actualEntityManager, entityManager);
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate actualJdbcTemplate = repository.getJdbcTemplate();
 assertNotNull(actualJdbcTemplate);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate actualNamedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
 assertNotNull(actualNamedParameterJdbcTemplate);
 }
}

class TestEntity {
 // Entity implementation
}