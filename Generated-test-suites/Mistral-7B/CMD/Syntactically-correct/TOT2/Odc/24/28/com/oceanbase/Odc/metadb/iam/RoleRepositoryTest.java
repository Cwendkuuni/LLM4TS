package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for findByIdIn method
    @Test
    public void testFindByIdIn() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByIdIn(Arrays.asList(role1.getId(), role2.getId()))).thenReturn(Arrays.asList(role1, role2));
        List<RoleEntity> roles = roleService.findRolesByIds(Arrays.asList(role1.getId(), role2.getId()));
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    // Test for findByName method
    @Test
    public void testFindByName() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.findByName("testName")).thenReturn(Collections.singletonList(role));
        RoleEntity foundRole = roleService.findRoleByName("testName");
        assertEquals(role, foundRole);
    }

    // Test for findByOrganizationId method with empty List
    @Test
    public void testFindByOrganizationIdEmpty() {
        when(roleRepository.findByOrganizationId(1L, null)).thenReturn(Collections.emptyList());
        List<RoleEntity> roles = roleService.findRolesByOrganizationId(1L, null);
        assertTrue(roles.isEmpty());
    }

    // Test for findByOrganizationId method with List of Roles
    @Test
    public void testFindByOrganizationId() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByOrganizationId(1L, null)).thenReturn(Arrays.asList(role1, role2));
        List<RoleEntity> roles = roleService.findRolesByOrganizationId(1L, null);
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    // Test for findByNameAndOrganizationId method with null name
    @Test(expected = NullPointerException.class)
    public void testFindByNameAndOrganizationIdWithNullName() {
        when(roleRepository.findByNameAndOrganizationId(null, 1L)).thenReturn(Optional.empty());
        roleService.findRoleByNameAndOrganizationId(null, 1L);
    }

    // Test for findByNameAndOrganizationId method with existing Role
    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId("testName", 1L)).thenReturn(Optional.of(role));
        RoleEntity foundRole = roleService.findRoleByNameAndOrganizationId("testName", 1L);
        assertEquals(role, foundRole);
    }

    // Test for findByOrganizationIdAndNameIn method with empty List
    @Test
    public void testFindByOrganizationIdAndNameInEmpty() {
        List<String> names = Collections.emptyList();
        when(roleRepository.findByOrganizationIdAndNameIn(1L, names)).thenReturn(Collections.emptyList());
        List<RoleEntity> roles = roleService.findRolesByOrganizationIdAndNames(1L, names);
        assertTrue(roles.isEmpty());
    }

    // Test for findByOrganizationIdAndNameIn method with List of Roles
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testName1", "testName2")))
                .thenReturn(Arrays.asList(role1, role2));
        List<RoleEntity> roles = roleService.findRolesByOrganizationIdAndNames(1L, Arrays.asList("testName1", "testName2"));
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    // Test for findByOrganizationIdAndRoleIds method with empty Pageable
    @Test
    public void testFindByOrganizationIdAndRoleIdsEmpty() {
        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.emptyList(), null)).thenReturn(Collections.emptyList());
        List<RoleEntity> roles = roleService.findRolesByOrganizationIdAndRoleIds(1L, Collections.emptyList(), null);
        assertTrue(roles.isEmpty());
    }

    // Test for findByOrganizationIdAndRoleIds method with Pageable and Roles
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(role1.getId(), role2.getId()), null))
                .thenReturn(Arrays.asList(role1, role2));
        Page<RoleEntity> roles = roleService.findRolesByOrganizationIdAndRoleIds(1L, Arrays.asList(role1.getId(), role2.getId()), null);
        assertEquals(2, roles.getContent().size());
        assertEquals(role1, roles.getContent().get(0));
        assertEquals(role2, roles.getContent().get(1));
        assertEquals(1, roles.getTotalElements());
    }

    // Test for findByUserId method with null userId
    @Test(expected = NullPointerException.class)
    public void testFindByUserIdWithNullUserId() {
        when(roleRepository.findByUserId(null)).thenReturn(Collections.emptyList());
        roleService.findRolesByUserId(null);
    }

    // Test for findByUserId method with existing Roles
    @Test
    public void testFindByUserId() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByUserId(1L)).thenReturn(Arrays.asList(role1, role2));
        List<RoleEntity> roles = roleService.findRolesByUserId(1L);
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    // Test for findByUserIdAndEnabled method with false enabled
    @Test
    public void testFindByUserIdAndEnabledFalse() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByUserIdAndEnabled(1L, false)).thenReturn(Collections.emptyList());
        List<RoleEntity> roles = roleService.findRolesByUserIdAndEnabled(1L, false);
        assertTrue(roles.isEmpty());
    }

    // Test for findByUserIdAndEnabled method with true enabled
    @Test
    public void testFindByUserIdAndEnabledTrue() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Arrays.asList(role1, role2));
        List<RoleEntity> roles = roleService.findRolesByUserIdAndEnabled(1L, true);
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    // Test for findByRoleIdsAndEnabled method with false enabled
    @Test
    public void testFindByRoleIdsAndEnabledFalse() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, false)).thenReturn(Collections.emptyList());
        List<RoleEntity> roles = roleService.findRolesByRoleIdsAndEnabled(roleIds, false);
        assertTrue(roles.isEmpty());
    }

    // Test for findByRoleIdsAndEnabled method with true enabled
    @Test
    public void testFindByRoleIdsAndEnabledTrue() {
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(Arrays.asList(role1, role2));
        List<RoleEntity> roles = roleService.findRolesByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    // Test for update method with null RoleEntity
    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullRoleEntity() {
        roleService.updateRole(null);
    }

    // Test for update method with existing Role
    @Test
    public void testUpdate() {
        RoleEntity role = new RoleEntity();
        when(roleRepository.save(any(RoleEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        RoleEntity updatedRole = roleService.updateRole(role);
        assertEquals(role, updatedRole);
    }
}