package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(Mockito.any()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByIdIn(Arrays.asList(1L, 2L)));
        verify(roleRepository, times(1)).findByIdIn(Arrays.asList(1L, 2L));
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("testRole");
        when(roleRepository.findByName(Mockito.any()))
                .thenReturn(Collections.singletonList(roleEntity));

        assertEquals(roleEntity, roleRepository.findByName("testRole"));
        verify(roleRepository, times(1)).findByName("testRole");
    }

    @Test
    public void testFindByOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        Page<RoleEntity> expectedPage = new PageImpl<>(Arrays.asList(roleEntity), Mockito.mock(Pageable.class), 1);

        when(roleRepository.findByOrganizationId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(expectedPage);

        assertEquals(expectedPage, roleRepository.findByOrganizationId(1L, Mockito.mock(Pageable.class)));
        verify(roleRepository, times(1)).findByOrganizationId(1L, Mockito.mock(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(Optional.of(roleEntity));

        assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId("testRole", 1L).get());
        verify(roleRepository, times(1)).findByNameAndOrganizationId("testRole", 1L);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(Mockito.anyLong(), Mockito.any()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2")));
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(1L, Arrays.asList("testRole1", "testRole2"));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleEntity roleEntity = new RoleEntity();
        Page<RoleEntity> expectedPage = new PageImpl<>(Collections.singletonList(roleEntity), Mockito.mock(Pageable.class), 1);

        when(roleRepository.findByOrganizationIdAndRoleIds(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(expectedPage);

        assertEquals(expectedPage, roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L), Mockito.mock(Pageable.class)));
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L), Mockito.mock(Pageable.class));
    }

    @Test
    public void testFindByUserId() {
        RoleEntity roleEntity = new RoleEntity();
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(roleRepository.findByUserId(Mockito.anyLong()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserId(1L));
        verify(roleRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        RoleEntity roleEntity = new RoleEntity();
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(roleRepository.findByUserIdAndEnabled(Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(1L, true));
        verify(roleRepository, times(1)).findByUserIdAndEnabled(1L, true);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        RoleEntity roleEntity = new RoleEntity();
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(roleRepository.findByRoleIdsAndEnabled(Mockito.any(), Mockito.anyBoolean()))
                .thenReturn(roleEntities);

        assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true));
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(Arrays.asList(1L), true);
    }

    @Test
    public void testUpdate() {
        doNothing().when(roleRepository).update(Mockito.any());

        roleRepository.update(roleEntity);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}