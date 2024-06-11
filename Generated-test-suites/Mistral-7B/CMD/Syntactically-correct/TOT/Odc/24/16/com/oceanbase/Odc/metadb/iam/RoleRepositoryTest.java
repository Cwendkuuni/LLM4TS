package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 // Given
 Long id1 = 1L;
 Long id2 = 2L;
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(id1, "role1"), new RoleEntity(id2, "role2"));
 when(roleRepository.findByIdIn(Mockito.any(Collection.class)))
 .thenReturn(roleEntities);

 // When
 Collection<Long> ids = Arrays.asList(id1, id2);
 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 // Then
 assertEquals(2, result.size());
 verify(roleRepository).findByIdIn(ids);
 }

 @Test
 public void testFindByIdInvalidId() {
 // Given
 Long id = -1L;
 when(roleRepository.findById(id)).thenReturn(Optional.empty());

 // When & Then
 assertFalse(roleRepository.findById(id).isPresent());
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "admin";
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, name), new RoleEntity(2L, "user"));
 when(roleRepository.findByName(name)).thenReturn(roleEntities);

 // When
 List<RoleEntity> result = roleRepository.findByName(name);

 // Then
 assertEquals(1, result.size());
 verify(roleRepository).findByName(name);
 }

 @Test
 public void testFindByNameInvalidName() {
 // Given
 String name = "";
 when(roleRepository.findByName(name)).thenReturn(Collections.emptyList());

 // When & Then
 assertTrue(roleRepository.findByName(name).isEmpty());
 }

 @Test
 public void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
 Pageable pageable = Mockito.mock(Pageable.class);
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(new PageImpl<>(roleEntities));

 // When
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 assertEquals(2, result.getContent().size());
 verify(roleRepository).findByOrganizationId(organizationId, pageable);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "admin";
 Long organizationId = 1L;
 RoleEntity roleEntity = new RoleEntity(1L, name, organizationId);
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

 // When
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertTrue(result.isPresent());
 verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("admin", "user");
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "admin", organizationId),
 new RoleEntity(2L, "user", organizationId));
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

 // When
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(2, result.size());
 verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIdsAndEnabled() {
 // Given
 Long organizationId = 1L;
 List<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1", organizationId, enabled),
 new RoleEntity(2L, "role2", organizationId, false));
 Pageable pageable = Mockito.mock(Pageable.class);
 when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled, pageable))
 .thenReturn(new PageImpl<>(roleEntities));

 // When
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled, pageable);

 // Then
 assertEquals(2, result.getContent().size());
 verify(roleRepository).findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled, pageable);
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
 when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

 // When
 List<RoleEntity> result = roleRepository.findByUserId(userId);

 // Then
 assertEquals(2, result.size());
 verify(roleRepository).findByUserId(userId);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1", userId, enabled),
 new RoleEntity(2L, "role2", userId, false));
 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

 // When
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals(1, result.size());
 verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

 // When
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals(2, result.size());
 verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity roleEntity = new RoleEntity(1L, "role1");
 when(roleRepository.update(roleEntity)).thenReturn(1);

 // When
 int result = roleRepository.update(roleEntity);

 // Then
 assertEquals(1, result);
 verify(roleRepository).update(roleEntity);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullRoleEntity() {
 // Given
 RoleEntity roleEntity = null;

 // When & Then
 assertThrows(IllegalArgumentException.class, () -> roleRepository.update(roleEntity));
 }
}