import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    private EnhancedJpaRepository enhancedJpaRepository;

    @Before
    public void setUp() {
        enhancedJpaRepository = new EnhancedJpaRepository(null, entityManager);
    }

    @Test
    public void testConstructorWithNullEntityInformation() {
        assertNotNull(enhancedJpaRepository);
    }

    @Test(expected = Exception.class)
    public void testConstructorWithNullException() {
        new EnhancedJpaRepository(null, null);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test(expected = ClassCastException.class)
    public void testGetJdbcTemplate() {
        enhancedJpaRepository.getJdbcTemplate();
    }

    @Test
    public void tetGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}