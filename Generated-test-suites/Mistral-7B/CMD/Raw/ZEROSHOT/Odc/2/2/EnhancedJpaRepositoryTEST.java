import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setUp() {
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(mock(DatasourceConnectionProviderImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl() {
            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        });
    }

    @Test
    public void testConstructor() {
        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        when(entityManager.unwrap(JpaEntityInformation.class)).thenReturn(entityInformation);
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        DataSource dataSource = mock(DataSource.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl() {
            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
        });

        when(entityManager.unwrap(EntityManager.class)).thenReturn(entityManager);
        assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

}

class TestEntity {
}