import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<Entity, Long> enhancedJpaRepository;

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = enhancedJpaRepository.getEntityManager();
        Assertions.assertNotNull(entityManager);
    }

    @Test
    public void testGetEntityManagerWithNullEntityManager() {
        Assertions.assertThrows(NullPointerException.class, () -> enhancedJpaRepository.getEntityManager(null));
    }
}