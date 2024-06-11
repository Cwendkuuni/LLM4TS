import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertNotNull;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    private EnhancedJpaRepository<Object, Serializable> repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(repo.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repo.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repo.getNamedParameterJdbcTemplate());
    }
}