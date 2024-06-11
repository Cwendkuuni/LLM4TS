import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation entityInformation;

    @InjectMocks
    private EnhancedJpaRepository enhancedJpaRepository;

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = enhancedJpaRepository.getEntityManager();
        assertNotNull("entityManager should not be null", entityManager);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull("jdbcTemplate should not be null", jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull("namedParameterJdbcTemplate should not be null", namedParameterJdbcTemplate);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSource_NullEntityManager() {
        when(entityManager.getEntityManagerFactory()).thenReturn(null);
        enhancedJpaRepository.getDataSource(entityManager);
    }

    @Test
    public void testGetDataSource_NotNullEntityManager() {
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull("dataSource should not be null", dataSource);
    }
}