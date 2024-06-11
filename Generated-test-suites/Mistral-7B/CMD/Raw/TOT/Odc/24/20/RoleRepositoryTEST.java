import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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
 void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 void testFindByIdIn() {
 // Given
 Long[] ids = {1L, 2L};
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Mockito.when(roleRepository.findByIdIn(Mockito.any()))
 .thenReturn(Optional.of(Mockito.mock(RoleEntity.class)));

 // When
 List<RoleEntity> roles = roleRepository.findByIdIn(List.of(ids));

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByIdIn(Mockito.any());
 Assertions.assertEquals(expectedRoles, roles);
 }

 @Test
 void testFindByName() {
 // Given
 String name = "testRole";
 RoleEntity role = new RoleEntity(1L, name, true);
 Mockito.when(roleRepository.findByName(name))
 .thenReturn(Optional.of(role));

 // When
 RoleEntity roleFromRepo = roleRepository.findByName(name);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByName(name);
 Assertions.assertEquals(role, roleFromRepo);
 }

 @Test
 void testFindByNameAndOrganizationId() {
 // Given
 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity role = new RoleEntity(1L, name, organizationId, true);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId))
 .thenReturn(Optional.of(role));

 // When
 RoleEntity roleFromRepo = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByNameAndOrganizationId(name, organizationId);
 Assertions.assertEquals(role, roleFromRepo);
 }

 @Test
 void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = List.of("testRole1", "testRole2");
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1", organizationId, true),
 new RoleEntity(2L, "testRole2", organizationId, false));

 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names))
 .thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndNameIn(organizationId, names);
 Assertions.assertEquals(expectedRoles, roles);
 }

 @Test
 void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1", organizationId, true),
 new RoleEntity(2L, "testRole2", organizationId, false));
 Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
 .thenReturn(new PageImpl<>(expectedRoles));

 // When
 Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 Assertions.assertEquals(expectedRoles, roles.getContent());
 }

 @Test
 void testFindByUserId() {
 // Given
 Long userId = 1L;
 RoleEntity role = new RoleEntity(1L, "testRole", true);
 Mockito.when(roleRepository.findByUserId(userId))
 .thenReturn(Optional.of(role));

 // When
 List<RoleEntity> roles = roleRepository.findByUserId(userId);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByUserId(userId);
 Assertions.assertEquals(role, roles.get(0));
 }

 @Test
 void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 RoleEntity role = new RoleEntity(1L, "testRole", enabled);
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled))
 .thenReturn(List.of(role));

 // When
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByUserIdAndEnabled(userId, enabled);
 Assertions.assertEquals(role, roles.get(0));
 }

 @Test
 void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = List.of(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "testRole1", true),
 new RoleEntity(2L, "testRole2", true));

 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled))
 .thenReturn(expectedRoles);

 // When
 List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 Mockito.verify(roleRepository, Mockito.times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
 Assertions.assertEquals(expectedRoles, roles);
 }

 @Test
 void testUpdate() {
 // Given
 RoleEntity role = new RoleEntity(1L, "testRole", true);

 Executable executable = () -> roleRepository.update(role);

 // Then (expect exception)
 Assertions.assertThrows(UnsupportedOperationException.class, executable);
 }
}