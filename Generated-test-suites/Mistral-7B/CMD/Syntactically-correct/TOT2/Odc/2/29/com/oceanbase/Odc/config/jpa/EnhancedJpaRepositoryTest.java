import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Test
    public void testGetEntityManager() {
        EnhancedJpaRepository<Object, Object> repository = Mockito.spy(new EnhancedJpaRepository<>(null, Mockito.mock(EntityManager.class)));
        EntityManager entityManagerMock = Mockito.mock(EntityManager.class);
        Mockito.when(repository.getEntityManager()).thenReturn(entityManagerMock);
        EntityManager actualEntityManager = repository.getEntityManager();
        Mockito.verify(repository).getEntityManager();
        Mockito.assertEquals(entityManagerMock, actualEntityManager);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityManagerNullEntityManager() {
        EnhancedJpaRepository<Object, Object> repository = new EnhancedJpaRepository<>(null, null);
        EntityManager actualEntityManager = repository.getEntityManager();
        Mockito.assertNull(actualEntityManager);
    }

    @Test(expected = RuntimeException.class)
    public void testGetEntityManagerNullEntityManagerFactory() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<Object, Object> repository = new EnhancedJpaRepository<>(null, entityManager);
        Mockito.assertThrows(RuntimeException.class, () -> repository.getEntityManager());
    }
}