import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(new TestEntityInformation(), entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    // Edge case: when entity manager is null
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullEntityManager() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(new TestEntityInformation(), null);
        enhancedJpaRepository.getJdbcTemplate();
    }

    // Edge case: when accessing a method before initializing the entity manager
    @Test(expected = NullPointerException.class)
    public void testGetJdbcTemplateBeforeInitialization() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(new TestEntityInformation(), entityManager);
        enhancedJpaRepository.getJdbcTemplate();
    }

    // Exception handling: when EntityManagerFactory does not unwrap to SessionFactoryImpl
    @Test(expected = ClassCastException.class)
    public void testGetDataSourceWithInvalidEntityManagerFactory() {
        EntityManager entityManager = mock(EntityManager.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(new TestEntityInformation(), entityManager);
        enhancedJpaRepository.getJdbcTemplate();
    }

    // Exception handling: when unable to get DataSource from ConnectionProvider
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullConnectionProvider() {
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(null);
        EntityManager entityManager = mock(EntityManager.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(new TestEntityInformation(), entityManager);
        enhancedJpaRepository.getJdbcTemplate();
    }

}

class TestEntityInformation implements JpaEntityInformation<TestEntity, Long> {
    // Implement methods for test purpose
}

class TestEntity {
    // Implement test entity class for test purpose
}