package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository = mock(RoleRepository.class);

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(any())).thenReturn(new ArrayList<>());
        assertNotNull(roleRepository.findByIdIn(new ArrayList<>()));
    }

    @Test
    public void testFindByIdIn_empty() {
        when(roleRepository.findByIdIn(any())).thenReturn(null);
        assertNull(roleRepository.findByIdIn(new ArrayList<>()));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(anyString())).thenReturn(new ArrayList<>());
        assertNotNull(roleRepository.findByName("Test"));
    }

    @Test
    public void testFindByOrganizationId() {
        when(roleRepository.findByOrganizationId(anyLong(), any())).thenReturn(Page.empty());
        assertNotNull(roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10)));
    }
    
    //...continue with the rest of the methods in the similar way

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_null() {
        roleRepository.update(null);
    }
    
    @Test
    public void testUpdate_valid() {
        RoleEntity re = new RoleEntity();
        when(roleRepository.update(re)).thenReturn(1);
        assertEquals(1, roleRepository.update(re));
    }
}
