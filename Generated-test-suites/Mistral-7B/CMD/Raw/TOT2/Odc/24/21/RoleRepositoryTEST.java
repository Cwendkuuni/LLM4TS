package com.oceanbase.odc.metadb.iam.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Mock
 private RoleEntity roleEntity;

 @BeforeEach
 void setup() {
 MockitoAnnotations.initMocks(this);
 roleRepository = mock(RoleRepository.class);
 }

 @Test
 void testFindByIdIn() {
 // Given
 List<Long> ids = Arrays.asList(1L, 2L);
 List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
 when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);

 // Then
 assertEquals(expectedRoles, actualRoles);
 verify(roleRepository).findByIdIn(ids);
 }

 @Test
 void testFindByName() {
 // Given
 String name = "TestRole";
 List<RoleEntity> expectedRoles = Arrays.asList(roleEntity);
 when(roleRepository.findByName(name)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> actualRoles = roleRepository.findByName(name);

 // Then
 assertEquals(expectedRoles, actualRoles);
 verify(roleRepository).findByName(name);
 }

 @Test
 void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 List<RoleEntity> expectedRoles = Arrays.asList(roleEntity);
 Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

 // When
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 assertEquals(expectedPage.getContent(), actualPage.getContent());
 verify(roleRepository).findByOrganizationId(organizationId, pageable);
 }

 @Test
 void testFindByNameAndOrganizationId() {
 // Given
 String name = "TestRole";
 Long organizationId = 1L;
 RoleEntity expectedRole = roleEntity;
 Optional<RoleEntity> expectedOptionalRole = Optional.of(expectedRole);
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedOptionalRole);

 // When
 Optional<RoleEntity> actualOptionalRole = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertTrue(actualOptionalRole.isPresent());
 assertEquals(expectedRole, actualOptionalRole.get());
 verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
 }

 @Test
 void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("TestRole1", "TestRole2");
 List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(expectedRoles, actualRoles);
 verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
 }

 @Test
 void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 List<RoleEntity> expectedRoles = Arrays.asList(roleEntity, roleEntity);
 Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);
 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged())).thenReturn(expectedPage);

 // When
 Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());

 // Then
 assertEquals(expectedPage.getContent(), actualPage.getContent());
 verify(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
 }

 @Test
 void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expectedRoles = Arrays.asList(roleEntity);
 when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

 // Then
 assertEquals(expectedRoles, actualRoles);
 verify(roleRepository).findByUserId(userId);
 }

 @Test
 void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = false;
 List<RoleEntity> expectedRoles = Collections.emptyList();
 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertTrue(actualRoles.isEmpty());
 verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
 }

 @Test
 void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = false;
 List<RoleEntity> expectedRoles = Collections.emptyList();
 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

 // When
 List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertTrue(actualRoles.isEmpty());
 verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
 }

 @Test
 void testUpdate() {
 // Given
 RoleEntity updatedRoleEntity = new RoleEntity();
 when(roleRepository.update(roleEntity)).thenReturn(1);

 // When
 int actualRowsAffected = roleRepository.update(roleEntity);

 // Then
 assertEquals(1, actualRowsAffected);
 verify(roleRepository).update(roleEntity);
 }
}