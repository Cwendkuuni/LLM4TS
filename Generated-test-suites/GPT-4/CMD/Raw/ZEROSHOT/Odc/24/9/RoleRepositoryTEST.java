
package com.oceanbase.odc.metadb.iam;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        roleRepository.findByIdIn(ids);
        verify(roleRepository, times(1)).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("testRole");
        verify(roleRepository, times(1)).findByName("testRole");
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(1L, mock(Pageable.class));
        verify(roleRepository, times(1)).findByOrganizationId(anyLong(), any(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleOptional = roleRepository.findByNameAndOrganizationId("testRole", 1L);
        assertNotNull(roleOptional);
        verify(roleRepository, times(1)).findByNameAndOrganizationId(anyString(), anyLong());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = new ArrayList<>();
        names.add("testRole");
        roleRepository.findByOrganizationIdAndNameIn(1L, names);
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(anyLong(), anyList());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, mock(Pageable.class));
        assertNotNull(page);
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(anyLong(), anyList(), any(Pageable.class));
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1L);
        verify(roleRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        verify(roleRepository, times(1)).findByUserIdAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(anyList(), anyBoolean());
    }

    @Test
    public void testUpdate() {
        when(roleEntity.getId()).thenReturn(1L);
        when(roleEntity.getName()).thenReturn("testRole");
        when(roleEntity.getDescription()).thenReturn("Test Role");
        when(roleEntity.isEnabled()).thenReturn(true);

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}
