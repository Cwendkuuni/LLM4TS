package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private RoleEntity validRole;
    private RoleEntity updatedRole;
    private RoleEntity invalidRole;
    private List<RoleEntity> roles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        validRole = new RoleEntity();
        validRole.setId(1L);
        validRole.setName("ROLE_ADMIN");
        validRole.setOrganizationId(1L);
        validRole.setEnabled(true);

        updatedRole = new RoleEntity();
        updatedRole.setId(1L);
        updatedRole.setName("ROLE_SUPER_ADMIN");
        updatedRole.setOrganizationId(1L);
        updatedRole.setEnabled(false);

        invalidRole = new RoleEntity();
        invalidRole.setId(null);
        invalidRole.setName(null);
        invalidRole.setOrganizationId(null);

        roles = new ArrayList<>();
        roles.add(validRole);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(validRole));
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());
        when(roleRepository.findAll()).thenReturn(roles);
        when(roleRepository.findByName(validRole.getName())).thenReturn(Collections.singletonList(validRole));
        when(roleRepository.findByName(invalidRole.getName())).thenReturn(Collections.emptyList());
        when(roleRepository.findByOrganizationId(1L, Pageable.unpaged())).thenReturn(new PageImpl<>(roles));
        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(Collections.singletonList(validRole));
        when(roleRepository.findByIdIn(Collections.singletonList(2L))).thenReturn(Collections.emptyList());
        when(roleRepository.findByNameAndOrganizationId(validRole.getName(), 1L)).thenReturn(Optional.of(validRole));
        when(roleRepository.findByNameAndOrganizationId(invalidRole.getName(), 1L)).thenReturn(Optional.empty());
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("ROLE_ADMIN"))).thenReturn(Collections.singletonList(validRole));
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("ROLE_INVALID"))).thenReturn(Collections.emptyList());
        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L))).thenReturn(new PageImpl<>(Collections.singletonList(validRole)));
        when(roleRepository.findByUserId(1L)).thenReturn(Collections.singletonList(validRole));
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Collections.singletonList(validRole));
        when(roleRepository.findByUserIdAndEnabled(1L, false)).thenReturn(Collections.emptyList());
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true)).thenReturn(Collections.singletonList(validRole));
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), false)).thenReturn(Collections.emptyList());
        when(roleRepository.save(validRole)).thenReturn(validRole);
        when(roleRepository.save(updatedRole)).thenReturn(updatedRole);
        when(roleRepository.save(invalidRole)).thenThrow(new RuntimeException("Invalid Role Entity"));
        when(roleRepository.deleteById(1L)).thenReturn(1);
        when(roleRepository.deleteById(2L)).thenReturn(0);
    }

    @Test
    public void testFindById() {
        RoleEntity actualRole = roleService.findById(1L);
        assertNotNull(actualRole);
        assertEquals(validRole.getName(), actualRole.getName());
    }

    @Test(expected = RoleRepositoryException.class)
    public void testFindById_InvalidId() {
        roleService.findById(2L);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> actualRoles = roleService.findByName("ROLE_ADMIN");
        assertNotNull(actualRoles);
        assertEquals(1, actualRoles.size());
        assertEquals(validRole.getName(), actualRoles.get(0).getName());
    }

    @Test
    public void testFindByName_InvalidName() {
        List<RoleEntity> actualRoles = roleService.findByName("ROLE_INVALID");
        assertNull(actualRoles);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> actualRoles = roleService.findByOrganizationId(1L, Pageable.unpaged());
        assertNotNull(actualRoles.getContent());
        assertEquals(1, actualRoles.getContent().size());
        assertEquals(validRole.getName(), actualRoles.getContent().get(0).getName());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> actualRole = roleService.findByNameAndOrganizationId("ROLE_ADMIN", 1L);
        assertNotNull(actualRole);
        assertTrue(actualRole.isPresent());
        assertEquals(validRole.getName(), actualRole.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> actualRoles = roleService.findByOrganizationIdAndNameIn(1L, Collections.singletonList("ROLE_ADMIN"));
        assertNotNull(actualRoles);
        assertEquals(1, actualRoles.size());
        assertEquals(validRole.getName(), actualRoles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> actualRoles = roleService.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L), Pageable.unpaged());
        assertNotNull(actualRoles.getContent());
        assertEquals(1, actualRoles.getContent().size());
        assertEquals(validRole.getName(), actualRoles.getContent().get(0).getName());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> actualRoles = roleService.findByUserId(1L);
        assertNotNull(actualRoles);
        assertEquals(1, actualRoles.size());
        assertEquals(validRole.getName(), actualRoles.get(0).getName());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> actualRoles = roleService.findByUserIdAndEnabled(1L, true);
        assertNotNull(actualRoles);
        assertEquals(1, actualRoles.size());
        assertEquals(validRole.getName(), actualRoles.get(0).getName());
    }

    @Test
    public void testFindByUserIdAndEnabled_Disabled() {
        List<RoleEntity> actualRoles = roleService.findByUserIdAndEnabled(1L, false);
        assertNull(actualRoles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> actualRoles = roleService.findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
        assertNotNull(actualRoles);
        assertEquals(1, actualRoles.size());
        assertEquals(validRole.getName(), actualRoles.get(0).getName());
    }

    @Test
    public void testFindByRoleIdsAndEnabled_Disabled() {
        List<RoleEntity> actualRoles = roleService.findByRoleIdsAndEnabled(Collections.singletonList(1L), false);
        assertNull(actualRoles);
    }

    @Test
    public void testSave() {
        RoleEntity savedRole = roleService.save(validRole);
        assertNotNull(savedRole);
        assertEquals(validRole.getName(), savedRole.getName());
    }

    @Test(expected = RoleRepositoryException.class)
    public void testSave_InvalidRoleEntity() {
        roleService.save(invalidRole);
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRole = roleService.update(updatedRole);
        assertNotNull(updatedRole);
        assertEquals(updatedRole.getName(), updatedRole.getName());
        assertFalse(updatedRole.isEnabled());
    }

    @Test(expected = RoleRepositoryException.class)
    public void testUpdate_InvalidRoleEntity() {
        roleService.update(invalidRole);
    }

    @Test
    public void testDeleteById() {
        int actualRowsAffected = roleService.deleteById(1L);
        assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testDeleteById_NotFound() {
        int actualRowsAffected = roleService.deleteById(2L);
        assertEquals(0, actualRowsAffected);
    }
}