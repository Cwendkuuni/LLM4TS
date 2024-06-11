package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private Pageable pageable;

    @Test
    void testFindByIdIn() {
        roleRepository = mock(RoleRepository.class);
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(Collections.singletonList(id1)))
                .thenReturn(Optional.of(new RoleEntity()));
        when(roleRepository.findByIdIn(Arrays.asList(id1, id2)))
                .thenReturn(Optional.of(expectedRoles));

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(Arrays.asList(id1, id2));
        assertEquals(2, actualRoles.size());
        verify(roleRepository, times(1)).findByIdIn(Arrays.asList(id1, id2));
    }

    @Test
    void testFindByName() {
        roleRepository = mock(RoleRepository.class);
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> actualRoles = roleRepository.findByName(name);
        assertEquals(1, actualRoles.size());
        verify(roleRepository, times(1)).findByName(name);
    }

    @Test
    void testFindByOrganizationId() {
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        when(roleRepository.findByOrganizationId(organizationId, Mockito.any(Pageable.class)))
                .thenReturn(expectedPage);
        when(expectedPage.getContent()).thenReturn(Collections.singletonList(roleEntity));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(1, actualPage.getContent().size());
        verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

    @Test
    void testFindByNameAndOrganizationId() {
        roleRepository = mock(RoleRepository.class);
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity actualRole = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertNotNull(actualRole);
        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    void testFindByOrganizationIdAndNameIn() {
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, actualRoles.size());
        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    void testFindByOrganizationIdAndRoleIds() {
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        RoleEntity roleEntity = new RoleEntity();
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
                .thenReturn(expectedPage);
        when(expectedPage.getContent()).thenReturn(Collections.singletonList(roleEntity));

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(1, actualPage.getContent().size());
        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
    }

    @Test
    void testFindByUserId() {
        roleRepository = mock(RoleRepository.class);
        Long userId = 1L;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);
        assertEquals(2, actualRoles.size());
        verify(roleRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindByUserIdAndEnabled() {
        roleRepository = mock(RoleRepository.class);
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(userId, enabled))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(2, actualRoles.size());
        verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        roleRepository = mock(RoleRepository.class);
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(2, actualRoles.size());
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    void testUpdate() {
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = new RoleEntity();

        Executable updateRole = () -> roleRepository.save(roleEntity);
        doAnswer(invocation -> {
            RoleEntity savedRole = invocation.getArgument(0);
            savedRole.setId(1L);
            return savedRole;
        }).when(roleRepository).save(roleEntity);

        RoleEntity updatedRole = updateRole.execute();
        Assertions.assertEquals(1L, updatedRole.getId());
        verify(roleRepository, times(1)).save(roleEntity);
    }

    @Test
    @Transactional
    void testUpdateWithTransaction() {
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = new RoleEntity();

        doAnswer(invocation -> {
            RoleEntity savedRole = invocation.getArgument(0);
            savedRole.setId(1L);
            return savedRole;
        }).when(roleRepository).save(roleEntity);

        RoleEntity updatedRole = roleRepository.save(roleEntity);
        Assertions.assertEquals(1L, updatedRole.getId());
        verify(roleRepository, times(1)).save(roleEntity);
    }
}