import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @SuppressWarnings("unchecked")
 @BeforeEach
 void setup() {
 roleRepository = Mockito.spy(RoleRepository.class);
 }

 @Test
 void testFindByIdIn() {
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Mockito.when(roleRepository.findByIdIn(ArgumentMatchers.any(Collection.class)))
 .thenReturn(expectedRoles);
 List<RoleEntity> result = roleRepository.findByIdIn(List.of(1L, 2L));
 Assertions.assertEquals(expectedRoles, result);
 }

 @Test
 void testFindByName() {
 RoleEntity expectedRole = new RoleEntity(1L, "admin", true);
 Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString()))
 .thenReturn(List.of(expectedRole));
 RoleEntity result = roleRepository.findByName("admin").get();
 Assertions.assertEquals(expectedRole, result);
 }

 @Test
 void testFindByOrganizationId() {
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Pageable pageable = Mockito.mock(Pageable.class);
 Mockito.when(roleRepository.findByOrganizationId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
 .thenReturn(Mockito.argThat(page -> page.getContent().equals(expectedRoles)));
 Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
 Assertions.assertEquals(expectedRoles, result.getContent());
 }

 @Test
 void testFindByNameAndOrganizationId() {
 RoleEntity expectedRole = new RoleEntity(1L, "admin", true);
 Mockito.when(roleRepository.findByNameAndOrganizationId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(expectedRole));
 RoleEntity result = roleRepository.findByNameAndOrganizationId("admin", 1L).get();
 Assertions.assertEquals(expectedRole, result);
 }

 @Test
 void testFindByOrganizationIdAndNameIn() {
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(ArgumentMatchers.anyLong(), ArgumentMatchers.any(List.class)))
 .thenReturn(expectedRoles);
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, List.of("role1", "role2"));
 Assertions.assertEquals(expectedRoles, result);
 }

 @Test
 void testFindByOrganizationIdAndRoleIds() {
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 Specification<RoleEntity> spec = Mockito.mock(Specification.class);
 Mockito.when(roleRepository.findAll(spec)).thenReturn(expectedRoles);
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
 Assertions.assertEquals(expectedRoles, result.getContent());
 }

 @Test
 void testFindByUserId() {
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Long userId = 1L;
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);
 List<RoleEntity> result = roleRepository.findByUserId(userId);
 Assertions.assertEquals(expectedRoles, result);
 }

 @Test
 void testFindByUserIdAndEnabled() {
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true));
 Long userId = 1L;
 Boolean enabled = true;
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 Assertions.assertEquals(expectedRoles, result);
 }

 @Test
 void testFindByRoleIdsAndEnabled() {
 List<RoleEntity> expectedRoles = List.of(new RoleEntity(1L, "role1", true), new RoleEntity(2L, "role2", false));
 Collection<Long> roleIds = List.of(1L, 2L);
 Boolean enabled = true;
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 Assertions.assertEquals(expectedRoles, result);
 }

 @Test
 void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 int result = roleRepository.update(roleEntity);
 Assertions.assertEquals(1, result);
 }

 @Test
 void testUpdateWithNullRoleEntity() {
 Executable testMethod = () -> roleRepository.update(null);
 Assertions.assertThrows(NullPointerException.class, testMethod);
 }
}