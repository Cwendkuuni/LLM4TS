import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private RoleEntity roleEntity1;
    @MockBean
    private RoleEntity roleEntity2;

    @Test
    public void testFindByIdIn() {
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        Assertions.assertEquals(Arrays.asList(roleEntity1, roleEntity2), roles);
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("TestRole");
        Mockito.when(roleRepository.findByName("TestRole"))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity role = roleRepository.findByName("TestRole").get();

        Assertions.assertEquals("TestRole", role.getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Long organizationId = 1L;
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(roleEntity1, roleEntity2), pageable, 2);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable))
                .thenReturn(page);

        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, pageable);

        Assertions.assertEquals(2, roles.getSize());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setOrganizationId(1L);
        roleEntity.setName("TestRole");

        Mockito.when(roleRepository.findByNameAndOrganizationId("TestRole", 1L))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity role = roleRepository.findByNameAndOrganizationId("TestRole", 1L).get();

        Assertions.assertEquals("TestRole", role.getName());
        Assertions.assertEquals(1L, role.getOrganizationId());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = Arrays.asList(roleEntity1, roleEntity2);

        Mockito.when(roleRepository.findByUserId(1L))
                .thenReturn(roles);

        Mockito.when(roleRepository.findByUserIdAndEnabled(1L, true))
                .thenReturn(Arrays.asList(roleEntity1));

        RoleEntity role = roleRepository.findByUserIdAndEnabled(1L, true).get();

        Assertions.assertNotNull(role);
        Assertions.assertTrue(role.isEnabled());
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here.

    @Test
    public void testUpdate() {
        RoleEntity newRoleEntity = new RoleEntity();
        newRoleEntity.setName("newRole");

        Mockito.when(roleRepository.update(newRoleEntity))
                .thenReturn(1);

        int numRowsAffected = roleRepository.update(newRoleEntity);

        Assertions.assertEquals(1, numRowsAffected);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(roleEntity1, roleEntity2), PageRequest.of(0, 2), 2);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 2)))
                .thenReturn(page);

        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 2));

        Assertions.assertEquals(2, roles.getSize());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roles = Arrays.asList(roleEntity1, roleEntity2);

        Mockito.when(roleRepository.findByUserId(userId))
                .thenReturn(roles);

        List<RoleEntity> roleList = roleRepository.findByUserId(userId);

        Assertions.assertEquals(2, roleList.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        List<RoleEntity> roles = Arrays.asList(roleEntity1, roleEntity2);

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, true))
                .thenReturn(Arrays.asList(roleEntity1));

        List<RoleEntity> roleList = roleRepository.findByUserIdAndEnabled(userId, true);

        Assertions.assertEquals(1, roleList.size());
    }
}