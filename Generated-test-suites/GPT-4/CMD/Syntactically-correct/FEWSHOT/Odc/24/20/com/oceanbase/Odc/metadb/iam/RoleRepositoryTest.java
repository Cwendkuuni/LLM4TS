package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;

public class RoleRepositoryTest {

    @InjectMocks
    RoleRepository roleRepository;

    @Mock
    List<RoleEntity> roleList;

    @Mock
    RoleEntity roleEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(anyList())).thenReturn(roleList);
        List<RoleEntity> returnedList = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertEquals(roleList, returnedList);
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(anyString())).thenReturn(roleList);
        List<RoleEntity> returnedList = roleRepository.findByName("roleName");
        assertEquals(roleList, returnedList);
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<RoleEntity> page = mock(Page.class);
        when(roleRepository.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(page);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> opt = Optional.of(roleEntity);
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(opt);
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("roleName", 1L);
        assertEquals(roleEntity, result.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        when(roleRepository.findByOrganizationIdAndNameIn(anyLong(), anyList())).thenReturn(roleList);
        List<RoleEntity> returnedList = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("role1", "role2"));
        assertEquals(roleList, returnedList);
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(anyLong())).thenReturn(roleList);
        List<RoleEntity> returnedList = roleRepository.findByUserId(1L);
        assertEquals(roleList, returnedList);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(anyLong(), anyBoolean())).thenReturn(roleList);
        List<RoleEntity> returnedList = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(roleList, returnedList);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        when(roleRepository.findByRoleIdsAndEnabled(anyList(), anyBoolean())).thenReturn(roleList);
        List<RoleEntity> returnedList = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true);
        assertEquals(roleList, returnedList);
    }

    @Test
    public void testUpdate() {
        when(roleEntity.getId()).thenReturn(1L);
        when(roleEntity.getName()).thenReturn("roleName");
        when(roleEntity.isEnabled()).thenReturn(true);
        when(roleEntity.getDescription()).thenReturn("A test role");
        when(roleRepository.update(roleEntity)).thenReturn(1);
        int rowsAffected = roleRepository.update(roleEntity);
        assertTrue(rowsAffected > 0);
    }
}