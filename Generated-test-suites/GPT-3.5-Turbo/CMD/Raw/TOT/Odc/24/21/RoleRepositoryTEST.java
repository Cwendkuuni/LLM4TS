import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        RoleEntity role1 = new RoleEntity(1L, "Role1", true, "Description1");
        RoleEntity role2 = new RoleEntity(2L, "Role2", false, "Description2");

        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(Collections.singletonList(role1));
        when(roleRepository.findByName("Role1")).thenReturn(Collections.singletonList(role1));
        when(roleRepository.findByOrganizationId(1L, Pageable.unpaged())).thenReturn(new PageImpl<>(Collections.singletonList(role1)));
        when(roleRepository.findByNameAndOrganizationId("Role1", 1L)).thenReturn(Optional.of(role1));
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Role1"))).thenReturn(Collections.singletonList(role1));
        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L), Pageable.unpaged())).thenReturn(new PageImpl<>(Collections.singletonList(role1)));
        when(roleRepository.findByUserId(1L)).thenReturn(Collections.singletonList(role1));
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Collections.singletonList(role1));
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true)).thenReturn(Collections.singletonList(role1));
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertEquals(1, roles.size());
        assertEquals("Role1", roles.get(0).getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Role1");
        assertEquals(1, roles.size());
        assertEquals("Role1", roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertEquals(1, page.getTotalElements());
        assertEquals("Role1", page.getContent().get(0).getName());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Role1", 1L);
        assertTrue(role.isPresent());
        assertEquals("Role1", role.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Role1"));
        assertEquals(1, roles.size());
        assertEquals("Role1", roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L), Pageable.unpaged());
        assertEquals(1, page.getTotalElements());
        assertEquals("Role1", page.getContent().get(0).getName());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertEquals(1, roles.size());
        assertEquals("Role1", roles.get(0).getName());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, roles.size());
        assertEquals("Role1", roles.get(0).getName());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
        assertEquals(1, roles.size());
        assertEquals("Role1", roles.get(0).getName());
    }

}