import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    private JpaEntityInformation entityInformation;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
    }

    @Test
    public void testConstructor() {
        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(enhancedJpaRepository);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullEntityInformation() {
        new EnhancedJpaRepository(null, entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullEntityManager() {
        new EnhancedJpaRepository(entityInformation, null);
    }

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertNotNull(enhancedJpaRepository.getEntityManager());
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertTrue(enhancedJpaRepository.getJdbcTemplate() instanceof JdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EnhancedJpaRepository enhancedJpaRepository = new EnhancedJpaRepository(entityInformation, entityManager);
        assertTrue(enhancedJpaRepository.getNamedParameterJdbcTemplate() instanceof NamedParameterJdbcTemplate);
    }
}