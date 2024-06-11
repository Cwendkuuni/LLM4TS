package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private PageRequest pageable;

    @Test
    public void testFindByIdIn() {
        roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    public void testFindByName() {
        roleRepository.findByName("Test");
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByName("Test"));
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository.findByOrganizationId(1L, pageable);
        assertNotNull(roleRepository.findByOrganizationId(1L, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository.findByNameAndOrganizationId("Test", 1L);
        assertEquals(Optional.of(roleEntity), roleRepository.findByNameAndOrganizationId("Test", 1L));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test", "Test2"));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test", "Test2")));
    }

    @Test
    public void testFindByUserId() {
        roleRepository.findByUserId(1L);
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByUserId(1L));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByUserIdAndEnabled(1L, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true));
    }

    @Test
    public void testUpdate() {
        when(roleEntity.getName()).thenReturn("Test");
        when(roleEntity.getDescription()).thenReturn("Test Desc");
        when(roleEntity.getId()).thenReturn(1L);
        when(roleEntity.isEnabled()).thenReturn(true);
        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}