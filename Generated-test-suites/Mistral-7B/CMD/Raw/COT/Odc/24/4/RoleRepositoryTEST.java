package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Testing findByIdIn method

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoleList);
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByIdIn(ids);
        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByIdInEmptyList() {
        Collection<Long> ids = Collections.emptyList();
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByIdIn(ids);
        assertTrue(actualRoleList.isEmpty());
    }

    // Testing findByName method

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity expectedRoleEntity = new RoleEntity();
        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedRoleEntity));
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByName(name);
        assertEquals(expectedRoleEntity, actualRoleList.get(0));
    }

    @Test
    public void testFindByNameEmptyList() {
        String name = "testRole";
        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByName(name);
        assertTrue(actualRoleList.isEmpty());
    }

    // Testing findByOrganizationId method with Pageable

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);
        Mockito.when(expectedPage.getContent()).thenReturn(expectedRoleList);
        Page<RoleEntity> actualPage = roleRepositoryTest.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedRoleList, actualPage.getContent());
    }

    @Test
    public void testFindByOrganizationIdEmptyList() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);
        Mockito.when(expectedPage.getContent()).thenReturn(Collections.emptyList());
        Page<RoleEntity> actualPage = roleRepositoryTest.findByOrganizationId(organizationId, pageable);
        assertTrue(actualPage.getContent().isEmpty());
    }

    // Testing findByNameAndOrganizationId method

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedRoleEntity = new RoleEntity();
        Optional<RoleEntity> expectedOptionalRoleEntity = Optional.of(expectedRoleEntity);
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedOptionalRoleEntity);
        Optional<RoleEntity> actualOptionalRoleEntity = roleRepositoryTest.findByNameAndOrganizationId(name, organizationId);
        assertTrue(actualOptionalRoleEntity.isPresent());
        RoleEntity actualRoleEntity = actualOptionalRoleEntity.get();
        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    public void testFindByNameAndOrganizationIdEmptyOptional() {
        String name = "testRole";
        Long organizationId = 1L;
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.empty());
        Optional<RoleEntity> actualOptionalRoleEntity = roleRepositoryTest.findByNameAndOrganizationId(name, organizationId);
        assertFalse(actualOptionalRoleEntity.isPresent());
    }

    // Testing findByOrganizationIdAndNameIn method

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoleList);
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByOrganizationIdAndNameInEmptyList() {
        Long organizationId = 1L;
        List<String> names = Collections.emptyList();
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByOrganizationIdAndNameIn(organizationId, names);
        assertTrue(actualRoleList.isEmpty());
    }

    // Testing findByOrganizationIdAndRoleIds and findByRoleIdsAndEnabled methods

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds)).thenReturn(expectedRoleList);
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByOrganizationIdAndRoleIds(organizationId, roleIds);
        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIdsEmptyList() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Collections.emptyList();
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByOrganizationIdAndRoleIds(organizationId, roleIds);
        assertTrue(actualRoleList.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoleList);
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByRoleIdsAndEnabledEmptyList() {
        Collection<Long> roleIds = Collections.emptyList();
        Boolean enabled = true;
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByRoleIdsAndEnabled(roleIds, enabled);
        assertTrue(actualRoleList.isEmpty());
    }

    // Testing findByUserId method

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedRoleList);
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByUserId(userId);
        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByUserIdEmptyList() {
        Long userId = 1L;
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByUserId(userId);
        assertTrue(actualRoleList.isEmpty());
    }

    // Testing findByUserIdAndEnabled method

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoleList);
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByUserIdAndEnabledEmptyList() {
        Long userId = 1L;
        Boolean enabled = true;
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByUserIdAndEnabled(userId, enabled);
        assertTrue(actualRoleList.isEmpty());
    }

    // Testing findByRoleIdsAndEnabled method

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoleList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoleList);
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    public void testFindByRoleIdsAndEnabledEmptyList() {
        Collection<Long> roleIds = Collections.emptyList();
        Boolean enabled = true;
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(Collections.emptyList());
        List<RoleEntity> actualRoleList = roleRepositoryTest.findByRoleIdsAndEnabled(roleIds, enabled);
        assertTrue(actualRoleList.isEmpty());
    }

    // Testing update method

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        int expectedRowsAffected = 1;
        Mockito.when(roleRepository.update(roleEntity)).thenReturn(expectedRowsAffected);
        int actualRowsAffected = roleRepositoryTest.update(roleEntity);
        assertEquals(expectedRowsAffected, actualRowsAffected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullRoleEntity() {
        RoleEntity roleEntity = null;
        roleRepositoryTest.update(roleEntity);
    }
}