package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertEquals(3, roles.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, PageRequest.of(0, 10));
        assertNotNull(roles);
        assertTrue(roles.getTotalElements() > 0);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Admin";
        Long organizationId = 1L;
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(role.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
        assertNotNull(roles);
        assertTrue(roles.getTotalElements() > 0);
    }

    // Add test cases for other methods in RoleRepository as needed

}