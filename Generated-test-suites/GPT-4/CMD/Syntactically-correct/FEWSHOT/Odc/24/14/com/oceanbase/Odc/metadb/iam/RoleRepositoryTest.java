
package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    
    @InjectMocks
    RoleRepository roleRepository;
    
    @Mock
    RoleEntity roleEntity;
    
    
    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        verify(roleRepository, times(1)).findByIdIn(any());
    }
    
    @Test
    public void testFindByName() {
        roleRepository.findByName("Test");
        verify(roleRepository, times(1)).findByName(anyString());
    }
    
    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(1L, PageRequest.of(0, 5));
        verify(roleRepository, times(1)).findByOrganizationId(anyLong(), any());
    }
    
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("TestRole1", "TestRole2"));
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(anyLong(), any());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("Test", 1L);
        verify(roleRepository, times(1)).findByNameAndOrganizationId(anyString(), anyLong());
    }
    
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L, 3L));
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(anyLong(), any());
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
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true);
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(any(), anyBoolean());
    }
    
    @Test
    public void testUpdate() {
        when(roleEntity.isEnabled()).thenReturn(true);
        when(roleEntity.getDescription()).thenReturn("Test");
        when(roleEntity.getName()).thenReturn("Test");
        when(roleEntity.getId()).thenReturn(1L);

        roleRepository.update(roleEntity);

        verify(roleRepository, times(1)).update(any(RoleEntity.class));
    }
}
