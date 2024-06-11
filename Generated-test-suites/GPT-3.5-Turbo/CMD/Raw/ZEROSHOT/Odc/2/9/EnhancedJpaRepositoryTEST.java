import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.Serializable;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<TestEntity, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    private EnhancedJpaRepository<TestEntity, Serializable> enhancedJpaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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

}