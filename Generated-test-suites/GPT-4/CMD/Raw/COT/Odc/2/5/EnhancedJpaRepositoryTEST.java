
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;
    
    @Mock
    private JpaEntityInformation entityInformation;
    
    @Mock
    private SessionFactoryImpl sessionFactory;
    
    @Mock
    private DatasourceConnectionProviderImpl datasourceConnectionProvider;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProvider);
        when(datasourceConnectionProvider.getDataSource()).thenReturn(dataSource);

        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
    }
    
    @Test
    public void testGetEntityManager() {
        EntityManager em = repository.getEntityManager();
        assertNotNull(em);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate npTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(npTemplate);
    }
}
