package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CollectionItemProvider;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE_ADMIN");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Role");
    }

    @Test
    void testFindById() {
        when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.ofNullable(roleEntity));

        RoleEntity actualRoleEntity = roleRepository.findById(roleEntity.getId()).orElse(null);

        assertEquals(roleEntity, actualRoleEntity);
    }

    @Test
    void testFindById_NotFound() {
        when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleRepository.findById(roleEntity.getId()).orElse(null));
    }

    @Test
    void testFindByIdIn() {
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("ROLE_USER");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(true);
        roleEntity2.setDescription("Test Role 2");

        List<RoleEntity> roleEntities = List.of(roleEntity, roleEntity2);

        when(roleRepository.findByIdIn(roleEntities.stream().map(RoleEntity::getId).collect(Collectors.toList()))).thenReturn(roleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId()));

        assertEquals(roleEntity, actualRoleEntities.get(0));
    }

    @Test
    void testFindByName() {
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> actualRoleEntities = roleRepository.findByName("ROLE_ADMIN");

        assertEquals(roleEntity, actualRoleEntities.get(0));
    }

    @Test
    void testFindByName_Empty() {
        when(roleRepository.findByName("ROLE_SUPER_ADMIN")).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoleEntities = roleRepository.findByName("ROLE_SUPER_ADMIN");

        assertTrue(actualRoleEntities.isEmpty());
    }

    @Test
    void testFindByOrganizationId() {
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("ROLE_USER");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(true);
        roleEntity2.setDescription("Test Role 2");

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        when(roleRepository.findByOrganizationId(roleEntity.getOrganizationId(), pageable)).thenReturn(Mockito.mock(Page.class));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(roleEntity.getOrganizationId(), pageable);

        verify(actualPage, times(1)).getContent();
    }

    @Test
    void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId("ROLE_ADMIN", 1L)).thenReturn(Optional.ofNullable(roleEntity));

        RoleEntity actualRoleEntity = roleRepository.findByNameAndOrganizationId("ROLE_ADMIN", 1L).orElse(null);

        assertEquals(roleEntity, actualRoleEntity);
    }

    @Test
    void testFindByNameAndOrganizationId_NotFound() {
        when(roleRepository.findByNameAndOrganizationId("ROLE_SUPER_ADMIN", 1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleRepository.findByNameAndOrganizationId("ROLE_SUPER_ADMIN", 1L).orElse(null));
    }

    @ParameterizedTest
    @CollectionItemProvider(name = "roleIds", value = {1L, 2L})
    void testFindByOrganizationIdAndRoleIds(Long roleId) {
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("ROLE_USER");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(true);
        roleEntity2.setDescription("Test Role 2");

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        when(roleRepository.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(), List.of(roleId), pageable)).thenReturn(Mockito.mock(Page.class));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(), List.of(roleId), pageable);

        verify(actualPage, times(1)).getContent();
    }

    @Test
    void testFindByUserId() {
        when(roleRepository.findByUserId(1L)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserId(1L);

        assertEquals(roleEntity, actualRoleEntities.get(0));
    }

    @Test
    void testFindByUserId_Empty() {
        when(roleRepository.findByUserId(2L)).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserId(2L);

        assertTrue(actualRoleEntities.isEmpty());
    }

    @Test
    void testFindByUserIdAndEnabled() {
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("ROLE_USER");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(false);
        roleEntity2.setDescription("Test Role 2");

        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(1L, true);

        assertEquals(roleEntity, actualRoleEntities.get(0));
    }

    @Test
    void testFindByUserIdAndEnabled_Empty() {
        when(roleRepository.findByUserIdAndEnabled(1L, false)).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(1L, false);

        assertTrue(actualRoleEntities.isEmpty());
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("ROLE_USER");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(false);
        roleEntity2.setDescription("Test Role 2");

        when(roleRepository.findByRoleIdsAndEnabled(List.of(roleEntity.getId()), true)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(List.of(roleEntity.getId()), true);

        assertEquals(roleEntity, actualRoleEntities.get(0));
    }

    @Test
    void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setId(roleEntity.getId());
        updatedRoleEntity.setName("UPDATED_ROLE_NAME");
        updatedRoleEntity.setEnabled(false);
        updatedRoleEntity.setDescription("Updated Role Description");

        doAnswer(i -> {
            RoleEntity roleEntityToUpdate = i.getArgument(0, RoleEntity.class);
            roleEntityToUpdate.setId(roleEntity.getId());
            roleEntityToUpdate.setName("UPDATED_ROLE_NAME");
            roleEntityToUpdate.setEnabled(false);
            roleEntityToUpdate.setDescription("Updated Role Description");
            roleEntityToUpdate.setUserUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

            return 1;
        }).when(roleRepository).update(updatedRoleEntity);

        int actualUpdateCount = roleRepository.update(updatedRoleEntity);

        assertEquals(1, actualUpdateCount);
    }

    @Test
    void testUpdate_ThrowsException() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setId(-1L);
        updatedRoleEntity.setName("UPDATED_ROLE_NAME");
        updatedRoleEntity.setEnabled(false);
        updatedRoleEntity.setDescription("Updated Role Description");

        assertThrows(IllegalArgumentException.class, () -> roleRepository.update(updatedRoleEntity));
    }
}