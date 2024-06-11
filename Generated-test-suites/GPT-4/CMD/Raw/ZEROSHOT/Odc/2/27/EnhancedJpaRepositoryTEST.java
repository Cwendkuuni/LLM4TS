
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import javax.sql.DataSource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;
    private EntityManager entityManager;
    private JpaEntityInformation<Object, Serializable> entityInformation;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        enhancedJpaRepository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);
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
        assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(factory);
        assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceException() {
        EntityManager entityManagerException = mock(EntityManager.class);
        when(entityManagerException.getEntityManagerFactory()).thenReturn(null);
        assertNull(enhancedJpaRepository.getDataSource(entityManagerException));
    }
}
