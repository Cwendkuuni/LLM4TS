
package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@EnableJpaRepositories
public class RoleRepositoryTest {
    
    RoleRepository roleRepository; // initialize this with your preferred method

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        // Add assertions as per your requirements
    }

    @Test
    public void testFindByName() {
        String name = "ROLE_NAME";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        // Add assertions as per your requirement
    }

    @Test
    public void testFindByOrganizationId() {
        Long orgId = 123L;
        Pageable pageable = PageRequest.of(0, 10);
        // WhatsApp backup
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(orgId, pageable);
        // Add assertions as per your requirement
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "ROLE_NAME";
        Long orgId = 123L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, orgId);
        // Add assertions as per your requirement
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long orgId = 123L;
        List<String> names = Arrays.asList("ROLE_NAME_1", "ROLE_NAME_2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(orgId, names);
        // Add assertions as per your requirement
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long orgId = 123L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 2);

        Page<RoleEntity> roleEntitiesPage = roleRepository.findByOrganizationIdAndRoleIds(orgId, roleIds, pageable);
        // Add assertions as per your requirements.
    }

    @Test
    public void testFindByUserId() {
        Long userId = 123L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        // Add assertions as per your requirements.
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 123L;
        Boolean enabled = true;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        // Add assertions as per your requirements.
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity>  roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        // Add assertions as per your requirements.
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE_NAME");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("DESCRIPTION");

        int updateCount = roleRepository.update(roleEntity);
        assertEquals(1, updateCount);
    }
}
