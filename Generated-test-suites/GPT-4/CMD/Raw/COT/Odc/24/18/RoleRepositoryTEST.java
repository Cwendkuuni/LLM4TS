
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setup(){
       roleRepository = mock(RoleRepository.class); 
    }

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(1l, 2l, 3l));
        verify(roleRepository).findByIdIn(anyList());
    }

    @Test
    public void testFindByName() {
       roleRepository.findByName("Name");
       verify(roleRepository).findByName(anyString());
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(1l, PageRequest.of(1,1));
        verify(roleRepository).findByOrganizationId(anyLong(), any());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("Name", 1l);
        verify(roleRepository).findByNameAndOrganizationId(anyString(), anyLong());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(1l, Arrays.asList("Name1", "Name2"));
        verify(roleRepository).findByOrganizationIdAndNameIn(anyLong(), anyList());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(1l, Arrays.asList(1l, 2l), PageRequest.of(1,1));
        verify(roleRepository).findByOrganizationIdAndRoleIds(anyLong(), anyList(), any());
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1l);
        verify(roleRepository).findByUserId(anyLong());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
       roleRepository.findByUserIdAndEnabled(1l, true);
       verify(roleRepository).findByUserIdAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1l, 2l), true);
        verify(roleRepository).findByRoleIdsAndEnabled(anyList(), anyBoolean());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1l);
        roleRepository.update(roleEntity);
        verify(roleRepository).update(any());
    }

    @Test
    public void testNullValue() {
        roleRepository.findByName(null);
        verify(roleRepository).findByName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIdBuzz() {
        roleRepository.findByUserId(-1l);
    }
}
