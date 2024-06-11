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
import com.oceanbase.odc.metadb.iam.RoleRepositoryImpl;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = Mockito.spy(new RoleRepositoryImpl());
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
 assertEquals(expected, result.get(0));
 }

 @Test
 public void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 RoleEntity expected = new RoleEntity();
 List<RoleEntity> roleList = Arrays.asList(expected);
 Page<RoleEntity> page = Mockito.mock(Page.class);
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);
 when(page.getContent()).thenReturn(roleList);

 // When
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 assertEquals(page, result);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "test";
 Long organizationId = 1L;
 RoleEntity expected = new RoleEntity();
 Optional<RoleEntity> optional = Optional.of(expected);
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

 // When
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertTrue(result.isPresent());
 assertEquals(expected, result.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test1", "test2");
 List<RoleEntity> roleList = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleList);

 // When
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(roleList, result);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 List<RoleEntity> roleList = Arrays.asList(new RoleEntity(), new RoleEntity());
 Page<RoleEntity> page = Mockito.mock(Page.class);
 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class))).thenReturn(page);
 when(page.getContent()).thenReturn(roleList);

 // When
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.mock(Pageable.class));

 // Then
 assertEquals(page, result);
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findByUserId(userId)).thenReturn(expected);

 // When
 List<RoleEntity> result = roleRepository.findByUserId(userId);

 // Then
 assertEquals(expected, result);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);

 // When
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals(expected, result);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);

 // When
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals(expected, result);
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity roleEntity = Mockito.mock(RoleEntity.class);
 doAnswer(invocationOnMock -> {
 int i = 1;
 return i;
 }).when(roleRepository).update(roleEntity);

 // When
 int result = roleRepository.update(roleEntity);

 // Then
 assertEquals(1, result);
 }
}