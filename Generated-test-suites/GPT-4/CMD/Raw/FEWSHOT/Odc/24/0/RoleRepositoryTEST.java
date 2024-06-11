package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(roleRepository.findByIdIn(ids)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("testName")).thenReturn(Collections.singletonList(roleEntity));
        List<RoleEntity> result = roleRepository.findByName("testName");
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByOrganizationId() {
        when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(mock(Page.class));
        assertNotNull(roleRepository.findByOrganizationId(1L, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId("testName", 1L)).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("testName", 1L);
        assertEquals(roleEntity, result.get());
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(1L)).thenReturn(Collections.singletonList(roleEntity));
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(1, result.size());
    }
	
    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Collections.singletonList(roleEntity));
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L,2L,3L);
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(Collections.singletonList(roleEntity));
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(roleEntity)).thenReturn(1);
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}