package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;

    @Before
    public void setup() {
        // Initialize any necessary objects for testing
        roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("Role1");
        roleEntity1.setEnabled(true);
        roleEntity1.setDescription("Description1");

        roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("Role2");
        roleEntity2.setEnabled(false);
        roleEntity2.setDescription("Description2");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.saveAll(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> foundRoles = roleRepository.findByIdIn(Arrays.asList(roleEntity1.getId(), roleEntity2.getId()));

        assertEquals(2, foundRoles.size());
    }

    // Repeat the same pattern for other test methods

}