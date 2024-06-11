package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> foundRoles = roleRepository.findByIdIn(ids);

        assertNotNull(foundRoles);
        assertEquals(3, foundRoles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> foundRoles = roleRepository.findByName("Admin");

        assertNotNull(foundRoles);
        assertEquals("Admin", foundRoles.get(0).getName());
    }

    // Add test cases for other public methods

}