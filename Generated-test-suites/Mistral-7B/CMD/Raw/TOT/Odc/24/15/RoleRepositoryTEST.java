package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity defaultRole;
    private RoleEntity existingRole;
    private RoleEntity nonExistingRole;

    @Before
    public void setup() {
        defaultRole = new RoleEntity();
        defaultRole.setName("defaultRole");
        defaultRole.setOrganizationId(1L);
        defaultRole.setDescription("Default role");
        defaultRole.setEnabled(true);
        roleRepository.save(defaultRole);

        existingRole = new RoleEntity();
        existingRole.setName("existingRole");
        existingRole.setOrganizationId(1L);
        existingRole.setDescription("Existing role");
        existingRole.setEnabled(true);
        roleRepository.save(existingRole);

        nonExistingRole = new RoleEntity();
        nonExistingRole.setName("nonExistingRole");
        nonExistingRole.setOrganizationId(1L);
        nonExistingRole.setDescription("Non-existing role");
        nonExistingRole.setEnabled(true);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(defaultRole.getId(), existingRole.getId());
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        Assert.assertEquals(2, roles.size());
        Assert.assertContains(defaultRole, roles);
        Assert.assertContains(existingRole, roles);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("defaultRole");
        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(defaultRole, roles.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = new PageRequest(0, 10);
        Page<RoleEntity> pages = roleRepository.findByOrganizationId(organizationId, pageable);
        Assert.assertTrue(pages.getTotalElements() > 0);
        Assert.assertFalse(pages.isEmpty());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        List<RoleEntity> roles = roleRepository.findByNameAndOrganizationId("defaultRole", 1L);
        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(defaultRole, roles.get(0));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("defaultRole", "existingRole");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        Assert.assertEquals(2, roles.size());
        Assert.assertContains(defaultRole, roles);
        Assert.assertContains(existingRole, roles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(defaultRole.getId(), existingRole.getId());
        Pageable pageable = new PageRequest(0, 10);
        Page<RoleEntity> pages = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        Assert.assertTrue(pages.getTotalElements() > 0);
        Assert.assertFalse(pages.isEmpty());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        Assert.assertEquals(2, roles.size());
        Assert.assertContains(defaultRole, roles);
        Assert.assertContains(existingRole, roles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, true);
        Assert.assertEquals(2, roles.size());
        Assert.assertContains(defaultRole, roles);
        Assert.assertContains(existingRole, roles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(defaultRole.getId(), existingRole.getId());
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        Assert.assertEquals(2, roles.size());
        Assert.assertContains(defaultRole, roles);
        Assert.assertContains(existingRole, roles);
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRole = new RoleEntity();
        updatedRole.setName("updatedRole");
        updatedRole.setOrganizationId(1L);
        updatedRole.setDescription("Updated role");
        updatedRole.setEnabled(false);

        int countBefore = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(defaultRole.getId()), true).size();
        roleRepository.update(updatedRole);

        RoleEntity roleInDB = roleRepository.findById(defaultRole.getId()).get();
        Assert.assertEquals("updatedRole", roleInDB.getName());
        Assert.assertFalse(roleInDB.isEnabled());

        int countAfter = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(defaultRole.getId()), true).size();
        Assert.assertEquals(countBefore - 1, countAfter);
    }
}