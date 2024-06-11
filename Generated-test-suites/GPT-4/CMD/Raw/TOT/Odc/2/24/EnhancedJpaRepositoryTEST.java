import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private EntityManager entityManager;
    
    @Mock
    private EntityManagerFactory entityManagerFactory;
    
    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        repository = new EnhancedJpaRepository(entityInformation, entityManager);
    }
    
    @Test
    public void getEntityManagerTest() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void getJdbcTemplateTest() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplateObject = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplateObject.getJdbcOperations(), repository.getJdbcTemplate());
    }
    
    @Test
    public void getNamedParameterJdbcTemplateTest() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplateObject = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplateObject, repository.getNamedParameterJdbcTemplate());
    }
}