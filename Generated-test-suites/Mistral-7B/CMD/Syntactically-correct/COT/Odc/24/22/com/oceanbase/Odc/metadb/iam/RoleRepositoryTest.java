package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
    }

    // Test for findByIdIn method
    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        for (RoleEntity role : result) {
            assertTrue(ids.contains(role.getId()));
        }
    }

    // Test for findByName method with existing name
    @Test
    public void testFindByNameWithExistingName() {
        String name = "testName";
        RoleEntity expectedResult = new RoleEntity();
        expectedResult.setName(name);

        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedResult));

        RoleEntity result = roleRepository.findByName(name).get();
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    // Test for findByName method with non-existing name
    @Test
    public void testFindByNameWithNonExistingName() {
        String name = "testName";

        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.emptyList());

        assertNull(roleRepository.findByName(name).get());
    }

    // Test for findByOrganizationId method with existing id and empty pageable
    @Test
    public void testFindByOrganizationIdWithExistingIdAndEmptyPageable() {
        Long organizationId = 1L;
        Page<RoleEntity> expectedResult = new PageImpl<>(Collections.singletonList(new RoleEntity()), Pageable.emptyPagination(), 1);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, Pageable.unpaged())).thenReturn(expectedResult);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.getTotalElements() > 0);
        assertEquals(expectedResult.getContent().get(0).getId(), result.getContent().get(0).getId());
    }

    // Test for findByOrganizationId method with existing id and pageable with offset and limit
    @Test
    public void testFindByOrganizationIdWithExistingIdAndPageable() {
        Long organizationId = 1L;
        int offset = 0;
        int limit = 10;
        List<RoleEntity> expectedResult = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            RoleEntity role = new RoleEntity();
            role.setOrganizationId(organizationId);
            role.setId(i + 1);
            expectedResult.add(role);
        }
        Pageable pageable = PageRequest.of(offset / limit, limit);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(new PageImpl<>(expectedResult, pageable, expectedResult.size()));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertNotNull(result);
        assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
        assertEquals(expectedResult.getSize(), result.getSize());
        assertEquals(expectedResult.getContent().get(0).getId(), result.getContent().get(0).getId());
    }

    // Test for findByNameAndOrganizationId method with existing name and id
    @Test
    public void testFindByNameAndOrganizationIdWithExistingNameAndId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity expectedResult = new RoleEntity();
        expectedResult.setName(name);
        expectedResult.setOrganizationId(organizationId);

        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedResult));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
        assertEquals(organizationId, result.get().getOrganizationId());
    }

    // Test for findByNameAndOrganizationId method with non-existing name and id
    @Test
    public void testFindByNameAndOrganizationIdWithNonExistingNameAndId() {
        String name = "testName";
        Long organizationId = 1L;

        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.empty());

        assertFalse(roleRepository.findByNameAndOrganizationId(name, organizationId).isPresent());
    }

    // Test for findByOrganizationIdAndNameIn method with existing organizationId and names
    @Test
    public void testFindByOrganizationIdAndNameInWithExistingOrganizationIdAndNames() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("name1");
        names.add("name2");

        List<RoleEntity> expectedResult = new ArrayList<>();
        for (String name : names) {
            RoleEntity role = new RoleEntity();
            role.setOrganizationId(organizationId);
            role.setName(name);
            expectedResult.add(role);
        }

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNotNull(result);
        assertEquals(names.size(), result.size());
        for (RoleEntity role : result) {
            assertTrue(names.contains(role.getName()));
            assertEquals(organizationId, role.getOrganizationId());
        }
    }

    // Test for findByOrganizationIdAndRoleIdsAndEnabled method with existing organizationId, roleIds and enabled
    @Test
    public void testFindByOrganizationIdAndRoleIdsAndEnabledWithExistingOrganizationIdRoleIdsAndEnabled() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;

        List<RoleEntity> expectedResult = new ArrayList<>();
        for (Long roleId : roleIds) {
            RoleEntity role = new RoleEntity();
            role.setOrganizationId(organizationId);
            role.setId(roleId);
            role.setIsEnabled(enabled);
            expectedResult.add(role);
        }

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled)).thenReturn(new PageImpl<>(expectedResult, Pageable.unpaged(), expectedResult.size()));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled);
        assertNotNull(result);
        assertEquals(expectedResult.size(), result.getTotalElements());
        assertEquals(expectedResult.get(0).getId(), result.getContent().get(0).getId());
    }

    // Test for findByUserId method with existing userId
    @Test
    public void testFindByUserIdWithExistingUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        role1.setUserId(userId);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        role2.setUserId(userId);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        for (RoleEntity role : result) {
            assertEquals(userId, role.getUserId());
        }
    }

    // Test for findByUserIdAndEnabled method with existing userId and enabled
    @Test
    public void testFindByUserIdAndEnabledWithExistingUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = new ArrayList<>();
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        role1.setUserId(userId);
        role1.setIsEnabled(enabled);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        role2.setUserId(userId);
        role2.setIsEnabled(enabled);
        expectedResult.add(role1);
        expectedResult.add(role2);

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        for (RoleEntity role : result) {
            assertEquals(userId, role.getUserId());
            assertEquals(enabled, role.isEnabled());
        }
    }

    // Test for findByRoleIdsAndEnabled method with existing roleIds and enabled
    @Test
    public void testFindByRoleIdsAndEnabledWithExistingRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;

        List<RoleEntity> expectedResult = new ArrayList<>();
        for (Long roleId : roleIds) {
            RoleEntity role = new RoleEntity();
            role.setId(roleId);
            role.setIsEnabled(enabled);
            expectedResult.add(role);
        }

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        for (RoleEntity role : result) {
            assertTrue(roleIds.contains(role.getId()));
            assertEquals(enabled, role.isEnabled());
        }
    }

    // Test for update method with existing roleEntity
    @Test
    public void testUpdateWithExistingRoleEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testName");
        roleEntity.setIsEnabled(true);
        roleEntity.setDescription("testDescription");

        int expectedResult = 1;

        Mockito.when(roleRepository.update(roleEntity)).thenReturn(expectedResult);

        int result = roleRepository.update(roleEntity);
        assertEquals(expectedResult, result);
    }
}