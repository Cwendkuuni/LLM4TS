import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

    @Before
    public void setUp() {
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    // Testing constructor
    @Test
    public void testEnhancedJpaRepositoryConstructor() {
        assert(enhancedJpaRepository != null);
    }

    // Testing getEntityManager method
    @Test
    public void testGetEntityManager() {
        assert(enhancedJpaRepository.getEntityManager() == entityManager);
    }

    // Testing getJdbcTemplate method
    @Test
    public void testGetJdbcTemplate() {
        assert(enhancedJpaRepository.getJdbcTemplate() == namedParameterJdbcTemplate.getJdbcOperations());
    }

    // Testing getNamedParameterJdbcTemplate method
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assert(enhancedJpaRepository.getNamedParameterJdbcTemplate() == namedParameterJdbcTemplate);
    }

    // Testing getDataSource method (edge case: null entityManager)
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullEntityManager() {
        EnhancedJpaRepository<Object, Serializable> enhancedJpaRepositoryNullEntityManager = new EnhancedJpaRepository<>(null, null);
        enhancedJpaRepositoryNullEntityManager.getDataSource(null);
    }

    // Testing getDataSource method (edge case: null sessionFactory)
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullSessionFactory() {
        when(entityManager.getEntityManagerFactory()).thenReturn(null);
        EnhancedJpaRepository<Object, Serializable> enhancedJpaRepositoryNullSessionFactory = new EnhancedJpaRepository<>(entityInformation, entityManager);
        enhancedJpaRepositoryNullSessionFactory.getDataSource(entityManager);
    }
}