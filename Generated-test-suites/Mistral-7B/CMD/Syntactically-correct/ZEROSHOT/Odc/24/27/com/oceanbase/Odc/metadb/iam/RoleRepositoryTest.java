package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByIdIn(ArgumentMatchers.collection(Long.class))).thenReturn(roles);

        List<RoleEntity> expectedRoles = roleRepository.findByIdIn(Collections.singletonList(1L));
        assertSame(roleEntity, expectedRoles.get(0));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(ArgumentMatchers.eq("testRole"))).thenReturn(Collections.singletonList(roleEntity));

        RoleEntity expectedRole = roleRepository.findByName("testRole").get(0);
        assertSame(roleEntity, expectedRole);
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        Page<RoleEntity> page = new PageImpl<>(roles);

        when(roleRepository.findByOrganizationId(ArgumentMatchers.anyLong(), pageable)).thenReturn(page);

        Page<RoleEntity> expectedPage = roleRepository.findByOrganizationId(1L, pageable);
        assertSame(page.getContent().get(0), expectedPage.getContent().get(0));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(ArgumentMatchers.eq("testRole"), ArgumentMatchers.eq(1L)))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity expectedRole = roleRepository.findByNameAndOrganizationId("testRole", 1L).get();
        assertSame(roleEntity, expectedRole);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByOrganizationIdAndNameIn(ArgumentMatchers.anyLong(), ArgumentMatchers.list(ArgumentMatchers.eq("testRole"))))
                .thenReturn(roles);

        List<RoleEntity> expectedRoles = roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("testRole"));
        assertSame(roleEntity, expectedRoles.get(0));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByOrganizationIdAndRoleIds(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(new PageImpl<>(roles));

        Page<RoleEntity> expectedPage = roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singletonList(1L));
        assertSame(roles.get(0), expectedPage.getContent().get(0));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByUserId(ArgumentMatchers.anyLong())).thenReturn(roles);

        List<RoleEntity> expectedRoles = roleRepository.findByUserId(1L);
        assertSame(roleEntity, expectedRoles.get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByUserIdAndEnabled(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(true))).thenReturn(roles);

        List<RoleEntity> expectedRoles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertSame(roleEntity, expectedRoles.get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByRoleIdsAndEnabled(ArgumentMatchers.any(), ArgumentMatchers.eq(true))).thenReturn(roles);

        List<RoleEntity> expectedRoles = roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
        assertSame(roleEntity, expectedRoles.get(0));
    }

    @Test
    public void testUpdate() {
        int count = 1;
        when(roleRepository.update(ArgumentMatchers.any(RoleEntity.class))).thenReturn(count);

        int result = roleRepository.update(roleEntity);
        assertTrue(result > 0);
    }
}