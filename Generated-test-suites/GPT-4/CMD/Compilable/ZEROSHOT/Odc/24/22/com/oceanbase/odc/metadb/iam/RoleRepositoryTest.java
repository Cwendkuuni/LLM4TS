
package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    RoleRepository roleRepository;

    @Mock
    RoleRepository mockRoleRepository;

    @Mock
    Pageable pageable;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Collections.emptyList();
        when(mockRoleRepository.findByIdIn(any())).thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = Collections.emptyList();
        when(mockRoleRepository.findByName(anyString())).thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByName("Test");
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = mock(Page.class);
        when(mockRoleRepository.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(page);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
        assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = Optional.of(new RoleEntity());
        when(mockRoleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(optionalRoleEntity);
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Test", 1L);
        assertEquals(optionalRoleEntity, result);
    }

    // Continue with similar structure for all the methods ...

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        when(mockRoleRepository.update(any(RoleEntity.class))).thenReturn(1);
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}
