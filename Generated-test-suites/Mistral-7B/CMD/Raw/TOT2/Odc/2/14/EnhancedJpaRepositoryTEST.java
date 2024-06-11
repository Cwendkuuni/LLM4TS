import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<SimpleJpaRepositoryTestEntity, Long> enhancedJpaRepository;

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = enhancedJpaRepository.getEntityManager();
        assertNotNull(entityManager);
    }
}