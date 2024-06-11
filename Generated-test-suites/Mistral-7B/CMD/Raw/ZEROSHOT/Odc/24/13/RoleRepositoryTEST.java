package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Before
 public void setup() {
 roleRepository = Mockito.spy(RoleRepositoryImpl.class);
 }

 @Test
 public void testFindByIdIn() {
 // Given
 Long id1 = 1L;
 Long id2 = 2L;
 List<Long> ids = Arrays.asList(id1, id2);
 RoleEntity role1 = new RoleEntity();
 role1.setId(id1);
 RoleEntity role2 = new RoleEntity();
 role2.setId(id2);
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(Arrays.asList(role1, role2));

 // When
 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 // Then
 assertEquals(2, result.size());
 assertEquals(role1, result.get(0));
 assertEquals(role2, result.get(1));
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "admin";
 RoleEntity role = new RoleEntity();
 role.setName(name);
 Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(role));

 // When
 List<RoleEntity> result = roleRepository.findByName(name);

 // Then
 assertEquals(1, result.size());
 assertEquals(role, result.get(0));
 }

 @Test
 public void testFindByOrganizationId() {
 // Given
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 RoleEntity role1 = new RoleEntity();
 role1.setOrganizationId(organizationId);
 RoleEntity role2 = new RoleEntity();
 role2.setOrganizationId(organizationId);
 List<RoleEntity> roles = Arrays.asList(role1, role2);
 Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findAll()).thenReturn(roles);
 Mockito.when(roleRepository.findAll(pageable.getPageNumber(), pageable.getPageSize())).thenReturn(expectedPage);
 Mockito.when(expectedPage.getContent()).thenReturn(roles);
 Mockito.when(expectedPage.getTotalElements()).thenReturn(2L);

 // When
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 // Then
 assertEquals(2, result.getTotalElements());
 assertEquals(roles, result.getContent());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "admin";
 Long organizationId = 1L;
 RoleEntity role = new RoleEntity();
 role.setName(name);
 role.setOrganizationId(organizationId);
 Optional<RoleEntity> optionalRole = Optional.of(role);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRole);

 // When
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 assertTrue(result.isPresent());
 assertEquals(role, result.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("admin", "user");
 RoleEntity role1 = new RoleEntity();
 role1.setOrganizationId(organizationId);
 role1.setName("admin");
 RoleEntity role2 = new RoleEntity();
 role2.setOrganizationId(organizationId);
 role2.setName("user");
 List<RoleEntity> roles = Arrays.asList(role1, role2);
 Mockito.when(roleRepository.findAll()).thenReturn(roles);

 // When
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 assertEquals(2, result.size());
 assertTrue(result.contains(role1));
 assertTrue(result.contains(role2));
 }

 @Test
 public void testFindByOrganizationIdAndRoleIdsAndEnabled() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 RoleEntity role1 = new RoleEntity();
 role1.setOrganizationId(organizationId);
 role1.setId(roleIds.get(0));
 role1.setEnabled(enabled);
 RoleEntity role2 = new RoleEntity();
 role2.setOrganizationId(organizationId);
 role2.setId(roleIds.get(1));
 role2.setEnabled(false);
 List<RoleEntity> roles = Arrays.asList(role1, role2);
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findAll()).thenReturn(roles);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled))
 .thenReturn(expectedPage);
 Mockito.when(expectedPage.getContent()).thenReturn(roles);
 Mockito.when(expectedPage.getTotalElements()).thenReturn(2L);

 // When
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(organizationId, roleIds, enabled, pageable);

 // Then
 assertEquals(2, result.getTotalElements());
 assertEquals(roles, result.getContent());
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 RoleEntity role1 = new RoleEntity();
 role1.setUserId(userId);
 RoleEntity role2 = new RoleEntity();
 List<RoleEntity> roles = Arrays.asList(role1, role2);
 Mockito.when(roleRepository.findAll()).thenReturn(roles);

 // When
 List<RoleEntity> result = roleRepository.findByUserId(userId);

 // Then
 assertEquals(1, result.size());
 assertEquals(role1, result.get(0));
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 RoleEntity role1 = new RoleEntity();
 role1.setUserId(userId);
 role1.setEnabled(enabled);
 RoleEntity role2 = new RoleEntity();
 List<RoleEntity> roles = Arrays.asList(role1, role2);
 Mockito.when(roleRepository.findAll()).thenReturn(roles);

 // When
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 assertEquals(1, result.size());
 assertEquals(role1, result.get(0));
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 RoleEntity role1 = new RoleEntity();
 role1.setId(roleIds.get(0));
 role1.setEnabled(enabled);
 RoleEntity role2 = new RoleEntity();
 role2.setId(roleIds.get(1));
 role2.setEnabled(false);
 List<RoleEntity> roles = Arrays.asList(role1, role2);
 Mockito.when(roleRepository.findAll()).thenReturn(roles);

 // When
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 assertEquals(1, result.size());
 assertEquals(role1, result.get(0));
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity role = new RoleEntity();
 role.setId(1L);
 role.setName("oldName");
 role.setEnabled(true);
 RoleEntity updatedRole = new RoleEntity();
 updatedRole.setId(role.getId());
 updatedRole.setName("newName");
 updatedRole.setEnabled(false);

 // When
 int result = roleRepository.update(updatedRole);

 // Then
 assertEquals(1, result);
 }
}

class RoleRepositoryImpl extends RoleRepository {
 @Override
 public List<RoleEntity> findByIdIn(Collection<Long> ids) {
 return super.findByIdIn(ids);
 }

 @Override
 public List<RoleEntity> findByName(String name) {
 return super.findByName(name);
 }

 @Override
 public Page<RoleEntity> findByOrganizationId(Long organizationId, Pageable pageable) {
 return super.findByOrganizationId(organizationId, pageable);
 }

 @Override
 public Optional<RoleEntity> findByNameAndOrganizationId(String name, Long organizationId) {
 return super.findByNameAndOrganizationId(name, organizationId);
 }

 @Override
 public List<RoleEntity> findByOrganizationIdAndNameIn(Long organizationId, List<String> names) {
 return super.findByOrganizationIdAndNameIn(organizationId, names);
 }

 @Override
 public Page<RoleEntity> findByOrganizationIdAndRoleIds(@Param("organizationId") Long organizationId,
 @Param("roleIds") Collection<Long> roleIds, Pageable pageable) {
 return super.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 @Override
 public List<RoleEntity> findByUserId(@Param("userId") Long userId) {
 return super.findByUserId(userId);
 }

 @Override
 public List<RoleEntity> findByUserIdAndEnabled(@Param("userId") Long userId, @Param("enabled") Boolean enabled) {
 return super.findByUserIdAndEnabled(userId, enabled);
 }

 @Override
 public List<RoleEntity> findByRoleIdsAndEnabled(@Param("roleIds") Collection<Long> roleIds,
 @Param("enabled") Boolean enabled) {
 return super.findByRoleIdsAndEnabled(roleIds, enabled);
 }

 @Override
 @Modifying
 @Transactional
 @Query(value = "update iam_role set name=:#{#roleEntity.name}, is_enabled=:#{#roleEntity.enabled}, "
 + "description=:#{#roleEntity.description}, user_update_time=now() where id=:#{#roleEntity.id}",
 nativeQuery = true)
 int update(@Param("roleEntity") RoleEntity roleEntity) {
 return super.update(roleEntity);
 }
}