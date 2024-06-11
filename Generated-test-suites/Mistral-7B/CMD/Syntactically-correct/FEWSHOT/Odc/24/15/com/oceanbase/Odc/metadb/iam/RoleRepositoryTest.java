package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.iamRepository.RoleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity testRole1;
    private RoleEntity testRole2;

    @Before
    public void setUp() {
        testRole1 = new RoleEntity();
        testRole1.setName("testRole1");
        testRole1.setOrganizationId(1L);
        testRole1.setDescription("test description 1");
        testRole1.setIsEnabled(true);

        testRole2 = new RoleEntity();
        testRole2.setName("testRole2");
        testRole2.setOrganizationId(1L);
        testRole2.setDescription("test description 2");
        testRole2.setIsEnabled(false);

        roleRepository.saveAll(Arrays.asList(testRole1, testRole2));
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);

        Assert.assertEquals(ids.size(), roles.size());
        Assert.assertTrue(roles.contains(testRole1));
        Assert.assertTrue(roles.contains(testRole2));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("testRole1");

        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(testRole1, roles.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = new PageRequest(0, 10);
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);

        Assert.assertFalse(roles.isEmpty());
        Assert.assertTrue(roles.getContent().contains(testRole1));
        Assert.assertTrue(roles.getContent().contains(testRole2));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("testRole1", 1L);

        Assert.assertTrue(role.isPresent());
        Assert.assertEquals(testRole1, role.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, names);

        Assert.assertEquals(names.size(), roles.size());
        Assert.assertTrue(roles.contains(testRole1));
        Assert.assertTrue(roles.contains(testRole2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = new PageRequest(0, 10);

        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);

        Assert.assertFalse(roles.isEmpty());
        Assert.assertTrue(roles.getContent().contains(testRole1));
        Assert.assertTrue(roles.getContent().contains(testRole2));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserId(userId);

        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(testRole1, roles.get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, true);

        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(testRole1, roles.get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

        Assert.assertEquals(roles.size(), 1);
        Assert.assertTrue(roles.contains(testRole1));
    }

    @Test
    @Transactional
    public void testUpdate() {
        testRole1.setName("newTestRole1");
        testRole1.setDescription("new test description 1");
        testRole1.setIsEnabled(false);

        int rowsAffected = roleRepository.update(testRole1);
        RoleEntity updatedRole = roleRepository.findById(1L).get();

        Assert.assertEquals(1, rowsAffected);
        Assert.assertEquals("newTestRole1", updatedRole.getName());
        Assert.assertEquals("new test description 1", updatedRole.getDescription());
        Assert.assertFalse(updatedRole.isEnabled());
    }
}