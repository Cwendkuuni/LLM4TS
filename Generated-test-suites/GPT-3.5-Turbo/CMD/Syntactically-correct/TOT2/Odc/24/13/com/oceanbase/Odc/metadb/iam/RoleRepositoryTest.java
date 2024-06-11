package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findAll();
        List<RoleEntity> foundRoles = roleRepository.findByIdIn(Collections.singletonList(roles.get(0).getId()));

        assertEquals(1, foundRoles.size());
        assertEquals(roles.get(0).getName(), foundRoles.get(0).getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> foundRoles = roleRepository.findByName("Admin");

        assertFalse(foundRoles.isEmpty());
    }

    // Test cases for other methods can be added here

}