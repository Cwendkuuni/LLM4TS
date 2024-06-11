package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        RoleRepository roleRepository = new RoleRepository();
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByName() {
        RoleRepository roleRepository = new RoleRepository();
        List<RoleEntity> roleEntities = roleRepository.findByName("Admin");
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        RoleRepository roleRepository = new RoleRepository();
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleRepository roleRepository = new RoleRepository();
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertNotNull(roleEntity);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleRepository roleRepository = new RoleRepository();
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, PageRequest.of(0, 10));
        assertNotNull(roleEntities);
    }

    // Add test cases for the remaining methods

}