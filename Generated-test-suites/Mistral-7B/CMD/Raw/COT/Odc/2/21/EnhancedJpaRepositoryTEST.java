import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation jpaEntityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(mock(DatasourceConnectionProviderImpl.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DataSource.class))
                .thenReturn(dataSource);
    }

    @Test
    public void testConstructor() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager);
        Assert.assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
        Assert.assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager);
        Assert.assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager);
        Assert.assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityManagerNullEntityManager() {
        when(entityManager).thenReturn(null);
        enhancedJpaRepository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullEntityManager() {
        when(entityManager).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(null);
        enhancedJpaRepository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager);
    }

}