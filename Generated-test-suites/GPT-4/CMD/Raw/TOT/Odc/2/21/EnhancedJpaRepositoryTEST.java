import mockit.Mocked;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;

public class EnhancedJpaRepositoryTest {

    @Tested
    EnhancedJpaRepository<Object, String> repository;

    @Injectable
    EntityManager entityManager;

    @Injectable
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mocked
    JpaEntityInformation<Object, String> entityInformation;

    @Before
    public void setUp(){
        new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager(){
        EntityManager actualEntityManager = repository.getEntityManager();
        assertNotNull(actualEntityManager);
    }

    @Test
    public void testGetJdbcTemplate(){
        JdbcTemplate jdbcTemplate  = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate(){
        NamedParameterJdbcTemplate template = repository.getNamedParameterJdbcTemplate();
        assertNotNull(template);
    }
}