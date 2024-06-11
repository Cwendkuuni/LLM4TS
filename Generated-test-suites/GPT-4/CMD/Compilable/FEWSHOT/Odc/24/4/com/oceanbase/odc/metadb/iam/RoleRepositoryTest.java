
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    private RoleRepository roleRepository; // Initialize this via dependency injection or in set up method

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> entities = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(entities);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> entities = roleRepository.findByName("TestRole");
        assertNotNull(entities);
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> entitiesPage = roleRepository.findByOrganizationId(1L, pageable);
        assertNotNull(entitiesPage);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> entityOpt = roleRepository.findByNameAndOrganizationId("TestRole", 1L);
        assertTrue(entityOpt.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> entities = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("TestRole1", "TestRole2"));
        assertNotNull(entities);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> entitiesPage = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), pageable);
        assertNotNull(entitiesPage);
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> entities = roleRepository.findByUserId(1L);
        assertNotNull(entities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> entities = roleRepository.findByUserIdAndEnabled(1L, true);
        assertNotNull(entities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> entities = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertNotNull(entities);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(); // Populate roleEntity with sample values
        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(1, updatedRows);
    }
}
