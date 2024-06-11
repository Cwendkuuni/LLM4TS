package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        RoleRepository roleRepository = new RoleRepository();
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(roles);
    }

    @Test
    public void testFindByName() {
        RoleRepository roleRepository = new RoleRepository();
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
    }

    @Test
    public void testFindByOrganizationId() {
        RoleRepository roleRepository = new RoleRepository();
        Pageable pageable = Pageable.unpaged();
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);
        assertNotNull(roles);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleRepository roleRepository = new RoleRepository();
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertNotNull(role);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleRepository roleRepository = new RoleRepository();
        Pageable pageable = Pageable.unpaged();
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), pageable);
        assertNotNull(roles);
    }

    @Test
    public void testFindByUserId() {
        RoleRepository roleRepository = new RoleRepository();
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertNotNull(roles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        RoleRepository roleRepository = new RoleRepository();
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertNotNull(roles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        RoleRepository roleRepository = new RoleRepository();
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertNotNull(roles);
    }

    @Test
    public void testUpdate() {
        RoleRepository roleRepository = new RoleRepository();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Role for Admin");
        
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}