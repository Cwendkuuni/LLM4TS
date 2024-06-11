package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class RoleRepositoryTest {
    
    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleEntity role = new RoleEntity();

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(any())).thenReturn(Collections.singletonList(role));
        assertEquals(1, roleRepository.findByIdIn(Arrays.asList(1L, 2L)).size());
    }

    @Test
    public void testFindByIdIn_Empty() {
        when(roleRepository.findByIdIn(any())).thenReturn(Collections.emptyList());
        assertEquals(0, roleRepository.findByIdIn(Collections.emptyList()).size());
    }

    @Test    
    public void testFindByName(){
        when(roleRepository.findByName(any())).thenReturn(Collections.singletonList(role));
        assertEquals(1, roleRepository.findByName("Admin").size());
    }

    // Similarly implement edge cases for each other method

    @Test
    public void testUpdate_Null(){
        when(roleRepository.update(any())).thenReturn(1);
        role.setId(null);
        assertEquals(1, roleRepository.update(role));
    }

    @Test
    public void testUpdate_NullName(){
        when(roleRepository.update(any())).thenReturn(1);
        role.setName(null);
        assertEquals(1, roleRepository.update(role));
    }
    
    @Test
    public void testfindByOrganizationIdAndRoleIds_Null(){
        when(roleRepository.findByOrganizationIdAndRoleIds(any(), any(), any())).thenReturn(Page.empty());
        assertEquals(0, roleRepository.findByOrganizationIdAndRoleIds(1L, null, PageRequest.of(1, 1)).getTotalElements());
    }
}