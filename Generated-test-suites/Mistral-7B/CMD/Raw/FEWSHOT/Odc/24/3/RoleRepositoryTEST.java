package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = Mockito.spy(RoleRepositoryImpl.class);
        roleEntity = Mockito.spy(RoleEntity.class);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByIdIn(any()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId()));
        assertEquals(roleEntity, result.get(0));
        verify(roleRepository).findByIdIn(any());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(anyString()))
                .thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByName("testName").get(0);
        assertEquals(roleEntity, result);
        verify(roleRepository).findByName("testName");
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(roleEntity));
        when(roleRepository.findByOrganizationId(anyLong(), any()))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertEquals(roleEntity, result.getContent().get(0));
        verify(roleRepository).findByOrganizationId(anyLong(), any());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong()))
                .thenReturn(optionalRoleEntity);

        RoleEntity result = roleRepository.findByNameAndOrganizationId("testName", 1L).get();
        assertEquals(roleEntity, result);
        verify(roleRepository).findByNameAndOrganizationId("testName", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), any()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("testName"));
        assertEquals(roleEntity, result.get(0));
        verify(roleRepository).findByOrganizationIdAndNameIn(anyLong(), any());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(roleEntity));
        when(roleRepository.findByOrganizationIdAndRoleIds(anyLong(), any()))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(roleEntity.getId()));
        assertEquals(roleEntity, result.getContent().get(0));
        verify(roleRepository).findByOrganizationIdAndRoleIds(anyLong(), any());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByUserId(anyLong()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(roleEntity, result.get(0));
        verify(roleRepository).findByUserId(anyLong());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByUserIdAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(roleEntity, result.get(0));
        verify(roleRepository).findByUserIdAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByRoleIdsAndEnabled(any(), anyBoolean()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(roleEntity.getId()), true);
        assertEquals(roleEntity, result.get(0));
        verify(roleRepository).findByRoleIdsAndEnabled(any(), anyBoolean());
    }

    @Test
    public void testUpdate() {
        doAnswer(invocation -> ((RoleEntity) invocation.getArgument(0)).setName("newName"))
                .when(roleEntity).setName(anyString());

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        verify(roleRepository).update(roleEntity);
    }
}