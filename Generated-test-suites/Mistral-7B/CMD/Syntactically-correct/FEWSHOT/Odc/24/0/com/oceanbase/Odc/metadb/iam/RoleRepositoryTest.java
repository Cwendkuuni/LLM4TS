package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.SpecificationSupport;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(Optional.of(new RoleEntity()));
        when(roleRepository.findByIdIn(Collections.singletonList(2L))).thenReturn(Optional.of(new RoleEntity()));
        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, actualRoles.size());
        verify(roleRepository, times(2)).findById(anyLong());
    }

    @Test
    public void testFindByName() {
        String name = "test";
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Optional.of(expectedRole));

        RoleEntity actualRole = roleRepository.findByName(name);
        assertSame(expectedRole, actualRole);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);

        when(roleRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedRoles.size(), actualPage.getContent().size());
        verify(roleRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRole));

        RoleEntity actualRole = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertSame(expectedRole, actualRole);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("test1", "test2");
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(names.size(), actualRoles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        Pageable pageable = Mockito.mock(Pageable.class);

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(roleIds.size(), actualPage.getContent().size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);
        assertEquals(expectedRoles.size(), actualRoles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedRoles.size(), actualRoles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(roleIds.size(), actualRoles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testUpdate() {
        RoleEntity newRoleEntity = new RoleEntity();
        RoleEntity oldRoleEntity = new RoleEntity();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(oldRoleEntity));

        int actualRowsAffected = roleRepository.update(newRoleEntity);
        assertEquals(1, actualRowsAffected);
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).update(newRoleEntity);
    }
}