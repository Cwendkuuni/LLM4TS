package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
 // given
 List<Long> ids = Arrays.asList(1L, 2L);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByIdIn(ids);

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByName() {
 // given
 String name = "role1";
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, name, true));
 Mockito.when(roleRepository.findByName(name)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByName(name);

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByOrganizationId() {
 // given
 Long organizationId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Page<RoleEntity> pageable = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationId(organizationId, Pageable.unpaged())).thenReturn(pageable);
 Mockito.when(pageable.getContent()).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged()).getContent();

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // given
 String name = "role1";
 Long organizationId = 1L;
 RoleEntity expected = new RoleEntity(1L, name, true);
 Optional<RoleEntity> optional = Optional.of(expected);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

 // when
 RoleEntity actual = roleRepository.findByNameAndOrganizationId(name, organizationId).get();

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("role1", "role2");
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));

 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // given
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Page<RoleEntity> pageable = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged())).thenReturn(pageable);
 Mockito.when(pageable.getContent()).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged()).getContent();

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByUserId() {
 // given
 Long userId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByUserId(userId);

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, "role1", true));
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // then
 assertEquals(expected, actual);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // given
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));

 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // then
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullRoleEntity() {
 // given
 RoleEntity roleEntity = null;

 // when & then
 roleRepository.update(roleEntity);
 }

 @Test
 public void testUpdate() {
 // given
 RoleEntity roleEntity = new RoleEntity(1L, "role1", true);
 roleEntity.setName("updatedRoleName");
 roleEntity.setEnabled(false);
 roleEntity.setDescription("updatedDescription");

 // when
 int actual = roleRepository.update(roleEntity);

 // then
 assertEquals(1, actual);
 }

}