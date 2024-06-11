import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Long> entityInformation;

    @InjectMocks
    private EnhancedJpaRepository<Object, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        initMocks(this);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, mock(EntityManager.class));
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        DataSource dataSource = mock(DataSource.class);

        when(enhancedJpaRepository.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(createEntityManagerFactoryMock());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(createSessionFactoryMock());
        when(createSessionFactoryMock().getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(createConnectionProviderMock());
        when(createConnectionProviderMock().getDataSource()).thenReturn(dataSource);

        DataSource ds = enhancedJpaRepository.getDataSource(entityManager);
        assertEquals(dataSource, ds);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        when(enhancedJpaRepository.getEntityManager()).thenReturn(entityManager);

        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        when(enhancedJpaRepository.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getFactory()).thenReturn(createEntityManagerFactoryMock());
        when(createEntityManagerFactoryMock().getJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);

        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        when(enhancedJpaRepository.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getFactory()).thenReturn(createEntityManagerFactoryMock());
        when(createEntityManagerFactoryMock().getJdbcOperations()).thenReturn(createJdbcOperationsMock());
        when(createJdbcOperationsMock().getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);

        NamedParameterJdbcTemplate namedTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, namedTemplate);
    }

    private SessionFactoryImpl createSessionFactoryMock() {
        return mock(SessionFactoryImpl.class);
    }

    private EntityManagerFactory createEntityManagerFactoryMock() {
        return mock(EntityManagerFactory.class);
    }

    private ConnectionProvider createConnectionProviderMock() {
        return mock(ConnectionProvider.class);
    }

    private JdbcOperations createJdbcOperationsMock() {
        return mock(JdbcOperations.class);
    }

}