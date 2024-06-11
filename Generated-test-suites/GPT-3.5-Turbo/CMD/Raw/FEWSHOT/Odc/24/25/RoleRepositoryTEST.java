package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByIdIn(Collections.singletonList(1L)));
    }

    @Test
    public void testFindByName() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByName("Admin"));
    }

    @Test
    public void testFindByOrganizationId() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10)));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByNameAndOrganizationId("Admin", 1L));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Admin", "User")));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), PageRequest.of(0, 10)));
    }

    @Test
    public void testFindByUserId() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByUserId(1L));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByUserIdAndEnabled(1L, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        RoleRepository roleRepository = new RoleRepository();
        assertNotNull(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true));
    }

    @Test
    public void testUpdate() {
        RoleRepository roleRepository = new RoleRepository();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Admin Role");
        assertNotNull(roleRepository.update(roleEntity));
    }
}