package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for findByIdIn method
    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity(), new RoleEntity());
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIdInEmptyList() {
        List<Long> ids = Collections.emptyList();
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for findByName method
    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity expectedResult = new RoleEntity();
        RoleEntity result = roleRepository.findByName(name).orElse(null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByNameNull() {
        String name = null;
        RoleEntity result = roleRepository.findByName(name);
        assertNull(result);
    }

    // Test for findByOrganizationId method
    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = MockitoAnnotations.mock(Pageable.class);
        Page<RoleEntity> expectedResult = MockitoAnnotations.mock(Page.class);
        List<RoleEntity> content = Arrays.asList(new RoleEntity(), new RoleEntity());
        expectedResult.setContent(content);
        roleRepository.findByOrganizationId(organizationId, pageable);
        MockitoAnnotations.verify(roleRepository).findAll(MockitoAnnotations.argThat(specification -> {
            RoleRepository.RoleSpecification spec = (RoleRepository.RoleSpecification) specification;
            assertEquals(organizationId, spec.organizationId);
            return true;
        }));
    }

    // Test for findByNameAndOrganizationId method
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedResult = new RoleEntity();
        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).orElse(null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByNameAndOrganizationIdNull() {
        String name = null;
        Long organizationId = 1L;
        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNull(result);
    }

    // Test for findByOrganizationIdAndNameIn method
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameInEmptyList() {
        Long organizationId = 1L;
        List<String> names = Collections.emptyList();
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for findByOrganizationIdAndRoleIds method
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> expectedResult = MockitoAnnotations.mock(Page.class);
        List<RoleEntity> content = Arrays.asList(new RoleEntity(), new RoleEntity());
        expectedResult.setContent(content);
        roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, MockitoAnnotations.mock(Pageable.class));
        MockitoAnnotations.verify(roleRepository).findAll(MockitoAnnotations.argThat(specification -> {
            RoleRepository.RoleSpecification spec = (RoleRepository.RoleSpecification) specification;
            assertEquals(organizationId, spec.organizationId);
            assertEquals(roleIds, spec.roleIds);
            return true;
        }));
    }

    // Test for findByUserId method
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByUserIdEmptyList() {
        Long userId = 1L;
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for findByUserIdAndEnabled method
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByUserIdAndEnabledEmptyList() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for findByRoleIdsAndEnabled method
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabledEmptyList() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for update method
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        int expectedResult = 1;
        int result = roleRepository.update(roleEntity);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullRoleEntity() {
        roleRepository.update(null);
    }
}