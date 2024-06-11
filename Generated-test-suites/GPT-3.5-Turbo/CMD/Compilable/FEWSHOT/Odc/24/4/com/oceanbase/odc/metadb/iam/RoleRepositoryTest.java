package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity(), new RoleEntity());
        
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);
        
        assertEquals(roleEntities, roleRepository.findByIdIn(ids));
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity());
        
        Mockito.when(roleRepository.findByName(name)).thenReturn(roleEntities);
        
        assertEquals(roleEntities, roleRepository.findByName(name));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> roleEntities = Mockito.mock(Page.class);
        
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(roleEntities);
        
        assertNotNull(roleRepository.findByOrganizationId(organizationId, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "User";
        Long organizationId = 2L;
        RoleEntity roleEntity = new RoleEntity();
        
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));
        
        assertTrue(roleRepository.findByNameAndOrganizationId(name, organizationId).isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 2L;
        List<String> names = Arrays.asList("Admin", "User");
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);
        
        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(organizationId, names));
    }

    // Add more test cases for other methods as needed

}