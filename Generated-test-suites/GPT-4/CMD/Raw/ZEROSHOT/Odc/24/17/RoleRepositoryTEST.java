
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private Collection<Long> ids;
    private List<String> name;
    private Pageable pageable;
    private RoleEntity roleEntity;
 
    @Before
    public void setUp() {
        ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        name = new ArrayList<>();
        name.add("Admin");
        name.add("User");

        pageable = PageRequest.of(0, 1);
        roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(ids);
        verify(roleRepository, times(1)).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("Admin");
        verify(roleRepository, times(1)).findByName("Admin");
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(1L, pageable);
        verify(roleRepository, times(1)).findByOrganizationId(1L, pageable);
    }
    
    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("Admin", 1L);
        verify(roleRepository, times(1)).findByNameAndOrganizationId("Admin", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(1L, name);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(1L, name);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(1L, ids, pageable);
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(1L, ids, pageable);
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1L);
        verify(roleRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(1L, true);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(ids, true);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(ids, true);
    }

    @Test
    public void testUpdate() {
        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}
