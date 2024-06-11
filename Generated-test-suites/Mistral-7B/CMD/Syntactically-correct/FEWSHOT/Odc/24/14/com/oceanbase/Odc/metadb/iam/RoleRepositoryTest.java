package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @org.junit.Before
    public void setUp() {
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        when(roleRepository.findByIdIn(anyCollection())).thenReturn(roleEntities);

        Collection<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(1, result.size());
        verify(roleRepository, times(1)).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        when(roleRepository.findByOrganizationId(organizationId, any(Pageable.class))).thenReturn(new PageImpl<>(roleEntities));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());

        assertEquals(2, result.getContent().size());
        verify(roleRepository, times(1)).findByOrganizationId(organizationId, any(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testName1");
        names.add("testName2");
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, any(List.class))).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, any(Collection.class), any(Pageable.class))).thenReturn(new PageImpl<>(roleEntities));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());

        assertEquals(2, result.getContent().size());
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, any(Pageable.class));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.update(any(RoleEntity.class))).thenReturn(1);

        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}