package com.oceanbase.odc.metadb.iam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        Assert.assertNotNull(roleEntities);
        Assert.assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        Assert.assertNull(roleEntities);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleRepository.save(roleEntity);

        roleEntities = roleRepository.findByName(name);
        Assert.assertNotNull(roleEntities);
        Assert.assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
        Assert.assertNotNull(page);
        Assert.assertFalse(page.isEmpty());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        Optional<RoleEntity> roleOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
        Assert.assertFalse(roleOptional.isPresent());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        roleRepository.save(roleEntity);

        roleOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
        Assert.assertTrue(roleOptional.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testRole1");
        names.add("testRole2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        Assert.assertNull(roleEntities);

        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName("testRole1");
        roleEntity1.setOrganizationId(organizationId);
        roleRepository.save(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setName("testRole2");
        roleEntity2.setOrganizationId(organizationId);
        roleRepository.save(roleEntity2);

        roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        Assert.assertNotNull(roleEntities);
        Assert.assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        Assert.assertNotNull(page);
        Assert.assertFalse(page.isEmpty());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        Assert.assertNull(roleEntities);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setUserId(userId);
        roleEntity.setRoleId(1L);
        roleRepository.save(roleEntity);

        roleEntities = roleRepository.findByUserId(userId);
        Assert.assertNotNull(roleEntities);
        Assert.assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assert.assertNull(roleEntities);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setUserId(userId);
        roleEntity.setRoleId(1L);
        roleEntity.setIsEnabled(enabled);
        roleRepository.save(roleEntity);

        roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assert.assertNotNull(roleEntities);
        Assert.assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assert.assertNull(roleEntities);

        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setIsEnabled(enabled);
        roleRepository.save(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setIsEnabled(enabled);
        roleRepository.save(roleEntity2);

        roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assert.assertNotNull(roleEntities);
        Assert.assertEquals(2, roleEntities.size());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");
        roleEntity.setIsEnabled(true);
        roleEntity.setDescription("test description");
        roleRepository.save(roleEntity);

        Long roleId = roleEntity.getId();
        String newName = "newName";
        Boolean newEnabled = false;
        String newDescription = "new description";

        int result = roleRepository.update(new RoleEntity()
                .setId(roleId)
                .setName(newName)
                .setIsEnabled(newEnabled)
                .setDescription(newDescription));

        RoleEntity updatedRoleEntity = roleRepository.findById(roleId).get();
        Assert.assertEquals(newName, updatedRoleEntity.getName());
        Assert.assertEquals(newEnabled, updatedRoleEntity.isEnabled());
        Assert.assertEquals(newDescription, updatedRoleEntity.getDescription());

        Assert.assertEquals(1, result);
    }
}