package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
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
    private List<RoleEntity> roleEntities;

    @Before
    public void setup() {
        roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("role1");
        roleEntity1.setOrganizationId(1L);
        roleEntities.add(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("role2");
        roleEntity2.setOrganizationId(1L);
        roleEntities.add(roleEntity2);

        roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByIdIn(anyCollection())).thenReturn(new PageImpl<>(roleEntities));
        when(roleRepository.findByName(anyString())).thenReturn(roleEntities);
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(Optional.of(roleEntity1));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(roleEntity1));
        when(roleRepository.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(roleEntities));
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList())).thenReturn(roleEntities);
        when(roleRepository.findByOrganizationIdAndRoleIds(anyLong(), anyCollection(), any(Pageable.class))).thenReturn(new PageImpl<>(roleEntities));
        when(roleRepository.findByUserId(anyLong())).thenReturn(roleEntities);
        when(roleRepository.findByUserIdAndEnabled(anyLong(), anyBoolean())).thenReturn(roleEntities);
        when(roleRepository.findByRoleIdsAndEnabled(anyCollection(), anyBoolean())).thenReturn(roleEntities);
        when(roleRepository.update(any(RoleEntity.class))).thenReturn(1);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        Page<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals("Size: 2", result.getSize());
        assertEquals("First Element: role1", result.getContent().get(0).getName());
    }

    @Test
    public void testFindByName() {
        String name = "role1";
        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals("Size: 1", result.size());
        assertEquals("First Element: role1", result.get(0).getName());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "role1";
        Long organizationId = 1L;
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(result.isPresent());
        assertEquals("role1", result.get().getName());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Optional<RoleEntity> result = roleRepository.findById(id);
        assertTrue(result.isPresent());
        assertEquals("role1", result.get().getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
        assertEquals("Size: 2", result.getSize());
        assertEquals("First Element: role1", result.getContent().get(0).getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("role1");
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals("Size: 1", result.size());
        assertEquals("First Element: role1", result.get(0).getName());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertEquals("Size: 2", result.getSize());
        assertEquals("First Element: role1", result.getContent().get(0).getName());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals("Size: 1", result.size());
        assertEquals("First Element: role1", result.get(0).getName());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals("Size: 1", result.size());
        assertEquals("First Element: role1", result.get(0).getName());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        Boolean enabled = true;
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals("Size: 1", result.size());
        assertEquals("First Element: role1", result.get(0).getName());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("updatedRoleName");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("updatedDescription");

        int result = roleRepository.update(roleEntity);
        assertEquals("Number of updated records: 1", result);
    }
}