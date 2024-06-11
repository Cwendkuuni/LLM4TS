package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);

        for (RoleEntity roleEntity : roleEntities) {
            assertTrue(ids.contains(roleEntity.getId()));
        }
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);

        for (RoleEntity roleEntity : roleEntities) {
            assertEquals(name, roleEntity.getName());
        }
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, PageRequest.of(0, 1));

        for (RoleEntity roleEntity : roleEntities) {
            assertEquals(organizationId, roleEntity.getOrganizationId());
        }
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Admin";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(roleEntityOptional.isPresent());
        assertEquals(name, roleEntityOptional.get().getName());
        assertEquals(organizationId, roleEntityOptional.get().getOrganizationId());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        // perform the test
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        // perform the test
    }

    @Test
    public void testFindByUserId() {
        // perform the test
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        // perform the test
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        // perform the test
    }

    @Test
    public void testUpdate() {
        // perform the test
    }
}
