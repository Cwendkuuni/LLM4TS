
package com.oceanbase.odc.metadb.iam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleRepositoryTest {
    
    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    
    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L)))
            .thenReturn(Arrays.asList(new RoleEntity(1L), new RoleEntity(2L)));
        
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("testRole"))
            .thenReturn(Arrays.asList(new RoleEntity("testRole")));
        
        List<RoleEntity> roleEntities = roleRepository.findByName("testRole");
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        PageRequest pageRequest = new PageRequest(0, 10);
        when(roleRepository.findByOrganizationId(1L, pageRequest))
            .thenReturn(Mockito.mock(Page.class));
        
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageRequest);
        assertEquals(Mockito.mock(Page.class), result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId("testRole", 1L))
            .thenReturn(Optional.of(new RoleEntity("testRole")));
        
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("testRole", 1L);
        assertEquals("testRole", roleEntity.get().getName());
    }

    // Continue writing tests for the rest of the methods in a similar manner...
}
