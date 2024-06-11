import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @InjectMocks
    private EnhancedJpaRepository<Entity, Long> enhancedJpaRepository;

    @Mock
    private JpaEntityInformation<Entity, Long> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Test
    public void testGetEntityManager() {
        when(entityManager.getEntityManagerFactory()).thenReturn(mockEntityManagerFactory());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(mockSessionFactory());
        when(mockSessionFactory().getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(mockConnectionProvider());
        when(mockConnectionProvider().getDataSource()).thenReturn(mockDataSource());

        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    private SessionFactoryImpl mockSessionFactory() {
        return mock(SessionFactoryImpl.class);
    }

    private EntityManagerFactory mockEntityManagerFactory() {
        return mock(EntityManagerFactory.class);
    }

    private ConnectionProvider mockConnectionProvider() {
        return mock(ConnectionProvider.class);
    }

    private DataSource mockDataSource() {
        return mock(DataSource.class);
    }
}