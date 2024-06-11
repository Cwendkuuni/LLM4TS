package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringBootTest;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(org.junit.runner.JUnit4.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertNotNull(roleEntities);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertNull(roleEntities);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleRepository.save(roleEntity);
        roleEntities = roleRepository.findByName(name);
        assertNotNull(roleEntities);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, new PageRequest(0, 10));
        assertNotNull(page);
        assertTrue(page.hasContent());
        assertEquals(10, page.getSize());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertFalse(roleEntityOptional.isPresent());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        roleRepository.save(roleEntity);
        roleEntityOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(roleEntityOptional.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Collections.singletonList("testRole");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNull(roleEntities);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");
        roleEntity.setOrganizationId(organizationId);
        roleRepository.save(roleEntity);
        roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNotNull(roleEntities);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, new PageRequest(0, 10));
        assertNotNull(page);
        assertTrue(page.hasContent());
        assertEquals(10, page.getSize());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        assertNotNull(roleEntities);
        assertTrue(roleEntities.isEmpty());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setUserId(userId);
        roleEntity.setRoleId(1L);
        roleRepository.save(roleEntity);
        roleEntities = roleRepository.findByUserId(userId);
        assertNotNull(roleEntities);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, true);
        assertNotNull(roleEntities);
        assertTrue(roleEntities.isEmpty());

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setUserId(userId);
        roleEntity.setRoleId(1L);
        roleEntity.setEnabled(true);
        roleRepository.save(roleEntity);
        roleEntities = roleRepository.findByUserIdAndEnabled(userId, true);
        assertNotNull(roleEntities);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertNull(roleEntities);

        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setEnabled(true);
        roleRepository.save(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setEnabled(false);
        roleRepository.save(roleEntity2);

        roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertNotNull(roleEntities);
        assertEquals(1, roleEntities.size());
        assertEquals(roleEntity1.getId(), roleEntities.get(0).getId());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");
        roleEntity.setEnabled(true);
       RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        long id = savedRoleEntity.getId();

        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setName("updatedTestRole");
        updatedRoleEntity.setEnabled(false);
        int result = roleRepository.update(updatedRoleEntity);
        assertEquals(1, result);

        RoleEntity updatedRoleEntityFromDB = roleRepository.findById(id).get();
        assertEquals("updatedTestRole", updatedRoleEntityFromDB.getName());
        assertEquals(false, updatedRoleEntityFromDB.isEnabled());
    }
}