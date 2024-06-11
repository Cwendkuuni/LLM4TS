package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByIdIn(Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Mockito.when(roleRepository.findByName("test")).thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByName("test"));
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(1L, Pageable.unpaged())).thenReturn(page);

        assertEquals(page, roleRepository.findByOrganizationId(1L, Pageable.unpaged()));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);
        Mockito.when(roleRepository.findByNameAndOrganizationId("test", 1L)).thenReturn(optionalRoleEntity);

        assertTrue(roleRepository.findByNameAndOrganizationId("test", 1L).isPresent());
        assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId("test", 1L).get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("test"))).thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("test")));
    }

    // Add remaining test methods for other queries in the RoleRepository class

}