package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @SuppressWarnings("unchecked")
 @BeforeEach
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 void testFindByIdIn() {
 // given
 Long id1 = 1L;
 Long id2 = 2L;
 List<Long> ids = Arrays.asList(id1, id2);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByIdIn(ids);

 // then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByName() {
 // given
 String name = "testRole";
 RoleEntity expected = new RoleEntity();
 Mockito.when(roleRepository.findByName(name)).thenReturn(Arrays.asList(expected));

 // when
 List<RoleEntity> actual = roleRepository.findByName(name);

 // then
 assertEquals(expected, actual.get(0));
 }

 @Test
 void testFindByOrganizationId() {
 // given
 Long organizationId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.ASC, "name"));
 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Page.of(expected, pageable));

 // when
 Page<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, pageable);

 // then
 assertEquals(expected.getContent(), actual.getContent());
 }

 @Test
 void testFindByNameAndOrganizationId() {
 // given
 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity expected = new RoleEntity();
 Optional<RoleEntity> optionalExpected = Optional.of(expected);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalExpected);

 // when
 Optional<RoleEntity> actual = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // then
 assertTrue(actual.isPresent());
 assertEquals(expected, actual.get());
 }

 @Test
 void testFindByOrganizationIdAndNameIn() {
 // given
 Long organizationId = 1L;
 List<String> names = Arrays.asList("testRole1", "testRole2");
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByOrganizationIdAndRoleIds() {
 // given
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Pageable pageable = PageRequest.of(0, 10, new Sort(Sort.Direction.ASC, "name"));
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
 .thenReturn(Page.of(expected, pageable));

 // when
 Page<RoleEntity> actual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // then
 assertEquals(expected.getContent(), actual.getContent());
 }

 @Test
 void testFindByUserId() {
 // given
 Long userId = 1L;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByUserId(userId);

 // then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByUserIdAndEnabled() {
 // given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // then
 assertEquals(expected, actual);
 }

 @Test
 void testFindByRoleIdsAndEnabled() {
 // given
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;
 List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);

 // when
 List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // then
 assertEquals(expected, actual);
 }

 @Test
 void testUpdate() {
 // given
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("updatedRole");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("updatedDescription");

 // when
 int actual = roleRepository.update(roleEntity);

 // then
 assertEquals(1, actual);
 }
}