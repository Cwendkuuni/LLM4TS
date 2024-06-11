package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        // Test findByIdIn with valid input
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Role1", true, "Description1"));
        roles.add(new RoleEntity(2L, "Role2", true, "Description2"));
        List<RoleEntity> savedRoles = roleRepository.saveAll(roles);

        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> foundRoles = roleRepository.findByIdIn(ids);
        assertEquals(2, foundRoles.size());
        assertTrue(foundRoles.containsAll(savedRoles));

        // Test findByIdIn with invalid input
        List<RoleEntity> emptyList = roleRepository.findByIdIn(Arrays.asList(3L, 4L));
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testFindByName() {
        // Test findByName with valid input
        RoleEntity role = new RoleEntity(1L, "Role1", true, "Description1");
        roleRepository.save(role);

        List<RoleEntity> foundRoles = roleRepository.findByName("Role1");
        assertNotNull(foundRoles);
        assertFalse(foundRoles.isEmpty());
        assertEquals("Role1", foundRoles.get(0).getName());

        // Test findByName with invalid input
        List<RoleEntity> emptyList = roleRepository.findByName("InvalidName");
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }

    // Add more test cases for other methods in RoleRepository

}