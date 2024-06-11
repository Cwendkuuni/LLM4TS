package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;

public class RoleRepositoryTest {

    @Mock
    private RoleEntity roleEntity;

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        RoleEntity expectedRoleEntity = new RoleEntity();
        when(roleRepository.findById(id)).thenReturn(Optional.of(expectedRoleEntity));

        RoleEntity actualRoleEntity = roleRepository.findById(id).get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    public void testFindById_whenRoleNotFound_thenReturnEmptyOptional() {
        Long id = 1L;
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        Optional<RoleEntity> actualRoleEntityOptional = roleRepository.findById(id);

        assertFalse(actualRoleEntityOptional.isPresent());
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity);
        when(roleRepository.findByName(name)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByName(name);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName_whenMultipleRolesWithSameName_thenReturnAll() {
        String name = "admin";
        List<RoleEntity> roles1 = Arrays.asList(roleEntity, roleEntity);
        when(roleRepository.findByName(name)).thenReturn(roles1);

        List<RoleEntity> actualRoles = roleRepository.findByName(name);

        assertEquals(roles1, actualRoles);
    }

    @Test
    public void testFindByName_whenNoRoleFound_thenReturnEmptyList() {
        String name = "non-existent-role";
        when(roleRepository.findByName(name)).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoles = roleRepository.findByName(name);

        assertEquals(Collections.emptyList(), actualRoles);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity);
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(expectedPage.getContent(), actualPage.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "admin";
        Long organizationId = 1L;
        RoleEntity expectedRoleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRoleEntity));

        RoleEntity actualRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId).get();

        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    public void testFindByNameAndOrganizationId_whenRoleNotFound_thenReturnEmptyOptional() {
        String name = "admin";
        Long organizationId = 1L;
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.empty());

        Optional<RoleEntity> actualRoleEntityOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertFalse(actualRoleEntityOptional.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("admin", "user");
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIdsAndEnabled() {
        Long organizationId = 1L;
        Boolean enabled = true;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
        when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIdsAndEnabled_whenNoRolesFound_thenReturnEmptyList() {
        Long organizationId = 1L;
        Boolean enabled = true;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(roleIds, enabled)).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(Collections.emptyList(), actualRoles);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
        when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserId_whenNoRolesFoundForUserId_thenReturnEmptyList() {
        Long userId = 1L;
        when(roleRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

        assertEquals(Collections.emptyList(), actualRoles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserIdAndEnabled_whenNoRolesFoundForUserIdAndEnabled_thenReturnEmptyList() {
        Long userId = 1L;
        Boolean enabled = true;
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(Collections.emptyList(), actualRoles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled_whenNoRolesFound_thenReturnEmptyList() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(Collections.emptyList());

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(Collections.emptyList(), actualRoles);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntityToUpdate = new RoleEntity();
        when(roleRepository.update(roleEntityToUpdate)).thenReturn(1);

        int actualResult = roleRepository.update(roleEntityToUpdate);

        assertEquals(1, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_whenNullRoleEntity_thenThrowsIllegalArgumentException() {
        when(roleRepository.update(null)).thenCallRealMethod();

        roleRepository.update(null);
    }
}