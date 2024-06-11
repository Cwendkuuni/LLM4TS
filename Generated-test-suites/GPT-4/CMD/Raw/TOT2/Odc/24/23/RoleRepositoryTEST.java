import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class RoleRepositoryTest {
    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private TestEntityManager entityManager;

    @Test
    public void findByIdIn_Should_Return_Roles() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        RoleEntity role3 = new RoleEntity();
        role3.setId(3L);

        when(entityManager.find(RoleEntity.class, 1L)).thenReturn(role1);
        when(entityManager.find(RoleEntity.class, 2L)).thenReturn(role2);
        when(entityManager.find(RoleEntity.class, 3L)).thenReturn(role3);

        // Act
        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        // Assert
        verify(entityManager, times(1)).find(RoleEntity.class, 1L);
        verify(entityManager, times(1)).find(RoleEntity.class, 2L);
        verify(entityManager, times(1)).find(RoleEntity.class, 3L);
        
        assertEquals(ids.size(), result.size());
        assertEquals(role1, result.get(0));
        assertEquals(role2, result.get(1));
        assertEquals(role3, result.get(2));
    }
}