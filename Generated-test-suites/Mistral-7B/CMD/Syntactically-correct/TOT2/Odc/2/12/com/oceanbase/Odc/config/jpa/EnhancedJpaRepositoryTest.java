import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;

    @Test
    public void testConstructor() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(ConnectionProvider.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class)).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        Mockito.verify(entityManager, Mockito.times(1)).getEntityManagerFactory();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullEntityInformation() {
        repository = new EnhancedJpaRepository<>(null, entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullEntityManager() {
        repository = new EnhancedJpaRepository<>(entityInformation, null);
    }

    @Test
    public void testGetEntityManager() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(ConnectionProvider.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class)).thenReturn(dataSource);

        Mockito.when(entityManager.isOpen()).thenReturn(true);
        Mockito.when(entityManager.isClosed()).thenReturn(false);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        EnhancedJpaRepository<Object, Serializable> newRepository = new EnhancedJpaRepository<>(entityInformation, Mockito.mock(EntityManager.class));
        Mockito.verify(entityManager, Mockito.times(1)).isOpen();
        Mockito.verify(entityManager, Mockito.times(1)).isClosed();
        Mockito.verify(entityManager, Mockito.times(1)).getEntityManagerFactory();

        Mockito.verify(entityManager, Mockito.atLeastOnce()).close();
        Mockito.verify(entityManager, Mockito.atLeastOnce()).flush();
        Mockito.verify(entityManager, Mockito.atLeastOnce()).clear();
        Mockito.verify(entityManager, Mockito.atLeastOnce()).getTransaction();
        Mockito.verify(entityManager, Mockito.atLeastOnce()).getTransaction().commit();
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityManagerNullEntityManager() {
        repository = new EnhancedJpaRepository<>(entityInformation, null);
    }

    @Test
    public void testGetJdbcTemplate() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(ConnectionProvider.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class)).thenReturn(dataSource);

        Mockito.when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(Mockito.mock(JdbcOperations.class));

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        Mockito.verify(namedParameterJdbcTemplate).getJdbcOperations();
    }

    @Test(expected = NullPointerException.class)
    public void testGetJdbcTemplateNullNamedParameterJdbcTemplate() {
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        repository.setNamedParameterJdbcTemplate(null);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(ConnectionProvider.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class)).thenReturn(dataSource);

        Mockito.when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(Mockito.mock(JdbcOperations.class));

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1)).getJdbcOperations();
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamedParameterJdbcTemplateNullNamedParameterJdbcTemplate() {
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        repository.setNamedParameterJdbcTemplate(null);
    }

    @Test
    public void testGetDataSource() {
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        Mockito.when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(Mockito.mock(ConnectionProvider.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(Mockito.mock(SessionFactoryImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class));
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(DatasourceConnectionProviderImpl.class)).thenReturn(Mockito.mock(DatasourceConnectionProviderImpl.class)).thenReturn(dataSource);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        DataSource dataSource = repository.getDataSource();
        Mockito.verify(namedParameterJdbcTemplate).getDataSource();
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullNamedParameterJdbcTemplate() {
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        repository.setNamedParameterJdbcTemplate(null);
    }
}