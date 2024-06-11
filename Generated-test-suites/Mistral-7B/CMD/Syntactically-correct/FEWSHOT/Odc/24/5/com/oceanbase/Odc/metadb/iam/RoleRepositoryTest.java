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
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedList = new ArrayList<>();
        RoleEntity roleEntity1 = Mockito.mock(RoleEntity.class);
        RoleEntity roleEntity2 = Mockito.mock(RoleEntity.class);
        expectedList.add(roleEntity1);
        expectedList.add(roleEntity2);

        when(roleRepository.findByIdIn(Mockito.any())).thenReturn(expectedList);

        Collection<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);

        List<RoleEntity> actualList = roleRepository.findByIdIn(ids);

        assertEquals(2, actualList.size());
        verify(roleRepository).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        RoleEntity expectedRoleEntity = Mockito.mock(RoleEntity.class);

        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedRoleEntity));

        RoleEntity actualRoleEntity = roleRepository.findByName(name).get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
        verify(roleRepository).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        List<RoleEntity> expectedList = new ArrayList<>();
        RoleEntity roleEntity1 = Mockito.mock(RoleEntity.class);
        RoleEntity roleEntity2 = Mockito.mock(RoleEntity.class);
        expectedList.add(roleEntity1);
        expectedList.add(roleEntity2);

        Page<RoleEntity> expectedPage = new PageImpl<>(expectedList);

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(expectedPage.getSize(), actualPage.getSize());
        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertEquals(expectedPage.getContent(), actualPage.getContent());
        verify(roleRepository).findByOrganizationId(organizationId, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "admin";
        Long organizationId = 1L;
        RoleEntity expectedRoleEntity = Mockito.mock(RoleEntity.class);

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRoleEntity));

        RoleEntity actualRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId).get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
        verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("admin");
        names.add("user");
        List<RoleEntity> expectedList = new ArrayList<>();
        RoleEntity roleEntity1 = Mockito.mock(RoleEntity.class);
        RoleEntity roleEntity2 = Mockito.mock(RoleEntity.class);
        expectedList.add(roleEntity1);
        expectedList.add(roleEntity2);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedList);

        List<RoleEntity> actualList = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, actualList.size());
        verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        List<RoleEntity> expectedList = new ArrayList<>();
        RoleEntity roleEntity1 = Mockito.mock(RoleEntity.class);
        RoleEntity roleEntity2 = Mockito.mock(RoleEntity.class);
        expectedList.add(roleEntity1);
        expectedList.add(roleEntity2);

        Page<RoleEntity> expectedPage = new PageImpl<>(expectedList);

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any())).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any());

        assertEquals(expectedPage.getSize(), actualPage.getSize());
        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertEquals(expectedPage.getContent(), actualPage.getContent());
        verify(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedList = new ArrayList<>();
        RoleEntity roleEntity1 = Mockito.mock(RoleEntity.class);
        RoleEntity roleEntity2 = Mockito.mock(RoleEntity.class);
        expectedList.add(roleEntity1);
        expectedList.add(roleEntity2);

        when(roleRepository.findByUserId(userId)).thenReturn(expectedList);

        List<RoleEntity> actualList = roleRepository.findByUserId(userId);

        assertEquals(2, actualList.size());
        verify(roleRepository).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedList = new ArrayList<>();
        RoleEntity roleEntity1 = Mockito.mock(RoleEntity.class);
        RoleEntity roleEntity2 = Mockito.mock(RoleEntity.class);
        expectedList.add(roleEntity1);
        expectedList.add(roleEntity2);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedList);

        List<RoleEntity> actualList = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(2, actualList.size());
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        List<RoleEntity> expectedList = new ArrayList<>();
        RoleEntity roleEntity1 = Mockito.mock(RoleEntity.class);
        RoleEntity roleEntity2 = Mockito.mock(RoleEntity.class);
        expectedList.add(roleEntity1);
        expectedList.add(roleEntity2);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedList);

        List<RoleEntity> actualList = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, actualList.size());
        verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = Mockito.mock(RoleEntity.class);
        Long id = 1L;
        String name = "newName";
        Boolean enabled = true;
        String description = "newDescription";

        doNothing().when(roleRepository).update(roleEntity);

        roleRepository.update(roleEntity);

        verify(roleRepository).update(roleEntity);

        when(roleRepository.update(roleEntity)).thenReturn(1);

        int actualResult = roleRepository.update(roleEntity);

        assertEquals(1, actualResult);
        verify(roleRepository, times(2)).update(roleEntity);

        RoleEntity updatedRoleEntity = Mockito.mock(RoleEntity.class);
        when(roleEntity.getId()).thenReturn(id);
        when(roleEntity.getName()).thenReturn(name);
        when(roleEntity.isEnabled()).thenReturn(enabled);
        when(roleEntity.getDescription()).thenReturn(description);

        doNothing().when(roleRepository).update(roleEntity);

        roleRepository.update(roleEntity);

        verify(roleRepository, times(3)).update(roleEntity);
    }
}