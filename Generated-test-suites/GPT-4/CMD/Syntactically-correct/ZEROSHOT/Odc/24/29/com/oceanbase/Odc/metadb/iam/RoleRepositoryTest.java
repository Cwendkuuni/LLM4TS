package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(roles);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertEquals(10, roles.getSize());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertNotNull(roleEntity.orElse(null));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Role1", "Role2"));
        assertNotNull(roles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L),
               PageRequest.of(0, 10));
        assertEquals(10, roles.getSize());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertNotNull(roles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertNotNull(roles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertNotNull(roles);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("AdminUpdated");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Updated Role");
        int updated = roleRepository.update(roleEntity);
        assertEquals(1, updated);
    }
}