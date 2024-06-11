package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoleRepositoryTest {

    @Test
    public void testFindByIdIn() {
        // create test data
        List<RoleEntity> roles = Arrays.asList(new RoleEntity(1L, "Role1"), new RoleEntity(2L, "Role2"));
        RoleRepository roleRepository = new RoleRepository();

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(roles.size(), result.size());
        assertEquals(roles.get(0), result.get(0));
        assertEquals(roles.get(1), result.get(1));

        result = roleRepository.findByIdIn(Collections.emptyList());
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByName() {
        // create test data
        RoleEntity role = new RoleEntity(1L, "Role1");
        RoleRepository roleRepository = new RoleRepository();

        List<RoleEntity> result = roleRepository.findByName("Role1");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(role, result.get(0));

        result = roleRepository.findByName("NonexistentRole");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // Add test cases for other methods in RoleRepository

}