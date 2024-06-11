package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleRepositoryImpl;
import com.oceanbase.odc.metadb.iam.RoleSpecifications;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;
    private Long roleId = 1L;
    private Long organizationId = 1L;
    private String roleName = "testRole";

    @Before
    public void setup() {
        roleRepository = Mockito.spy(new RoleRepositoryImpl());
        roleEntity = new RoleEntity();
        roleEntity.setId(roleId);
        roleEntity.setName(roleName);
        roleEntity.setOrganizationId(organizationId);
        roleEntity.setEnabled(true);
    }

    @Test
    public void testFindById() {
        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));
        RoleEntity retrievedRole = roleRepository.findById(roleId).get();
        assertEquals(roleId, retrievedRole.getId());
        assertEquals(roleName, retrievedRole.getName());
        assertEquals(organizationId, retrievedRole.getOrganizationId());
        assertEquals(true, retrievedRole.isEnabled());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        Mockito.when(roleRepository.findByName(roleName)).thenReturn(roleEntities);
        List<RoleEntity> retrievedRoles = roleRepository.findByName(roleName);
        assertEquals(1, retrievedRoles.size());
        assertEquals(roleId, retrievedRoles.get(0).getId());
        assertEquals(roleName, retrievedRoles.get(0).getName());
        assertEquals(organizationId, retrievedRoles.get(0).getOrganizationId());
        assertEquals(true, retrievedRoles.get(0).isEnabled());
    }

    @Test
    public void testFindByOrganizationId() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> page = Page.of(roleEntities, pageable);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);
        Page<RoleEntity> retrievedPage = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(1, retrievedPage.getTotalElements());
        assertEquals(roleId, retrievedPage.getContent().get(0).getId());
        assertEquals(roleName, retrievedPage.getContent().get(0).getName());
        assertEquals(organizationId, retrievedPage.getContent().get(0).getOrganizationId());
        assertEquals(true, retrievedPage.getContent().get(0).isEnabled());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Mockito.when(roleRepository.findByNameAndOrganizationId(roleName, organizationId))
                .thenReturn(Optional.of(roleEntity));
        RoleEntity retrievedRole = roleRepository.findByNameAndOrganizationId(roleName, organizationId).get();
        assertEquals(roleId, retrievedRole.getId());
        assertEquals(roleName, retrievedRole.getName());
        assertEquals(organizationId, retrievedRole.getOrganizationId());
        assertEquals(true, retrievedRole.isEnabled());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, Collections.singletonList(roleName)))
                .thenReturn(roleEntities);
        List<RoleEntity> retrievedRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId,
                Collections.singletonList(roleName));
        assertEquals(1, retrievedRoles.size());
        assertEquals(roleId, retrievedRoles.get(0).getId());
        assertEquals(roleName, retrievedRoles.get(0).getName());
        assertEquals(organizationId, retrievedRoles.get(0).getOrganizationId());
        assertEquals(true, retrievedRoles.get(0).isEnabled());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        Page<RoleEntity> page = Page.of(roleEntities, pageable);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, Collections.singleton(roleId),
                pageable)).thenReturn(page);
        Page<RoleEntity> retrievedPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId,
                Collections.singleton(roleId), pageable);
        assertEquals(1, retrievedPage.getTotalElements());
        assertEquals(roleId, retrievedPage.getContent().get(0).getId());
        assertEquals(roleName, retrievedPage.getContent().get(0).getName());
        assertEquals(organizationId, retrievedPage.getContent().get(0).getOrganizationId());
        assertEquals(true, retrievedPage.getContent().get(0).isEnabled());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);
        List<RoleEntity> retrievedRoles = roleRepository.findByUserId(userId);
        assertEquals(1, retrievedRoles.size());
        assertEquals(roleId, retrievedRoles.get(0).getId());
        assertEquals(roleName, retrievedRoles.get(0).getName());
        assertEquals(organizationId, retrievedRoles.get(0).getOrganizationId());
        assertEquals(true, retrievedRoles.get(0).isEnabled());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, true)).thenReturn(roleEntities);
        List<RoleEntity> retrievedRoles = roleRepository.findByUserIdAndEnabled(userId, true);
        assertEquals(1, retrievedRoles.size());
        assertEquals(roleId, retrievedRoles.get(0).getId());
        assertEquals(roleName, retrievedRoles.get(0).getName());
        assertEquals(organizationId, retrievedRoles.get(0).getOrganizationId());
        assertEquals(true, retrievedRoles.get(0).isEnabled());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(roleEntities);
        List<RoleEntity> retrievedRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(1, retrievedRoles.size());
        assertEquals(roleId, retrievedRoles.get(0).getId());
        assertEquals(roleName, retrievedRoles.get(0).getName());
        assertEquals(organizationId, retrievedRoles.get(0).getOrganizationId());
        assertEquals(true, retrievedRoles.get(0).isEnabled());
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setId(roleId);
        updatedRoleEntity.setName("updatedRoleName");
        updatedRoleEntity.setOrganizationId(organizationId);
        updatedRoleEntity.setEnabled(true);

        Mockito.when(roleRepository.update(updatedRoleEntity)).thenReturn(1);
        int rowsAffected = roleRepository.update(updatedRoleEntity);
        assertEquals(1, rowsAffected);
    }
}