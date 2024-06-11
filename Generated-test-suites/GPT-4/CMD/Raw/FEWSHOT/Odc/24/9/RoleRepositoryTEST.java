package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Test
    public void testFindByName() {
        List<RoleEntity> expected = Arrays.asList(roleEntity);
        when(roleRepository.findByName("Test")).thenReturn(expected);

        List<RoleEntity> roles = roleRepository.findByName("Test");

        assertFalse(roles.isEmpty());
        assertEquals("Test", roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> expected = Arrays.asList(roleEntity);
        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test"))).thenReturn(expected);

        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test"));

        assertFalse(roles.isEmpty());
        assertEquals("Test", roles.get(0).getName());
        assertEquals(1L, roles.get(0).getOrganizationId().longValue());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> expected = Arrays.asList(roleEntity);
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(expected);

        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);

        assertFalse(roles.isEmpty());
        assertEquals("Test", roles.get(0).getName());
        assertTrue(roles.get(0).getEnabled());
    }

    @Test
    public void testUpdate() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName("Update Test");
        entity.setEnabled(true);
        entity.setDescription("Update test description");
        
        when(roleRepository.update(entity)).thenReturn(1);
        int updateCount = roleRepository.update(entity);

        assertEquals(1, updateCount);
    }

    // Add more tests to ensure full coverage of RoleRepository...
    
}