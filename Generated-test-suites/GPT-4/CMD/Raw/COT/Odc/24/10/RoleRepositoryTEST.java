import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class RoleRepositoryTest {

    @InjectMocks
    RoleRepository roleRepository;

    @Mock
    RoleEntity roleEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(anyList());
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName(anyString());
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(anyLong(), any());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId(anyString(), anyLong());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(anyLong(), anyList(), any());
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(anyLong());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(anyList(), anyBoolean());
    }

    @Test
    public void testUpdate() {
        roleRepository.update(roleEntity);
    }
}