import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import static org.junit.Assert.*;

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
 Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
 Mockito.when(entityManager.getDataSource()).thenReturn(Mockito.mock(DataSource.class));
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
 assertNotNull(jdbcTemplate);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
 assertNotNull(namedParameterJdbcTemplate);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = repository.getDataSource(entityManager);
 assertNotNull(dataSource);
 }
}