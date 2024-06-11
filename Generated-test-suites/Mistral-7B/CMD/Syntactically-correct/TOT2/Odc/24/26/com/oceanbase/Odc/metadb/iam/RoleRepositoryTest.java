import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryTest {

 @InjectMocks
 private RoleRepository roleRepository;

 @Test
 void testFindByIdIn() {
 RoleEntity role1 = new RoleEntity();
 RoleEntity role2 = new RoleEntity();
 Mockito.when(roleRepository.findByIdIn(Arrays.asList(role1.getId(), role2.getId())))
 .thenReturn(Arrays.asList(role1, role2));

 List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(role1.getId(), role2.getId()));
 Assertions.assertEquals(Arrays.asList(role1, role2), result);
 }

 @Test
 void testFindByName() {
 String name = "TestRole";
 RoleEntity role = new RoleEntity();
 role.setName(name);
 Mockito.when(roleRepository.findByName(name))
 .thenReturn(Collections.singletonList(role));

 List<RoleEntity> result = roleRepository.findByName(name);
 Assertions.assertEquals(Collections.singletonList(role), result);
 }

 @Test
 void testFindByOrganizationId() {
 Long organizationId = 1L;
 RoleEntity role1 = new RoleEntity();
 RoleEntity role2 = new RoleEntity();
 Mockito.when(roleRepository.findByOrganizationId(organizationId, Mockito.any(Pageable.class)))
 .thenReturn(Mockito.mock(Page.class));

 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, Pageable.ofSize(10, Sort.by("id").descending()));
 Assertions.assertNotNull(result);
 }

 @Test
 void testFindByNameAndOrganizationId() {
 String name = "TestRole";
 Long organizationId = 1L;
 RoleEntity role = new RoleEntity();
 role.setName(name);
 role.setOrganizationId(organizationId);
 Optional<RoleEntity> optionalRole = Optional.of(role);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId))
 .thenReturn(optionalRole);

 RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).orElse(null);
 Assertions.assertEquals(role, result);
 }

 @Test
 void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("TestRole1", "TestRole2");
 RoleEntity role1 = new RoleEntity();
 RoleEntity role2 = new RoleEntity();
 role1.setName(names.get(0));
 role1.setOrganizationId(organizationId);
 role2.setName(names.get(1));
 role2.setOrganizationId(organizationId);
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names))
 .thenReturn(Arrays.asList(role1, role2));

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 Assertions.assertEquals(Arrays.asList(role1, role2), result);
 }

 @Test
 void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 List<Long> roleIds = Arrays.asList(1L, 2L);
 RoleEntity role1 = new RoleEntity();
 RoleEntity role2 = new RoleEntity();
 role1.setId(roleIds.get(0));
 role1.setOrganizationId(organizationId);
 role2.setId(roleIds.get(1));
 role2.setOrganizationId(organizationId);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
 .thenReturn(Mockito.mock(Page.class));

 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.ofSize(10, Sort.by("id").descending()));
 Assertions.assertNotNull(result);
 }

 @Test
 void testFindByUserId() {
 Long userId = 1L;
 RoleEntity role1 = new RoleEntity();
 RoleEntity role2 = new RoleEntity();
 Mockito.when(roleRepository.findByUserId(userId))
 .thenReturn(Arrays.asList(role1, role2));

 List<RoleEntity> result = roleRepository.findByUserId(userId);
 Assertions.assertEquals(Arrays.asList(role1, role2), result);
 }

 @Test
 void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 RoleEntity role = new RoleEntity();
 role.setUserId(userId);
 role.setEnabled(enabled);
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled))
 .thenReturn(Collections.singletonList(role));

 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 Assertions.assertEquals(Collections.singletonList(role), result);
 }

 @Test
 void testFindByRoleIdsAndEnabled() {
 List<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 RoleEntity role1 = new RoleEntity();
 role1.setId(roleIds.get(0));
 role1.setEnabled(enabled);
 RoleEntity role2 = new RoleEntity();
 role2.setId(roleIds.get(1));
 role2.setEnabled(enabled);
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled))
 .thenReturn(Arrays.asList(role1, role2));

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 Assertions.assertEquals(Arrays.asList(role1, role2), result);
 }

 @Test
 void testUpdate() {
 Long id = 1L;
 RoleEntity role = new RoleEntity();
 role.setId(id);
 role.setName("TestRole");
 role.setEnabled(true);
 role.setDescription("Test Description");
 Mockito.when(roleRepository.update(role))
 .thenReturn(1);

 int result = roleRepository.update(role);
 Assertions.assertEquals(1, result);
 }
}