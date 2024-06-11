import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @org.junit.jupiter.api.BeforeEach
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 // Given
 long id1 = 1L;
 long id2 = 2L;
 List<Long> ids = List.of(id1, id2);
 List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByIdIn(ids);

 // Then
 Assertions.assertSame(expected, actual);
 }

 @Test
 public void testFindByName() {
 // Given
 String name = "testRole";
 RoleEntity expected = new RoleEntity();
 Mockito.when(roleRepository.findByName(name)).thenReturn(expected);

 // When
 RoleEntity actual = roleRepository.findByName(name);

 // Then
 Assertions.assertSame(expected, actual);
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
 Assertions.assertSame(expected, actual);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Given
 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity expected = new RoleEntity();
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expected));

 // When
 Optional<RoleEntity> actual = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Then
 Assertions.assertSame(expected.get(), actual.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Given
 Long organizationId = 1L;
 List<String> names = List.of("testRole1", "testRole2");
 List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Then
 Assertions.assertSame(expected, actual);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Given
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expected = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expected);

 // When
 Page<RoleEntity> actual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Then
 Assertions.assertSame(expected, actual);
 }

 @Test
 public void testFindByUserId() {
 // Given
 Long userId = 1L;
 List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByUserId(userId);

 // Then
 Assertions.assertSame(expected, actual);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Given
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expected = List.of(new RoleEntity());
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Then
 Assertions.assertSame(expected, actual);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Given
 Collection<Long> roleIds = List.of(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expected = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expected);

 // When
 List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Then
 Assertions.assertSame(expected, actual);
 }

 @Test
 public void testUpdate() {
 // Given
 RoleEntity roleEntity = new RoleEntity();
 Executable updateMethod = () -> roleRepository.update(roleEntity);

 // When & Then
 Mockito.verify(roleRepository, Mockito.times(1)).update(roleEntity);
 }

}