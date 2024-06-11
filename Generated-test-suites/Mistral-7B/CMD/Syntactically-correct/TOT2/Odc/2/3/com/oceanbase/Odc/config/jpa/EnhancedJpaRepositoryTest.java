import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;

    @Test
    public void testConstructor() {
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(repo);
    }
}