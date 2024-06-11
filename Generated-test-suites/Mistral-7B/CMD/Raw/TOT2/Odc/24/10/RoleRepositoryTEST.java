package com.oceanbase.odc.metadb.iam;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;

class RoleRepositoryTest {

 private RoleRepository roleRepository;
 private RoleEntity roleEntity;

 @BeforeEach
 public void setUp() {
 roleRepository = Mockito.spy(RoleRepositoryImpl.class);
 roleEntity = new RoleEntity();
 }

 @AfterEach
 public void tearDown() {
 roleRepository = null;
 roleEntity = null;
 }

 @Test
 void testFindByIdIn() {
 // Given
 Long id1 = 1L;
 Long id2 = 2L;
 List<Long> ids = Arrays.asList(id1, id2);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

 // When
 doReturn(expected).when(roleRepository).findByIdIn(ids);
 List<RoleEntity> actual = roleRepository.findByIdIn(ids);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByName() {
 // Given
 String name = RandomStringUtils.randomAlphanumeric(10);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

 // When
 doReturn(expected).when(roleRepository).findByName(name);
 List<RoleEntity> actual = roleRepository.findByName(name);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> expected = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()), pageable, 2);

 // When
 doReturn(expected).when(roleRepository).findByOrganizationId(organizationId, pageable);
 Page<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 assertEquals(expected, actual);
 }

 // Test findByNameAndOrganizationId will be tested in the following test methods for findByOrganizationIdAndName

 @Test
 void testFindByOrganizationIdAndName() {
 // Given
 String name = RandomStringUtils.randomAlphanumeric(10);
 Long organizationId = 1L;
 RoleEntity expected = new RoleEntity();
 Optional<RoleEntity> optionalExpected = Optional.of(expected);

 // When
 doReturn(optionalExpected).when(roleRepository).findByNameAndOrganizationId(name, organizationId);
 Optional<RoleEntity> actual = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertEquals(expected, actual.get());
 }

 @Test
 void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("name1", "name2");
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

 // When
 doReturn(expected).when(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
 List<RoleEntity> actual = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Page<RoleEntity> expected = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()), Pageable.empty(), 2);

 // When
 doAnswer(invocation -> {
 Page<RoleEntity> page = (Page<RoleEntity>) invocation.getArgument(2);
 page.setContent(Arrays.asList(new RoleEntity(), new RoleEntity()));
 return expected;
 }).when(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
 Page<RoleEntity> actual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());

 // Then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

 // When
 doReturn(expected).when(roleRepository).findByUserId(userId);
 List<RoleEntity> actual = roleRepository.findByUserId(userId);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

 // When
 doReturn(expected).when(roleRepository).findByUserIdAndEnabled(userId, enabled);
 List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

 // When
 doReturn(expected).when(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
 List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals(expected, actual);
 }

 @Test
 void testUpdate() {
 // Given
 String newName = RandomStringUtils.randomAlphanumeric(10);
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("oldName");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("oldDescription");
 roleEntity.setUserUpdateTime(new java.sql.Timestamp(System.currentTimeMillis()));

 // When
 doAnswer(invocation -> {
 int rowsAffected = 2;
 return rowsAffected;
 }).when(roleRepository).update(roleEntity);
 int actual = roleRepository.update(roleEntity);

 // Then
 assertEquals(roleEntity.getName(), newName);
 assertEquals(actual, 2);
 }

}