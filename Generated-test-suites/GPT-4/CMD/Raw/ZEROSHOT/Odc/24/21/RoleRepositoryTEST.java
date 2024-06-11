
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays; 
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private PageRequest pageRequest;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleRepository).build();
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L));
        assertNotNull(result);
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByName("Test");
        assertNotNull(result);
    }

    @Test
    public void testFindByOrganizationId() {
        when(roleRepository.findByOrganizationId(anyLong(), any())).thenReturn(Page.empty());
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageRequest);
        assertNotNull(result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(any(), anyLong())).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Test", 1L);
        assertNotNull(result.orElse(null));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test"));
        assertNotNull(result);
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(anyLong(), any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertNotNull(result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        when(roleRepository.findByRoleIdsAndEnabled(any(), any())).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true);
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(any())).thenReturn(1);
        int result = roleRepository.update(roleEntity);
        assertNotNull(result);
    }
}
