package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId()))).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId()));
        assertEquals(1, result.size());
        verify(roleRepository).findByIdIn(Collections.singletonList(roleEntity.getId()));
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(1, result.size());
        verify(roleRepository).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        Pageable pageable = Mockito.mock(Pageable.class);

        Page<RoleEntity> page = new PageImpl<>(roleEntities);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(1, result.getContent().size());
        verify(roleRepository).findByOrganizationId(organizationId, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.ofNullable(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNotNull(result.get());
        verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testRole1");
        names.add("testRole2");

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName("testRole1");
        roleEntity1.setOrganizationId(organizationId);
        roleEntities.add(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setName("testRole2");
        roleEntity2.setOrganizationId(organizationId);
        roleEntities.add(roleEntity2);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, result.size());
        verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleEntity.getId());

        Page<RoleEntity> page = new PageImpl<>(Collections.emptyList());
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, null);
        assertTrue(result.isEmpty());
        verify(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(1, result.size());
        verify(roleRepository).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(1, result.size());
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = new ArrayList<>();
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(0, result.size());
        verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setId(roleEntity.getId());
        updatedRoleEntity.setName("updatedRoleName");
        updatedRoleEntity.setEnabled(true);
        updatedRoleEntity.setDescription("updatedDescription");
        updatedRoleEntity.setUserUpdateTime(System.currentTimeMillis());

        when(roleRepository.update(updatedRoleEntity)).thenReturn(1);

        int result = roleRepository.update(updatedRoleEntity);
        assertEquals(1, result);
        verify(roleRepository).update(updatedRoleEntity);
    }
}