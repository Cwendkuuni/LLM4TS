package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleEntity roleEntity;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        // add expected roleEntities to roleRepository
        roleEntities = roleRepository.findByIdIn(ids);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roleEntities = new ArrayList<>();
        // add expected roleEntities to roleRepository
        roleEntities = roleRepository.findByName(name);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        // add expected roleEntities to roleRepository
        roleEntities = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Admin";
        Long organizationId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.empty();
        // add expected RoleEntity to roleRepository
        optionalRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNotNull(optionalRoleEntity);
        assertFalse(optionalRoleEntity.isEmpty());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        // add expected roleEntities to roleRepository
        roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        // add expected roleEntities to roleRepository
        roleEntities = roleRepository.findByUserId(userId);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        // add expected roleEntities to roleRepository
        roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        // add expected roleEntities to roleRepository
        roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertNotNull(roleEntities);
        assertFalse(roleEntities.isEmpty());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        // set parameters for roleEntity
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}