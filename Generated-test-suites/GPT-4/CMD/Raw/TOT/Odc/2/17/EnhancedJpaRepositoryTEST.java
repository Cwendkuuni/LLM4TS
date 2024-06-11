import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class EnhancedJpaRepositoryTest {
    
    private EnhancedJpaRepository mockRepo;
    private EntityManager em;
    private JpaEntityInformation entityInformation;

    @Before
    public void setUp() throws Exception {
        em = mock(EntityManager.class);
        entityInformation = mock(JpaEntityInformation.class);
        mockRepo = new EnhancedJpaRepository(entityInformation, em);
    }

    @Test
    public void testGetEntityManager() {
        Assert.assertEquals(em, mockRepo.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        Assert.assertNotNull(mockRepo.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        Assert.assertNotNull(mockRepo.getNamedParameterJdbcTemplate());
    }
}