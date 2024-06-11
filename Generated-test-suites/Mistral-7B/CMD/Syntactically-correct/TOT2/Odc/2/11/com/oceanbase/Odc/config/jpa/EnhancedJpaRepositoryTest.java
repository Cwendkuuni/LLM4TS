import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<SomeEntity, Serializable> enhancedJpaRepository;

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityManagerWithNullEntityManager() {
        EnhancedJpaRepository<SomeEntity, Serializable> emptyRepository = new EnhancedJpaRepository<>(null, null);
        assertNull(emptyRepository.getEntityManager());
    }
}