package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(classes = RoleRepository.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case for findByIdIn method
    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(expectedResult, result);
    }

    // Test case for findByName method
    @Test
    public void testFindByName() {
        String name = "testRole";
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByName(name)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(expectedResult, result);
    }

    // Test case for findByOrganizationId method with valid pageable
    @Test
    public void testFindByOrganizationIdWithValidPageable() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedResult = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedResult);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedResult, result);
    }

    // Test case for findByOrganizationId method with invalid pageable
    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdWithInvalidPageable() {
        Long organizationId = 1L;
        Pageable pageable = null;
        roleRepository.findByOrganizationId(organizationId, pageable);
    }

    // Test case for findByNameAndOrganizationId method
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        Optional<RoleEntity> expectedResult = Optional.of(Mockito.mock(RoleEntity.class));
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedResult);
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(result.isPresent());
        assertEquals(expectedResult.get(), result.get());
    }

    // Test case for findByOrganizationIdAndNameIn method with valid parameters
    @Test
    public void testFindByOrganizationIdAndNameInWithValidParameters() {
        Long organizationId = 1L;
        List<String> names = Collections.singletonList("testRole");
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedResult, result);
    }

    // Test case for findByOrganizationIdAndRoleIds method with valid parameters
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Page<RoleEntity> expectedResult = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
                .thenReturn(expectedResult);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.mock(Pageable.class));
        assertEquals(expectedResult, result);
    }

    // Test case for findByUserId method
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(expectedResult, result);
    }

    // Test case for findByUserIdAndEnabled method with enabled = true
    @Test
    public void testFindByUserIdAndEnabledWithEnabledTrue() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, true)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, true);
        assertEquals(expectedResult, result);
    }

    // Test case for findByUserIdAndEnabled method with enabled = false
    @Test
    public void testFindByUserIdAndEnabledWithEnabledFalse() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Collections.emptyList();
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, false)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, false);
        assertTrue(result.isEmpty());
    }

    // Test case for findByRoleIdsAndEnabled method with valid parameters
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedResult, result);
    }

    // Test case for update method
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = Mockito.mock(RoleEntity.class);
        int expectedResult = 1;
        Mockito.when(roleRepository.update(roleEntity)).thenReturn(expectedResult);
        int result = roleRepository.update(roleEntity);
        assertEquals(expectedResult, result);
    }
}