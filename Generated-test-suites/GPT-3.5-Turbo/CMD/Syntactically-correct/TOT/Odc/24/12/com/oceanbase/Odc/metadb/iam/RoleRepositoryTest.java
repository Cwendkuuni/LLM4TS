import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private List<RoleEntity> roles;

    @Before
    public void setUp() {
        RoleEntity role1 = new RoleEntity(1L, "Role 1", true, "Description 1");
        RoleEntity role2 = new RoleEntity(2L, "Role 2", false, "Description 2");
        roles = Arrays.asList(role1, role2);
    }

    @Test
    public void testFindByIdIn() {
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);
        List<RoleEntity> foundRoles = roleService.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(foundRoles);
        assertEquals(2, foundRoles.size());
        assertEquals("Role 1", foundRoles.get(0).getName());
        assertEquals("Role 2", foundRoles.get(1).getName());
    }

    @Test
    public void testFindByName() {
        Mockito.when(roleRepository.findByName("Role 1")).thenReturn(Arrays.asList(roles.get(0)));
        List<RoleEntity> foundRoles = roleService.findByName("Role 1");
        assertNotNull(foundRoles);
        assertEquals(1, foundRoles.size());
        assertEquals("Role 1", foundRoles.get(0).getName());
    }

    // Add more test cases for the remaining methods

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> page = new PageImpl<>(roles, PageRequest.of(0, 10, Sort.by("id")), roles.size());
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), PageRequest.of(0, 10, Sort.by("id"))))
                .thenReturn(page);
        Page<RoleEntity> foundRoles = roleService.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), PageRequest.of(0, 10, Sort.by("id")));
        assertNotNull(foundRoles);
        assertEquals(2, foundRoles.getTotalElements());
        assertEquals("Role 1", foundRoles.getContent().get(0).getName());
        assertEquals("Role 2", foundRoles.getContent().get(1).getName());
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRole = new RoleEntity(1L, "Updated Role", true, "Updated Description");
        Mockito.when(roleRepository.save(updatedRole)).thenReturn(updatedRole);
        RoleEntity savedRole = roleService.update(updatedRole);
        assertNotNull(savedRole);
        assertEquals("Updated Role", savedRole.getName());
        assertTrue(savedRole.isEnabled());
        assertEquals("Updated Description", savedRole.getDescription());
    }

}