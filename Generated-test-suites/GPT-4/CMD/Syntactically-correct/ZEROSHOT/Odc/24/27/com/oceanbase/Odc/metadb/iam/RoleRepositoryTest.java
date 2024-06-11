package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Admin";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNotNull(roleEntity);
        assertTrue(roleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("Admin", "User");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNotNull(roleEntities);
    }
  
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertNotNull(roleEntities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertNotNull(roleEntities);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("AdminUpdated");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Updated description");
        int result = roleRepository.update(roleEntity);
        assertTrue(result > 0);
    }
}
