package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertNotNull(roles);
        assertEquals(3, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(role.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, PageRequest.of(0, 10));
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(1L, "Admin", true, "Admin Role");
        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(1, updatedRows);
    }
}