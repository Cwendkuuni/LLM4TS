import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation entityInformation;

    private EnhancedJpaRepository<Object, Long> enhancedJpaRepository;

    @Before
    public void setUp() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
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

    @Test
    public void testGetDataSource() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    // Edge cases and exception handling scenarios could be added here

}