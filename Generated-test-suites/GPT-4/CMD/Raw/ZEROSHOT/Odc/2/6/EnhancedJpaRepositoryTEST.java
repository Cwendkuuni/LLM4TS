
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    private EntityManager em;
    private EnhancedJpaRepository enhancedJpaRepository;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() {
        em = Mockito.mock(EntityManager.class);
        namedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        enhancedJpaRepository = new EnhancedJpaRepository(null, em);
    }

    @Test
    public void getEntityManagerTest() {
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertNotNull(result);
    }

    @Test
    public void getJdbcTemplateTest() {
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(Mockito.mock(JdbcTemplate.class));
        JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void getNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void getDataSourceTest() {        
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        when(em.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        assertNotNull(enhancedJpaRepository.getDataSource(em));        
    }
}
