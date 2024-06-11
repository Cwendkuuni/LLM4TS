package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.exception.RoleNotFoundException;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

/**
 * RoleRepositoryTest
 */
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
    }

    /**
     * Test findByIdIn method
     */
    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity, new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(expectedResult);
        when(roleRepository.findByIdIn(ids)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByIdIn(ids);
        verify(roleRepository).findByIdIn(ids);
        assertThat(result).isEqualTo(page);
    }

    /**
     * Test findByName method with existing name
     */
    @Test
    public void testFindByNameExisting() {
        String name = "role1";
        RoleEntity expectedResult = roleEntity;
        Optional<RoleEntity> optional = Optional.of(roleEntity);
        when(roleRepository.findByName(name)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findByName(name);
        verify(roleRepository).findByName(name);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedResult, result.get());
    }

    /**
     * Test findByName method with non-existing name
     */
    @Test(expected = RoleNotFoundException.class)
    public void testFindByNameNonExisting() {
        String name = "role2";
        when(roleRepository.findByName(name)).thenReturn(Optional.empty());

        roleRepository.findByName(name);
    }

    /**
     * Test findByOrganizationId method
     */
    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity);
        Page<RoleEntity> page = new PageImpl<>(expectedResult);
        when(roleRepository.findByOrganizationId(organizationId, Pageable.unpaged())).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
        verify(roleRepository).findByOrganizationId(organizationId, Pageable.unpaged());
        assertThat(result).isEqualTo(page);
    }

    /**
     * Test findByNameAndOrganizationId method
     */
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "role1";
        Long organizationId = 1L;
        RoleEntity expectedResult = roleEntity;
        Optional<RoleEntity> optional = Optional.of(roleEntity);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedResult, result.get());
    }

    /**
     * Test findByOrganizationIdAndNameIn method
     */
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("role1", "role2");
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity, new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(expectedResult);
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
        assertThat(result).isEqualTo(page);
    }

    /**
     * Test findByOrganizationIdAndRoleIds method
     */
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity, new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(expectedResult);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged()))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                Pageable.unpaged());
        verify(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertThat(result).isEqualTo(page);
    }

    /**
     * Test findByUserId method
     */
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity);
        when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        verify(roleRepository).findByUserId(userId);
        Assertions.assertEquals(expectedResult, result);
    }

    /**
     * Test findByUserIdAndEnabled method
     */
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity);
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
        Assertions.assertEquals(expectedResult, result);
    }

    /**
     * Test findByRoleIdsAndEnabled method
     */
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity, new RoleEntity());

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
        Assertions.assertEquals(expectedResult, result);
    }

    /**
     * Test update method
     */
    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setId(1L);
        updatedRoleEntity.setName("updatedRoleName");
        updatedRoleEntity.setIsEnabled(true);
        int expectedResult = 1;

        when(roleRepository.update(updatedRoleEntity)).thenReturn(expectedResult);

        int result = roleRepository.update(updatedRoleEntity);
        verify(roleRepository).update(updatedRoleEntity);
        Assertions.assertEquals(expectedResult, result);
    }

    /**
     * Test findByUserIdAndEnabled with non-existing userId and enabled
     */
    @Test
    public void testFindByUserIdAndEnabledNonExisting() {
        Long userId = 1L;
        Boolean enabled = true;
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.emptyList());

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * Test findByUserIdAndEnabled with non-existing userId
     */
    @Test
    public void testFindByUserIdAndEnabledNonExistingUserId() {
        Long userId = 2L;
        Boolean enabled = true;
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.emptyList());

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * Test findByUserIdAndEnabled with non-existing enabled
     */
    @Test
    public void testFindByUserIdAndEnabledNonExistingEnabled() {
        Long userId = 1L;
        Boolean enabled = false;
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.emptyList());

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
        Assertions.assertTrue(result.isEmpty());
    }
}