package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");
        roleEntity.setOrganizationId(1L);
        roleEntity.setIsEnabled(true);
        roleEntity.setDescription("test description");
    }

    @Test
    public void testFindById() {
        RoleEntity expectedRole = new RoleEntity();
        expectedRole.setId(1L);
        expectedRole.setName("testRole");
        expectedRole.setOrganizationId(1L);
        expectedRole.setIsEnabled(true);
        expectedRole.setDescription("test description");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(expectedRole));

        RoleEntity actualRole = roleRepository.findById(1L).orElse(null);

        assertEquals(expectedRole, actualRole);
    }

    @Test
    public void testFindByIdNotFound() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertNull(roleRepository.findById(2L).orElse(null));
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findAll();

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity expectedRole = new RoleEntity();
        expectedRole.setId(1L);
        expectedRole.setName(name);
        expectedRole.setOrganizationId(1L);
        expectedRole.setIsEnabled(true);
        expectedRole.setDescription("test description");

        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedRole));

        RoleEntity actualRole = roleRepository.findByName(name).get(0);

        assertEquals(expectedRole, actualRole);
    }

    @Test
    public void testFindByNameNotFound() {
        String name = "nonExistentRole";

        when(roleRepository.findByName(name)).thenReturn(Collections.emptyList());

        assertNull(roleRepository.findByName(name).orElse(null));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByOrganizationId(organizationId)).thenReturn(pageWithContent(expectedRoles));

        Page<RoleEntity> actualRoles = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());

        assertEquals(expectedRoles, actualRoles.getContent());
    }

    @Test
    public void testFindByOrganizationIdEmpty() {
        Long organizationId = 1L;

        when(roleRepository.findByOrganizationId(organizationId)).thenReturn(Page.empty());

        assertNull(roleRepository.findByOrganizationId(organizationId, Pageable.unpaged()).getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedRole = new RoleEntity();
        expectedRole.setId(1L);
        expectedRole.setName(name);
        expectedRole.setOrganizationId(organizationId);
        expectedRole.setIsEnabled(true);
        expectedRole.setDescription("test description");

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRole));

        RoleEntity actualRole = roleRepository.findByNameAndOrganizationId(name, organizationId).orElse(null);

        assertEquals(expectedRole, actualRole);
    }

    @Test
    public void testFindByNameAndOrganizationIdNotFound() {
        String name = "nonExistentRole";
        Long organizationId = 1L;

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.empty());

        assertNull(roleRepository.findByNameAndOrganizationId(name, organizationId).orElse(null));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Collections.singletonList("testRole");
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIdsAndEnabled() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled))
                .thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds,
                enabled);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        Boolean enabled = true;

        List<RoleEntity> expectedRoles = new ArrayList<>();
        expectedRoles.add(roleEntity);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRole = new RoleEntity();
        updatedRole.setId(1L);
        updatedRole.setName("updatedRole");
        updatedRole.setOrganizationId(1L);
        updatedRole.setIsEnabled(true);
        updatedRole.setDescription("updated description");

        int rowsAffected = 1;

        when(roleRepository.update(updatedRole)).thenReturn(rowsAffected);

        int actualRowsAffected = roleRepository.update(updatedRole);

        assertEquals(rowsAffected, actualRowsAffected);
    }

    private Page<RoleEntity> pageWithContent(List<RoleEntity> content) {
        return new PageImpl<>(content);
    }
}