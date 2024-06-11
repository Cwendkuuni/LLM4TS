package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
        roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> expectedRoles = new CopyOnWriteArrayList<>();
        expectedRoles.add(roleEntity);
        when(roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId())))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId()));
        assertEquals(1, actualRoles.size());
        assertSame(roleEntity, actualRoles.get(0));
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity expectedRole = new RoleEntity();
        expectedRole.setName(name);
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedRole));

        RoleEntity actualRole = roleRepository.findByName(name).get(0);
        assertEquals(name, actualRole.getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedRoles = new CopyOnWriteArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setOrganizationId(organizationId);
        expectedRoles.add(role);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(1, actualPage.getTotalElements());
        assertSame(role, actualPage.getContent().get(0));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedRole = new RoleEntity();
        expectedRole.setName(name);
        expectedRole.setOrganizationId(organizationId);

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRole));

        RoleEntity actualRole = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(name, actualRole.getName());
        assertEquals(organizationId, actualRole.getOrganizationId());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("testRole1", "testRole2");
        List<RoleEntity> expectedRoles = new CopyOnWriteArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setName("testRole1");
        role1.setOrganizationId(organizationId);
        RoleEntity role2 = new RoleEntity();
        role2.setName("testRole2");
        role2.setOrganizationId(organizationId);
        expectedRoles.add(role1);
        expectedRoles.add(role2);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, actualRoles.size());
        assertTrue(actualRoles.containsAll(expectedRoles));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        List<RoleEntity> expectedRoles = new CopyOnWriteArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(roleIds.get(0));
        RoleEntity role2 = new RoleEntity();
        role2.setId(roleIds.get(1));
        expectedRoles.add(role1);
        expectedRoles.add(role2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(2, actualPage.getTotalElements());
        assertTrue(actualPage.getContent().containsAll(expectedRoles));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoles = new CopyOnWriteArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        expectedRoles.add(role);

        when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);
        assertEquals(1, actualRoles.size());
        assertSame(role, actualRoles.get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = new CopyOnWriteArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setEnabled(enabled);
        expectedRoles.add(role);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(1, actualRoles.size());
        assertTrue(actualRoles.get(0).isEnabled());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = new CopyOnWriteArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(roleIds.get(0));
        role1.setEnabled(enabled);
        RoleEntity role2 = new RoleEntity();
        role2.setId(roleIds.get(1));
        role2.setEnabled(enabled);
        expectedRoles.add(role1);
        expectedRoles.add(role2);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(2, actualRoles.size());
        assertTrue(actualRoles.stream().allMatch(RoleEntity::isEnabled));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.update(roleEntity)).thenReturn(1);

        int actualRowsAffected = roleRepository.update(roleEntity);
        assertEquals(1, actualRowsAffected);
    }
}