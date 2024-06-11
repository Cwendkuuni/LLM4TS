package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findAll();
        List<Long> ids = new ArrayList<>();
        for (RoleEntity role : roles) {
            ids.add(role.getId());
        }
        List<RoleEntity> foundRoles = roleRepository.findByIdIn(ids);
        assertEquals(roles.size(), foundRoles.size());
    }

    @Test
    public void testFindByName() {
        String roleName = "Admin";
        List<RoleEntity> foundRoles = roleRepository.findByName(roleName);
        assertNotNull(foundRoles);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, pageable);
        assertNotNull(roles);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String roleName = "Admin";
        Long organizationId = 1L;
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(roleName, organizationId);
        assertNotNull(role);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("Admin", "User");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNotNull(roles);
    }

    // Add more test cases for the remaining methods in RoleRepository

}