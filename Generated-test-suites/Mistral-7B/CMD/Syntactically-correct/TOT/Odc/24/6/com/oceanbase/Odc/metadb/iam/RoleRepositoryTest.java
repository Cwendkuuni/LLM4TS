package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity role1, role2, role3;

    @Before
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
        role1 = new RoleEntity(1L, "role1", true);
        role2 = new RoleEntity(2L, "role2", true);
        role3 = new RoleEntity(3L, "role3", false);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByIdIn(ids);

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    @Test
    public void testFindByName() {
        Mockito.when(roleRepository.findByName("role1")).thenReturn(Collections.singletonList(role1));

        RoleEntity role = roleRepository.findByName("role1").orElse(null);

        assertNotNull(role);
        assertEquals("role1", role.getName());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any()))
                .thenReturn(Page.empty());

        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                Mockito.mock(Pageable.class));

        assertTrue(roles.isEmpty());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIdsWithData() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = Arrays.asList(role1, role2);
        Pageable pageable = Mockito.mock(Pageable.class);
        Mockito.when(roleRepository.countQueryMethod(Mockito.eq(organizationId), Mockito.eq(roleIds)))
                .thenReturn((long) roles.size());
        Mockito.when(roleRepository.findQueryMethod(Mockito.eq(organizationId), Mockito.eq(roleIds), Mockito.eq(pageable)))
                .thenReturn(Page.of(roles, pageable));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(roles.size(), result.getTotalElements());
        assertEquals(roles, result.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndRoleIdsWithNullRoleIds() {
        Long organizationId = 1L;
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, null))
                .thenCallRealMethod();

        roleRepository.findByOrganizationIdAndRoleIds(organizationId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndRoleIdsWithEmptyRoleIds() {
        Long organizationId = 1L;
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, Collections.emptyList()))
                .thenCallRealMethod();

        roleRepository.findByOrganizationIdAndRoleIds(organizationId, Collections.emptyList());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(Arrays.asList(role1, role2));

        List<RoleEntity> roles = roleRepository.findByUserId(userId);

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.singletonList(role1));

        RoleEntity role = roleRepository.findByUserIdAndEnabled(userId, enabled).orElse(null);

        assertNotNull(role);
        assertEquals("role1", role.getName());
        assertTrue(role.isEnabled());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;

        List<RoleEntity> roles = Arrays.asList(role1, role2);

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(roles.size(), result.size());
        assertTrue(result.containsAll(roles));
    }

    @Test
    public void testUpdate() {
        RoleEntity newRole = new RoleEntity(4L, "newRole", true);

        Mockito.when(roleRepository.update(newRole)).thenReturn(1);

        int result = roleRepository.update(newRole);

        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullRoleEntity() {
        RoleEntity roleEntity = null;

        Mockito.when(roleRepository.update(roleEntity)).thenCallRealMethod();

        roleRepository.update(roleEntity);
    }
}