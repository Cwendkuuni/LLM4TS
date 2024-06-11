import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Before
    public void setup() {
        JpaEntityInformation<TestEntity, ?> entityInformation = mock(JpaEntityInformation.class);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
        enhancedJpaRepository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        
        assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        enhancedJpaRepository.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}