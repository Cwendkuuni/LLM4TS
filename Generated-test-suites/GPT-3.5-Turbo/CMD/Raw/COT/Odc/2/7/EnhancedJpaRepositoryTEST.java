
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EnhancedJpaRepositoryTest {

    private EntityManager entityManager;
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        JpaEntityInformation<TestEntity, Long> entityInformation = Mockito.mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        Assert.assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        Assert.assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        Assert.assertNotNull(namedParameterJdbcTemplate);
    }

    // Edge cases and exception handling scenarios that should be tested:
    // 1. entityManager is null
    // 2. ConnectionProvider can't be unwrapped from EntityManager
    // Add more if needed

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullEntityManager() {
        EnhancedJpaRepository<TestEntity, Long> repo = new EnhancedJpaRepository<>(entityInformation, null);
    }

    // Additional edge cases and exception handling scenarios tests can be added here

}
