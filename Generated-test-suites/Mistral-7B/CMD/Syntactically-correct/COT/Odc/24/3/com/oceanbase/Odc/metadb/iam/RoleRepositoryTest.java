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

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 // Given
 List<Long> ids = Arrays.asList(1L, 2L);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findByIdIn(ids)).thenReturn(expected);

 // When
 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 // Then
 assertEquals(expected, result);
 verify(roleRepository, times(1)).findByIdIn(ids);
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "test";
 RoleEntity expected = new RoleEntity();
 when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expected));

 // When
 List<RoleEntity> result = roleRepository.findByName(name);

 // Then
 assertEquals(Collections.singletonList(expected), result);
 verify(roleRepository, times(1)).findByName(name);
 }

 @Test
 public void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Page<RoleEntity> page = Mockito.mock(Page.class);
 when(roleRepository.findAll()).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));
 when(page.getContent()).thenReturn(expected);
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

 // When
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 assertEquals(page, result);
 verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "test";
 Long organizationId = 1L;
 RoleEntity expected = new RoleEntity();
 Optional<RoleEntity> optionalExpected = Optional.of(expected);
 when(roleRepository.findAll()).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalExpected);

 // When
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertTrue(result.isPresent());
 assertEquals(expected, result.get());
 verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test1", "test2");
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findAll()).thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity(), new RoleEntity()));
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);

 // When
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(expected, result);
 verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithInvalidPageable() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Pageable pageable = Mockito.mock(Pageable.class);
 when(pageable.getPageSize()).thenThrow(IllegalArgumentException.class);

 // When & Then
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsWithNullPageable() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Pageable pageable = null;

 // When & Then
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findAll()).thenReturn(expected);
 when(roleRepository.findByUserId(userId)).thenCallRealMethod();

 // When
 List<RoleEntity> result = roleRepository.findByUserId(userId);

 // Then
 assertEquals(expected, result);
 verify(roleRepository, times(1)).findByUserId(userId);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findAll()).thenReturn(expected);
 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenCallRealMethod();

 // When
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals(expected, result);
 verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findAll()).thenReturn(expected);
 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenCallRealMethod();

 // When
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals(expected, result);
 verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
 }

 @Test
 public void testUpdate() {
 // Given
 Long id = 1L;
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("test");
 roleEntity.setIsEnabled(true);
 roleEntity.setDescription("test");
 roleEntity.setUserUpdateTime(System.currentTimeMillis());

 // When
 int result = roleRepository.update(roleEntity);

 // Then
 assertEquals(1, result);
 verify(roleRepository, times(1)).update(roleEntity);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullRoleEntity() {
 // Given
 RoleEntity roleEntity = null;

 // When & Then
 roleRepository.update(roleEntity);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullId() {
 // Given
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(null);

 // When & Then
 roleRepository.update(roleEntity);
 }
}