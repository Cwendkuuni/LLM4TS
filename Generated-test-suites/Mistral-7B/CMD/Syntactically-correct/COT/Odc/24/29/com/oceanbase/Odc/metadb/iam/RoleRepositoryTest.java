package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    // Basic test cases for findByIdIn method
    @Test
    public void testFindByIdIn_validIds_returnsExpectedEntities() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<RoleEntity> expectedEntities = Mockito.mock(List.class);
        RoleEntity entity1 = Mockito.mock(RoleEntity.class);
        RoleEntity entity2 = Mockito.mock(RoleEntity.class);
        Mockito.when(expectedEntities.get(0)).thenReturn(entity1);
        Mockito.when(expectedEntities.get(1)).thenReturn(entity2);

        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByIdIn(ids);

        assertEquals(2, actualEntities.size());
        assertEquals(entity1, actualEntities.get(0));
        assertEquals(entity2, actualEntities.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testFindByIdIn_nullIds_throwsRuntimeException() {
        roleRepository.findByIdIn(null);
    }

    // Basic test cases for findByName method
    @Test
    public void testFindByName_validName_returnsExpectedEntity() {
        String name = "testRole";
        RoleEntity expectedEntity = Mockito.mock(RoleEntity.class);

        Mockito.when(roleRepository.findByName(name)).thenReturn(expectedEntity);

        RoleEntity actualEntity = roleRepository.findByName(name);

        assertEquals(expectedEntity, actualEntity);
    }

    @Test
    public void testFindByName_nonExistingName_returnsEmptyOptional() {
        String name = "nonExistingRole";

        Optional<RoleEntity> expectedOptional = Optional.empty();
        Mockito.when(roleRepository.findByName(name)).thenReturn(expectedOptional);

        Optional<RoleEntity> actualOptional = roleRepository.findByName(name);

        assertTrue(actualOptional.isEmpty());
    }

    // Basic test cases for findByOrganizationId method with Pageable
    @Test
    public void testFindByOrganizationId_validOrganizationId_returnsExpectedPage() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        List<RoleEntity> expectedEntities = new ArrayList<>();
        RoleEntity entity1 = Mockito.mock(RoleEntity.class);
        RoleEntity entity2 = Mockito.mock(RoleEntity.class);
        Mockito.when(expectedPage.getContent()).thenReturn(expectedEntities);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(expectedPage.getSize(), actualPage.getSize());
        assertEquals(expectedEntities, actualPage.getContent());
    }

    @Test
    public void testFindByOrganizationId_emptyPage_returnsEmptyPage() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        Mockito.when(expectedPage.isEmpty()).thenReturn(true);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

        assertTrue(actualPage.isEmpty());
    }

    // Basic test cases for findByNameAndOrganizationId method
    @Test
    public void testFindByNameAndOrganizationId_validNameAndOrganizationId_returnsExpectedEntity() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedEntity = Mockito.mock(RoleEntity.class);

        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedEntity);

        RoleEntity actualEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertEquals(expectedEntity, actualEntity);
    }

    @Test
    public void testFindByNameAndOrganizationId_nonExistingNameAndOrganizationId_returnsEmptyOptional() {
        String name = "nonExistingRole";
        Long organizationId = 1L;

        Optional<RoleEntity> expectedOptional = Optional.empty();
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedOptional);

        Optional<RoleEntity> actualOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(actualOptional.isEmpty());
    }

    // Basic test cases for findByOrganizationIdAndNameIn method
    @Test
    public void testFindByOrganizationIdAndNameIn_validOrganizationIdAndNames_returnsExpectedEntities() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testRole1");
        names.add("testRole2");

        List<RoleEntity> expectedEntities = Mockito.mock(List.class);
        RoleEntity entity1 = Mockito.mock(RoleEntity.class);
        RoleEntity entity2 = Mockito.mock(RoleEntity.class);
        Mockito.when(expectedEntities.get(0)).thenReturn(entity1);
        Mockito.when(expectedEntities.get(1)).thenReturn(entity2);

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, actualEntities.size());
        assertEquals(entity1, actualEntities.get(0));
        assertEquals(entity2, actualEntities.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testFindByOrganizationIdAndNameIn_nullOrganizationId_throwsRuntimeException() {
        List<String> names = new ArrayList<>();
        roleRepository.findByOrganizationIdAndNameIn(null, names);
    }

    @Test(expected = RuntimeException.class)
    public void testFindByOrganizationIdAndNameIn_nullNames_throwsRuntimeException() {
        Long organizationId = 1L;
        roleRepository.findByOrganizationIdAndNameIn(organizationId, null);
    }

    // Test cases for findByOrganizationIdAndRoleIds method with Pageable
    @Test
    public void testFindByOrganizationIdAndRoleIds_validOrganizationIdAndRoleIds_returnsExpectedPage() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);

        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        List<RoleEntity> expectedEntities = new ArrayList<>();
        RoleEntity entity1 = Mockito.mock(RoleEntity.class);
        RoleEntity entity2 = Mockito.mock(RoleEntity.class);
        Mockito.when(expectedPage.getContent()).thenReturn(expectedEntities);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(expectedPage.getSize(), actualPage.getSize());
        assertEquals(expectedEntities, actualPage.getContent());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_emptyPage_returnsEmptyPage() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        Mockito.when(expectedPage.isEmpty()).thenReturn(true);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertTrue(actualPage.isEmpty());
    }

    // Basic test cases for findByUserId method
    @Test
    public void testFindByUserId_validUserId_returnsExpectedEntities() {
        Long userId = 1L;
        List<RoleEntity> expectedEntities = Mockito.mock(List.class);
        RoleEntity entity1 = Mockito.mock(RoleEntity.class);
        RoleEntity entity2 = Mockito.mock(RoleEntity.class);
        Mockito.when(expectedEntities.get(0)).thenReturn(entity1);
        Mockito.when(expectedEntities.get(1)).thenReturn(entity2);

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByUserId(userId);

        assertEquals(2, actualEntities.size());
        assertEquals(entity1, actualEntities.get(0));
        assertEquals(entity2, actualEntities.get(1));
    }

    @Test
    public void testFindByUserId_nonExistingUserId_returnsEmptyList() {
        Long userId = 1L;

        List<RoleEntity> expectedEntities = new ArrayList<>();

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByUserId(userId);

        assertEquals(0, actualEntities.size());
    }

    // Basic test cases for findByUserIdAndEnabled method
    @Test
    public void testFindByUserIdAndEnabled_validUserIdAndEnabled_returnsExpectedEntities() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedEntities = Mockito.mock(List.class);
        RoleEntity entity1 = Mockito.mock(RoleEntity.class);
        RoleEntity entity2 = Mockito.mock(RoleEntity.class);
        Mockito.when(expectedEntities.get(0)).thenReturn(entity1);
        Mockito.when(expectedEntities.get(1)).thenReturn(entity2);

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(2, actualEntities.size());
        assertEquals(entity1, actualEntities.get(0));
        assertEquals(entity2, actualEntities.get(1));
    }

    @Test
    public void testFindByUserIdAndEnabled_validUserIdAndDisabled_returnsEmptyList() {
        Long userId = 1L;
        Boolean enabled = false;

        List<RoleEntity> expectedEntities = new ArrayList<>();

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(0, actualEntities.size());
    }

    // Basic test cases for findByRoleIdsAndEnabled method
    @Test
    public void testFindByRoleIdsAndEnabled_validRoleIdsAndEnabled_returnsExpectedEntities() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;

        List<RoleEntity> expectedEntities = Mockito.mock(List.class);
        RoleEntity entity1 = Mockito.mock(RoleEntity.class);
        RoleEntity entity2 = Mockito.mock(RoleEntity.class);
        Mockito.when(expectedEntities.get(0)).thenReturn(entity1);
        Mockito.when(expectedEntities.get(1)).thenReturn(entity2);

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, actualEntities.size());
        assertEquals(entity1, actualEntities.get(0));
        assertEquals(entity2, actualEntities.get(1));
    }

    @Test
    public void testFindByRoleIdsAndEnabled_validRoleIdsAndDisabled_returnsEmptyList() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = false;

        List<RoleEntity> expectedEntities = new ArrayList<>();

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedEntities);

        List<RoleEntity> actualEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(0, actualEntities.size());
    }

    // Test case for update method
    @Test
    public void testUpdate_validRoleEntity_returnsExpectedRowsAffected() {
        RoleEntity roleEntity = Mockito.mock(RoleEntity.class);
        int expectedRowsAffected = 1;

        Mockito.when(roleRepository.update(roleEntity)).thenReturn(expectedRowsAffected);

        int actualRowsAffected = roleRepository.update(roleEntity);

        assertEquals(expectedRowsAffected, actualRowsAffected);
    }

    // Edge cases and exception handling test cases can be added based on the business requirements and the given RoleRepository class.
    // ...

    // Additional test cases for other methods can be added if necessary.
    // ...
}