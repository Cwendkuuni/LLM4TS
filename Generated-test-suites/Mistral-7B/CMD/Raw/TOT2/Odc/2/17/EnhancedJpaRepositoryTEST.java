import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    // Test EntityManager with no transactions
    @Test
    public void testGetEntityManagerNoTransactions() {
        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository(null, null);
        assertNull(repository.getEntityManager());
    }

    // Test EntityManager with a closed connection
    @Test(expected = IllegalStateException.class)
    public void testGetEntityManagerClosedConnection() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        when(entityManager.isOpen()).thenReturn(false);

        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository(null, entityManager);
        repository.getEntityManager();
    }

    // Test EntityManager with no session factory
    @Test(expected = IllegalStateException.class)
    public void testGetEntityManagerNoSessionFactory() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenThrow(new IllegalStateException());

        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository(null, entityManager);
        repository.getEntityManager();
    }

    // Test EntityManager with no data source
    @Test(expected = IllegalStateException.class)
    public void testGetEntityManagerNoDataSource() {
        SessionFactoryImpl sessionFactory = Mockito.mock(SessionFactoryImpl.class);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenThrow(new IllegalStateException());

        EntityManager entityManager = new EntityManagerImpl(sessionFactory);

        EnhancedJpaRepository<TestEntity, Long> repository = new EnhancedJpaRepository(null, entityManager);
        repository.getEntityManager();
    }
}