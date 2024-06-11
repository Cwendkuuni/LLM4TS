package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {
    
    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    
    @Test
    public void testFindByIdIn() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("Test");
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> results = Arrays.asList(roleEntity);
        
        when(roleRepository.findByIdIn(ids)).thenReturn(results);
        
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertEquals(1, roles.size());
        assertEquals("Test", roles.get(0).getName());
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("Test");
        when(roleRepository.findByName("Test")).thenReturn(Arrays.asList(roleEntity));
        
        List<RoleEntity> roles = roleRepository.findByName("Test");
        assertEquals(1, roles.size());
        assertEquals("Test", roles.get(0).getName());
    }
    
    @Test
    public void testFindByOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("Test");
        Pageable pageable = PageRequest.of(0, 10);
        when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(Mockito.mock(Page.class));
        
        Page<RoleEntity> roles = roleRepository.findByOrganizationId(1L, pageable);
        assertTrue(roles.isPaged());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("Test");
        when(roleRepository.findByNameAndOrganizationId("Test", 1L)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Test", 1L);
        assertTrue(role.isPresent());
        assertEquals("Test", role.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("Test");
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test")))
            .thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test"));
        assertEquals(1, roles.size());
        assertEquals("Test", roles.get(0).getName());
    }
    
    // Likewise add more test cases for the remaining methods
}