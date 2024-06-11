package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {
    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");
        roleEntity.setOrganizationId(1L);
        roleEntity.setDescription("desc1");
        roleEntity.setEnabled(true);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId())))
                .thenReturn(expectedRoles);

        assertEquals(expectedRoles, roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId())));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(roleEntity.getName()))
                .thenReturn(Collections.singletonList(roleEntity));

        assertEquals(roleEntity, roleRepository.findByName(roleEntity.getName()).get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = Pageable.ofSize(1, Sort.by(Sort.Direction.ASC, "id"));

        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);

        when(roleRepository.findByOrganizationId(roleEntity.getOrganizationId(), pageable))
                .thenReturn(expectedPage);

        assertEquals(expectedPage, roleRepository.findByOrganizationId(roleEntity.getOrganizationId(), pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> expectedRoleOptional = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId(roleEntity.getName(), roleEntity.getOrganizationId()))
                .thenReturn(expectedRoleOptional);

        assertTrue(expectedRoleOptional.isPresent());
        assertEquals(roleEntity, expectedRoleOptional.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByOrganizationIdAndNameIn(roleEntity.getOrganizationId(), Collections.singletonList(roleEntity.getName())))
                .thenReturn(expectedRoles);

        assertEquals(expectedRoles, roleRepository.findByOrganizationIdAndNameIn(roleEntity.getOrganizationId(), Collections.singletonList(roleEntity.getName())));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(), Collections.singletonList(roleEntity.getId())))
                .thenReturn(expectedRoles);

        assertEquals(expectedRoles, roleRepository.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(), Collections.singletonList(roleEntity.getId())));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByUserId(roleEntity.getId()))
                .thenReturn(expectedRoles);

        assertEquals(expectedRoles, roleRepository.findByUserId(roleEntity.getId()));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByUserIdAndEnabled(roleEntity.getId(), true))
                .thenReturn(expectedRoles);

        assertEquals(expectedRoles, roleRepository.findByUserIdAndEnabled(roleEntity.getId(), true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(roleEntity.getId()), true))
                .thenReturn(expectedRoles);

        assertEquals(expectedRoles, roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(roleEntity.getId()), true));
    }

    @Test
    public void testUpdate() {
        int expectedRowsAffected = 1;

        when(roleRepository.update(roleEntity)).thenReturn(expectedRowsAffected);

        assertEquals(expectedRowsAffected, roleRepository.update(roleEntity));
    }
}