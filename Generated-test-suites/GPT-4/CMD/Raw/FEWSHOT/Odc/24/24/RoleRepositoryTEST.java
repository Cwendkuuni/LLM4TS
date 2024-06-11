import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class RoleRepositoryTest {
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = new RoleRepositoryImpl(); // Assuming you have an implementation class.
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = roleRepository.findByIdIn(roleIds);
        assertFalse(roles.isEmpty());
        assertEquals(roleIds.size(), roles.size());
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> roles = roleRepository.findByName(name);
        assertFalse(roles.isEmpty());
        assertTrue(roles.stream().allMatch(role -> role.getName().equals(name)));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, pageable);
        assertFalse(roles.isEmpty());
        assertTrue(roles.stream().allMatch(role -> role.getOrganizationId().equals(organizationId)));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "admin";
        Long organizationId = 1L;
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(role.isPresent());
        assertEquals(name, role.get().getName());
        assertEquals(organizationId, role.get().getOrganizationId());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> roleNames = new ArrayList<>();
        roleNames.add("admin");
        roleNames.add("user");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, roleNames);
        assertFalse(roles.isEmpty());
        assertTrue(roles.stream().anyMatch(role -> role.getOrganizationId().equals(organizationId) && roleNames.contains(role.getName())));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertFalse(roles.isEmpty());
        assertTrue(roles.stream().allMatch(role -> role.getEnabled().equals(enabled)));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertFalse(roles.isEmpty());
        assertTrue(roles.stream().allMatch(role -> role.getEnabled().equals(enabled) && roleIds.contains(role.getId())));
    }

    @Test
    public void testUpdate() {
        RoleEntity newRoleEntity = new RoleEntity();
        newRoleEntity.setName("Test");
        newRoleEntity.setEnabled(true);
        int result = roleRepository.update(newRoleEntity);
        assertTrue(result > 0);
    }
}