package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedRoles = mock(List.class);
        when(roleRepository.findByIdIn(Collections.singletonList(id1))).thenReturn(Optional.of(new RoleEntity()));
        when(roleRepository.findByIdIn(Collections.singletonList(id2))).thenReturn(Optional.of(new RoleEntity()));
        when(roleRepository.findByIdIn(new HashSet<>(Arrays.asList(id1, id2)))).thenReturn(Optional.of(expectedRoles));

        List<RoleEntity> roles = roleRepository.findByIdIn(new HashSet<>(Arrays.asList(id1, id2)));
        assertEquals(2, roles.size());
        verify(roleRepository, times(3)).findByIdIn(anyCollection());
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Optional.of(roleEntity));

        RoleEntity role = roleRepository.findByName(name).get();
        assertEquals(name, role.getName());
        verify(roleRepository, times(1)).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        Page<RoleEntity> expectedPage = mock(Page.class);
        when(roleRepository.findAll(eq(Pageable.unpaged()))).thenReturn(expectedPage);
        when(expectedPage.getContent()).thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
        assertEquals(2, roles.getContent().size());
        verify(roleRepository, times(1)).findAll(eq(Pageable.unpaged()));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        RoleEntity role = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(name, role.getName());
        assertEquals(organizationId, role.getOrganizationId());
        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        String name1 = "testRole1";
        String name2 = "testRole2";
        List<RoleEntity> expectedRoles = mock(List.class);
        when(roleRepository.findAll(any(Pageable.class))).thenReturn(mock(Page.class));
        when(roleRepository.findAll(any(Pageable.class)).getContent()).thenReturn(expectedRoles);

        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, Arrays.asList(name1, name2));
        assertEquals(2, roles.size());
        verify(roleRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Long roleId1 = 1L;
        Long roleId2 = 2L;
        List<RoleEntity> expectedRoles = mock(List.class);
        when(roleRepository.findAll(any(Pageable.class))).thenReturn(mock(Page.class));
        when(roleRepository.findAll(any(Pageable.class)).getContent()).thenReturn(expectedRoles);
        when(roleRepository.findByOrganizationId(organizationId)).thenReturn(Optional.of(new RoleEntity()));

        Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, Arrays.asList(roleId1, roleId2), Pageable.unpaged());
        assertEquals(1, roles.getContent().size());
        assertEquals(roleId1, roles.getContent().get(0).getId());
        verify(roleRepository, times(1)).findAll(any(Pageable.class));
        verify(roleRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        when(roleRepository.findAll(eq(Pageable.unpaged()))).thenReturn(mock(Page.class));
        when(roleRepository.findAll(eq(Pageable.unpaged())).getContent()).thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> roles = roleRepository.findByUserId(userId);
        assertEquals(2, roles.size());
        verify(roleRepository, times(1)).findAll(eq(Pageable.unpaged()));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        when(roleRepository.findAll(eq(Pageable.unpaged()))).thenReturn(mock(Page.class));
        when(roleRepository.findAll(eq(Pageable.unpaged())).getContent()).thenReturn(Arrays.asList(roleEntity1, roleEntity2));

        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(1, roles.size());
        assertEquals(enabled, roles.get(0).isEnabled());
        verify(roleRepository, times(1)).findAll(eq(Pageable.unpaged()));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Boolean enabled = true;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoles = mock(List.class);
        when(roleRepository.findAll(any(Pageable.class))).thenReturn(mock(Page.class));
        when(roleRepository.findAll(any(Pageable.class)).getContent()).thenReturn(expectedRoles);

        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(2, roles.size());
        verify(roleRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        String name = "newName";
        Boolean enabled = true;
        RoleEntity roleEntity = new RoleEntity();

        roleRepository.update(roleEntity);

        verify(roleRepository, times(1)).update(any(RoleEntity.class));
    }
}