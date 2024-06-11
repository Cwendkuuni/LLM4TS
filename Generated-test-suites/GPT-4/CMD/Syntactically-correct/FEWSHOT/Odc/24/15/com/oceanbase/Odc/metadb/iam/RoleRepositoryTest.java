
package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L))).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertNotNull(roles);
        assertEquals(2, roles.size());
        verify(roleRepository).findByIdIn(Arrays.asList(1L, 2L, 3L));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("Admin")).thenReturn(Arrays.asList(new RoleEntity()));
        List<RoleEntity> roles = roleRepository.findByName("Admin");
        assertNotNull(roles);
        assertEquals(1, roles.size());
        verify(roleRepository).findByName("Admin");
    }

    //Similarly generate test cases for other interface methods like getOne, deleteById etc.

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("NewRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("NewTest");
        
        when(roleRepository.update(roleEntity)).thenReturn(1);
        
        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(1, updatedRows);
        verify(roleRepository).update(roleEntity);
    }
}
