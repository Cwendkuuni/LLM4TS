package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
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
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        roles.add(role1);
        roles.add(role2);

        roleRepository.saveAll(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByName() {
        RoleEntity role = new RoleEntity();
        role.setName("Admin");

        roleRepository.save(role);

        List<RoleEntity> result = roleRepository.findByName("Admin");

        assertNotNull(result);
        assertEquals("Admin", result.get(0).getName());
    }

    // Add test methods for other repository methods

    // Remember to cleanup test data after each test case

}