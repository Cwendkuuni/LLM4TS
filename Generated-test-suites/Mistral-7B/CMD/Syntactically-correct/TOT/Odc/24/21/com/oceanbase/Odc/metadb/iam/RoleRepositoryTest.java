package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private ArgumentCaptor<RoleEntity> roleEntityArgumentCaptor;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
        roleEntityArgumentCaptor = ArgumentCaptor.forClass(RoleEntity.class);
    }

    @Test
    public void testFindByIdIn() {
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        List<Long> ids = List.of(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> expectedRoles = List.of(roleEntity1, roleEntity2);

        when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);

        when(roleRepository.findByName(name)).thenReturn(List.of(roleEntity));

        List<RoleEntity> actualRoles = roleRepository.findByName(name);

        assertEquals(1, actualRoles.size());
        assertEquals(name, actualRoles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        RoleEntity roleEntity = new RoleEntity();
        List<RoleEntity> expectedRoles = List.of(roleEntity);
        Page<RoleEntity> expectedPage = Page.of(expectedRoles, pageable);

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(expectedPage.getContent(), actualPage.getContent());
        assertEquals(expectedPage.getSize(), actualPage.getSize());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> actualRoleOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(actualRoleOptional.isPresent());
        assertEquals(roleEntity, actualRoleOptional.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("testRole1", "testRole2");
        List<RoleEntity> expectedRoles = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName("testRole1");
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setName("testRole2");
        expectedRoles.add(roleEntity1);
        expectedRoles.add(roleEntity2);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        List<RoleEntity> expectedRoles = List.of(roleEntity1, roleEntity2);
        Pageable pageable = Pageable.ofSize(10);

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(Page.empty());

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertTrue(actualPage.isEmpty());

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(Page.of(expectedRoles, pageable));

        Page<RoleEntity> actualPage2 = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(expectedRoles, actualPage2.getContent());
        assertEquals(pageable.getSize(), actualPage2.getSize());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        RoleEntity roleEntity = new RoleEntity();

        when(roleRepository.findByUserId(userId)).thenReturn(List.of(roleEntity));

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

        assertEquals(1, actualRoles.size());
        assertEquals(roleEntity, actualRoles.get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        RoleEntity roleEntity = new RoleEntity();

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(List.of(roleEntity));

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(1, actualRoles.size());
        assertEquals(roleEntity, actualRoles.get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        List<RoleEntity> expectedRoles = List.of(roleEntity1, roleEntity2);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();

        roleRepository.update(roleEntity);

        ArgumentCaptor<RoleEntity> roleEntityArgumentCaptor = this.roleEntityArgumentCaptor;
        verify(roleRepository).update(roleEntityArgumentCaptor.capture());

        RoleEntity capturedRoleEntity = roleEntityArgumentCaptor.getValue();
        assertEquals(roleEntity, capturedRoleEntity);
    }
}