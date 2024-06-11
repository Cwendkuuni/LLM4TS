import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnhancedJpaRepositoryTest{

    private EntityManager entityManager;
    private JpaEntityInformation entityInformation;
    private EnhancedJpaRepository repository;

    @Before
    public void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void repositoryTest_NullEntityInformation() {
        new EnhancedJpaRepository(null, entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void repositoryTest_NullEntityManager() {
        new EnhancedJpaRepository(entityInformation, null);
    }

    @Test(expected = NullPointerException.class)
    public void getDataSourceTest_NullEntityManager() {
        repository.getDataSource(null);
    }

    @Test
    public void getEntityManagerTest() {
        assertEquals(repository.getEntityManager(), entityManager);
    }

    @Test
    public void getJdbcTemplateTest() {
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void getNamedParameterJdbcTemplateTest() {
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }
}