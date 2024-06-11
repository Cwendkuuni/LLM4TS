//Test Class for RoleRepository

package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertNotNull(roles);
    }

    @Test
    public void testFindByName() {
        String name = "roleName";
        List<RoleEntity> roles = roleRepository.findByName(name);
        assertNotNull(roles);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, PageRequest.of(0,10));
        assertNotNull(roles);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "roleName";
        Long organizationId = 1L;
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNotNull(role);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names= Arrays.asList("role1", "role2");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId,names);
        assertNotNull(roles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds= Arrays.asList(1L, 2L);
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0,10));
        assertNotNull(roles);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        assertNotNull(roles);
    }
    
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId,enabled);
        assertNotNull(roles);
    }
    
    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds,enabled);
        assertNotNull(roles);
    }
    
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setEnabled(true);
        roleEntity.setId(1L);
        roleEntity.setName("Test");
        roleEntity.setDescription("Desc");
        int isUpdated = roleRepository.update(roleEntity);
        assertTrue(isUpdated>0);
    }
}