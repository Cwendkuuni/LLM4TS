package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    // Test findByIdIn
    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);

        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(id1);
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(id2);
        expectedResult.add(roleEntity1);
        expectedResult.add(roleEntity2);

        List<RoleEntity> actualResult = roleRepository.findByIdIn(ids);

        assertEquals(expectedResult, actualResult);
    }

    // Test findByName
    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);

        RoleEntity expectedResult = roleEntity;

        RoleEntity actualResult = roleRepository.findByName(name).get();

        assertEquals(expectedResult, actualResult);
    }

    // Test findByOrganizationId
    @Test
    public void testFindByOrganizationId(Pageable pageable) {
        Long organizationId = 1L;
        Page<RoleEntity> expectedResult = Page.empty();
        Page<RoleEntity> actualResult = roleRepository.findByOrganizationId(organizationId, pageable);

        // Test empty result
        assertEquals(expectedResult, actualResult);

        RoleEntity roleEntity1 = new RoleEntity();
        RoleEntity roleEntity2 = new RoleEntity();
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);

        expectedResult = Page.of(roles, pageable);

        // Test non-empty result
        List<RoleEntity> rolesInRepo = new ArrayList<>();
        rolesInRepo.add(roleEntity1);
        rolesInRepo.add(roleEntity2);

        when(roleRepository.findAll(eq(Specification.where( RoleSpecifications.organizationIdEq(organizationId) )))).thenReturn(rolesInRepo);

        actualResult = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(expectedResult, actualResult);
    }

    // Test findByNameAndOrganizationId
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        Optional<RoleEntity> expectedResult = Optional.of(roleEntity);

        RoleEntity actualResult = roleRepository.findByNameAndOrganizationId(name, organizationId).get();

        assertEquals(expectedResult.get(), actualResult);
    }

    // Test findByOrganizationIdAndNameIn
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        String name1 = "testRole1";
        String name2 = "testRole2";
        List<String> names = new ArrayList<>();
        names.add(name1);
        names.add(name2);
        List<RoleEntity> expectedResult = new ArrayList<>();

        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName(name1);
        roleEntity1.setOrganizationId(organizationId);
        expectedResult.add(roleEntity1);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setName(name2);
        roleEntity2.setOrganizationId(organizationId);
        expectedResult.add(roleEntity2);

        List<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(expectedResult, actualResult);
    }

    // Test findByOrganizationIdAndRoleIds
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);

        Page<RoleEntity> expectedResult = Page.empty();
        Page<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());

        // Test empty result
        assertEquals(expectedResult, actualResult);

        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(roleIds.iterator().next());
        roleEntity1.setOrganizationId(organizationId);
        roles.add(roleEntity1);

        expectedResult = Page.of(roles, Pageable.unpaged());

        // Test non-empty result
        when(roleRepository.findAll(eq(Specification.where( RoleSpecifications.organizationIdEq(organizationId) ).and(RoleSpecifications.idIn(roleIds) )))).thenReturn(roles);

        actualResult = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());

        assertEquals(expectedResult, actualResult);
    }

    // Test findByUserId
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setUserId(userId);
        expectedResult.add(roleEntity);

        List<RoleEntity> actualResult = roleRepository.findByUserId(userId);

        assertEquals(expectedResult, actualResult);
    }

    // Test findByUserIdAndEnabled
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setUserId(userId);
        roleEntity.setEnabled(enabled);
        expectedResult.add(roleEntity);

        List<RoleEntity> actualResult = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(expectedResult, actualResult);
    }

    // Test findByRoleIdsAndEnabled
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        Boolean enabled = true;
        List<RoleEntity> expectedResult = new ArrayList<>();

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(roleIds.iterator().next());
        roleEntity.setEnabled(enabled);
        expectedResult.add(roleEntity);

        List<RoleEntity> actualResult = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expectedResult, actualResult);
    }

    // Test update
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("testDescription");

        int expectedNumAffectedRows = 1;

        roleRepository.save(roleEntity);

        int actualNumAffectedRows = roleRepository.update(roleEntity);

        assertEquals(expectedNumAffectedRows, actualNumAffectedRows);
    }
}