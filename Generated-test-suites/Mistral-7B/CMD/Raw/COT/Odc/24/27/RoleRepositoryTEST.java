package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(classes = RoleRepositoryTest.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    // Test for findByIdIn method
    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    // Test for findByName method
    @Test
    public void testFindByName() {
        String name = "testRole";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertNotNull(roleEntities);
        if (roleEntities != null && !roleEntities.isEmpty()) {
            RoleEntity roleEntity = roleEntities.get(0);
            assertEquals(name, roleEntity.getName());
        } else {
            assertEmpty(roleEntities);
        }
    }

    // Test for findByOrganizationId method with Pageable
    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> rolePages = roleRepository.findByOrganizationId(organizationId, pageable);
        assertNotNull(rolePages);
        assertEquals(true, rolePages.isPresent());
        List<RoleEntity> roleEntities = rolePages.getContent();
        assertFalse(roleEntities.isEmpty());
    }

    // Test for findByNameAndOrganizationId method
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
        if (roleEntityOptional.isPresent()) {
            RoleEntity roleEntity = roleEntityOptional.get();
            assertEquals(name, roleEntity.getName());
            assertEquals(organizationId, roleEntity.getOrganizationId());
        } else {
            assertTrue(roleEntityOptional.isEmpty());
        }
    }

    // Test for findByOrganizationIdAndNameIn method
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testRole1");
        names.add("testRole2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());

        for (RoleEntity roleEntity : roleEntities) {
            boolean isMatch = false;
            for (String name : names) {
                if (roleEntity.getName().equals(name)) {
                    isMatch = true;
                    break;
                }
            }
            assertTrue(isMatch);
        }
    }

    // Test for findByOrganizationIdAndRoleIds method
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Page<RoleEntity> rolePages = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
        assertNotNull(rolePages);
        assertEquals(true, rolePages.isPresent());
        List<RoleEntity> roleEntities = rolePages.getContent();
        assertFalse(roleEntities.isEmpty());

        for (RoleEntity roleEntity : roleEntities) {
            boolean isMatch = false;
            for (Long roleId : roleIds) {
                if (roleEntity.getId().equals(roleId)) {
                    isMatch = true;
                    break;
                }
            }
            assertTrue(isMatch);
        }
    }

    // Test for findByUserId method
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    // Test for findByUserIdAndEnabled method
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    // Test for findByRoleIdsAndEnabled method
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());

        for (RoleEntity roleEntity : roleEntities) {
            boolean isMatch = false;
            for (Long roleId : roleIds) {
                if (roleEntity.getId().equals(roleId)) {
                    isMatch = true;
                    break;
                }
            }
            assertTrue(isMatch);
            assertEquals(enabled, roleEntity.isEnabled());
        }
    }

    // Test for update method
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test description");
        int rowsAffected = roleRepository.update(roleEntity);
        assertEquals(1, rowsAffected);

        RoleEntity updatedRoleEntity = roleRepository.findByName("testRole").orElse(null);
        assertEquals(roleEntity.getName(), updatedRoleEntity.getName());
        assertEquals(roleEntity.isEnabled(), updatedRoleEntity.isEnabled());
        assertEquals(roleEntity.getDescription(), updatedRoleEntity.getDescription());
    }

    // Test for findByUserIdAndEnabled method with wrong user id
    @Test(expected = EntityNotFoundException.class)
    public void testFindByUserIdAndEnabledWithWrongUserId() {
        Long userId = -1L;
        Boolean enabled = true;
        roleRepository.findByUserIdAndEnabled(userId, enabled);
    }

    // Test for findByNameAndOrganizationId method with wrong name or organization id
    @Test(expected = EntityNotFoundException.class)
    public void testFindByNameAndOrganizationIdWithWrongNameOrOrganizationId() {
        String name = "testRole";
        Long organizationId = -1L;
        roleRepository.findByNameAndOrganizationId(name, organizationId);
    }

    // Test for findByRoleIdsAndEnabled method with wrong role id or enabled value
    @Test(expected = EntityNotFoundException.class)
    public void testFindByRoleIdsAndEnabledWithWrongRoleIdsOrEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(-1L);
        Boolean enabled = true;
        roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
    }

    // Test for findByOrganizationIdAndRoleIds method with empty id list
    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndRoleIdsWithEmptyIdList() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
    }
}