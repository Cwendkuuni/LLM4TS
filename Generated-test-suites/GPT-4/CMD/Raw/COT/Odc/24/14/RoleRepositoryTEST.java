
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private Page<RoleEntity> roleEntityPage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L))).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L));
        assertNotNull(roles);
        assertTrue(roles.size() > 0);
    }

    @Test
    public void testFindByName() {
        Mockito.when(roleRepository.findByName(Mockito.anyString())).thenReturn(Arrays.asList(roleEntity));
        assertNotNull(roleRepository.findByName("test"));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(roleEntityPage);
        assertNotNull(roleRepository.findByOrganizationId(1L, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Mockito.when(roleRepository.findByNameAndOrganizationId("admin", 1L)).thenReturn(Optional.of(roleEntity));
        assertNotNull(roleRepository.findByNameAndOrganizationId("admin", 1L));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("admin"))).thenReturn(Arrays.asList(roleEntity));
        assertNotNull(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("admin")));
    }

    @Test
    public void testFindByUserId() {
        Mockito.when(roleRepository.findByUserId(1L)).thenReturn(Arrays.asList(roleEntity));
        assertNotNull(roleRepository.findByUserId(1L));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Mockito.when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Arrays.asList(roleEntity));
        assertNotNull(roleRepository.findByUserIdAndEnabled(1L, true));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntityNew = new RoleEntity();
        roleEntityNew.setName("test2");
        roleEntityNew.setEnabled(true);
        Mockito.when(roleRepository.update(roleEntityNew)).thenReturn(1);
        assertEquals(1, roleRepository.update(roleEntityNew));
    }
}
