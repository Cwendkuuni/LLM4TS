package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertEquals(3, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("testRole");
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals("testRole")));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity role = roleRepository.findByNameAndOrganizationId("testRole", 1L).orElse(null);
        assertEquals("testRole", role.getName());
        assertEquals(1L, role.getOrganizationId().longValue());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2"));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals("testRole1")));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals("testRole2")));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertTrue(roles.stream().anyMatch(role -> role.getUsers().contains(1L)));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertTrue(roles.stream().anyMatch(role -> role.getUsers().contains(1L) && role.getEnabled()));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertTrue(roles.stream().anyMatch(role -> role.getId() == 1L && role.getEnabled()));
        assertTrue(roles.stream().anyMatch(role -> role.getId() == 2L && role.getEnabled()));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleToUpdate = new RoleEntity();
        roleToUpdate.setId(1L);
        roleToUpdate.setName("updatedName");
        roleToUpdate.setEnabled(true);
        roleToUpdate.setDescription("updatedDescription");

        int updatedRowCount = roleRepository.update(roleToUpdate);
        assertEquals(1, updatedRowCount);

        RoleEntity updatedRole = roleRepository.findById(roleToUpdate.getId()).orElse(null);
        assertEquals("updatedName", updatedRole.getName());
        assertEquals(true, updatedRole.getEnabled());
        assertEquals("updatedDescription", updatedRole.getDescription());
    }

}