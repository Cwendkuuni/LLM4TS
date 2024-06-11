package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
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
 List<Long> ids = Arrays.asList(id1, id2);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByIdIn(ids);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "test";
 RoleEntity expected = new RoleEntity();
 Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expected));

 // When
 List<RoleEntity> actual = roleRepository.findByName(name);

 // Then
 assertEquals(expected, actual.get(0));
 }

 @Test
 public void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expected = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expected);

 // When
 Page<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 assertTrue(actual.hasContent());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "test";
 Long organizationId = 1L;
 RoleEntity expected = new RoleEntity();
 Optional<RoleEntity> optionalExpected = Optional.of(expected);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalExpected);

 // When
 Optional<RoleEntity> actual = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertTrue(actual.isPresent());
 assertEquals(expected, actual.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test1", "test2");
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expected = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expected);

 // When
 Page<RoleEntity> actual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Then
 assertTrue(actual.hasContent());
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByUserId(userId);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity roleEntity = new RoleEntity();
 Mockito.doNothing().when(roleRepository).update(roleEntity);

 // When
 int actual = roleRepository.update(roleEntity);

 // Then
 assertEquals(1, actual);
 }
}