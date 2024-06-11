package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        roleRepository = MockitoAnnotations.spy(RoleRepositoryImpl.class); // Assuming RoleRepositoryImpl is the implementation class
    }

    // Test for findByIdIn method
    @Test
    public void testFindByIdIn() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(1L, "Admin", true), new RoleEntity(2L, "User", false));

        // When
        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        // Then
        assertEquals(expectedResult, result);
    }

    // Test for findByName method - positive scenario
    @Test
    public void testFindByName_positive() {
        // Given
        String name = "Admin";
        RoleEntity expectedResult = new RoleEntity(1L, name, true);

        // When
        RoleEntity result = roleRepository.findByName(name).get();

        // Then
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    // Test for findByName method - null scenario
    @Test
    public void testFindByName_null() {
        // Given
        String name = null;

        // When
        Optional<RoleEntity> result = roleRepository.findByName(name);

        // Then
        assertNull(result.isPresent());
    }

    // Test for findByOrganizationId method - empty Page
    @Test
    public void testFindByOrganizationId_emptyPage() {
        // Given
        Long organizationId = 1L;
        Pageable pageable = Pageable.ofSize(0);

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        // Then
        assertEquals(Collections.emptyList(), result.getContent());
        assertEquals(0L, result.getTotalElements());
    }

    // Test for findByOrganizationId method - non-empty Page
    @Test
    public void testFindByOrganizationId_nonEmptyPage() {
        // Given
        Long organizationId = 1L;
        List<RoleEntity> roleEntityList = Arrays.asList(new RoleEntity(1L, "Admin", true), new RoleEntity(2L, "User", false));
        Pageable pageable = Pageable.ofSize(1).withSort(Sort.by(Sort.Direction.ASC, "id"));

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        // Then
        assertEquals(roleEntityList, result.getContent());
        assertEquals(roleEntityList.size(), result.getTotalElements());
    }

    // Test for findByNameAndOrganizationId method - name not found
    @Test
    public void testFindByNameAndOrganizationId_nameNotFound() {
        // Given
        String name = "Admin";
        Long organizationId = 1L;

        // When
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        // Then
        assertFalse(result.isPresent());
    }

    // Test for findByNameAndOrganizationId method - name and organizationId found
    @Test
    public void testFindByNameAndOrganizationId_nameAndOrganizationIdFound() {
        // Given
        String name = "Admin";
        Long organizationId = 1L;
        RoleEntity expectedResult = new RoleEntity(1L, name, true);

        // When
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedResult, result.get());
    }

    // Test for findByOrganizationIdAndNameIn method - empty List
    @Test
    public void testFindByOrganizationIdAndNameIn_emptyList() {
        // Given
        Long organizationId = 1L;
        List<String> names = Collections.emptyList();
        Pageable pageable = Pageable.ofSize(10);

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable);

        // Then
        assertEquals(Collections.emptyList(), result.getContent());
        assertEquals(0L, result.getTotalElements());
    }

    // Test for findByOrganizationIdAndNameIn method - non-empty List
    @Test
    public void testFindByOrganizationIdAndNameIn_nonEmptyList() {
        // Given
        Long organizationId = 1L;
        List<String> names = Arrays.asList("Admin", "User");
        List<RoleEntity> roleEntityList = Arrays.asList(new RoleEntity(1L, "Admin", true), new RoleEntity(2L, "User", false));
        Pageable pageable = Pageable.ofSize(10);

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable);

        // Then
        assertEquals(roleEntityList, result.getContent());
        assertEquals(roleEntityList.size(), result.getTotalElements());
    }

    // Test for findByOrganizationIdAndRoleIds method - empty Collection
    @Test
    public void testFindByOrganizationIdAndRoleIds_emptyCollection() {
        // Given
        Long organizationId = 1L;
        Collection<Long> roleIds = Collections.emptyList();
        Pageable pageable = Pageable.ofSize(10);

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        // Then
        assertEquals(Collections.emptyList(), result.getContent());
        assertEquals(0L, result.getTotalElements());
    }

    // Test for findByOrganizationIdAndRoleIds method - non-empty Collection
    @Test
    public void testFindByOrganizationIdAndRoleIds_nonEmptyCollection() {
        // Given
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntityList = Arrays.asList(new RoleEntity(1L, "Admin", true), new RoleEntity(2L, "User", false));
        Pageable pageable = Pageable.ofSize(10);

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        // Then
        assertEquals(roleEntityList, result.getContent());
        assertEquals(roleEntityList.size(), result.getTotalElements());
    }

    // Test for findByUserId method
    @Test
    public void testFindByUserId() {
        // Given
        Long userId = 1L;

        // When
        List<RoleEntity> result = roleRepository.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // Test for findByUserIdAndEnabled method - enabled = true
    @Test
    public void testFindByUserIdAndEnabled_enabledTrue() {
        // Given
        Long userId = 1L;
        Boolean enabled = true;

        // When
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        for (RoleEntity roleEntity : result) {
            assertTrue(roleEntity.isEnabled());
        }
    }

    // Test for findByUserIdAndEnabled method - enabled = false
    @Test
    public void testFindByUserIdAndEnabled_enabledFalse() {
        // Given
        Long userId = 1L;
        Boolean enabled = false;

        // When
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for findByRoleIdsAndEnabled method - enabled = true
    @Test
    public void testFindByRoleIdsAndEnabled_enabledTrue() {
        // Given
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;

        // When
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        for (RoleEntity roleEntity : result) {
            assertTrue(roleEntity.isEnabled());
        }
    }

    // Test for findByRoleIdsAndEnabled method - enabled = false
    @Test
    public void testFindByRoleIdsAndEnabled_enabledFalse() {
        // Given
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = false;

        // When
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for update method - successful update
    @Test
    public void testUpdate_successful() {
        // Given
        RoleEntity roleEntity = new RoleEntity(1L, "Admin", true);

        // When
        int rowsAffected = roleRepository.update(roleEntity);

        // Then
        assertEquals(1, rowsAffected);
    }

    // Test for update method - failed update (roleEntity not found)
    @Test(expected = RuntimeException.class)
    public void testUpdate_failed() {
        // Given
        RoleEntity roleEntity = new RoleEntity(100L, "Admin", true);

        // When
        roleRepository.update(roleEntity);

        // Then
        // An exception should be thrown as the roleEntity with id 100 does not exist in the repository
    }
}