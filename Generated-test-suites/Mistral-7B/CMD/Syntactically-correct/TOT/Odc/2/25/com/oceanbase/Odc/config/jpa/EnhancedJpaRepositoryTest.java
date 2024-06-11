import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        JpaEntityInformation<SomeEntity, Serializable> entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<SomeEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);

        // Test the method
        EntityManager returnedEntityManager = repository.getEntityManager();

        // Assertions
        Mockito.verify(entityManager).getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
        Mockito.verify(entityManager, Mockito.atLeastOnce()).unwrap(SessionFactoryImpl.class);
        Mockito.assertEquals(entityManager, returnedEntityManager);
    }
}