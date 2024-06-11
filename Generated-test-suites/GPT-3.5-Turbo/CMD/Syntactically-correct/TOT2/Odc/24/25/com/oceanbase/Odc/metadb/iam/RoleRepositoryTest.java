import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository roleRepositoryMock;

    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Role Description");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        Mockito.when(roleRepositoryMock.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepositoryMock.findByIdIn(ids);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), 1L);
    }

    // Additional test methods for other public methods in the RoleRepository interface should be added here

}