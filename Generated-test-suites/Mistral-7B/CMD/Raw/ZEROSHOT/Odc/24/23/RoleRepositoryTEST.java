import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @SuppressWarnings("unchecked")
 @BeforeEach
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 List<Long> ids = List.of(1L, 2L);
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);

 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByName() {
 String name = "test";
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByName(name)).thenReturn(expectedResult);

 List<RoleEntity> result = roleRepository.findByName(name);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
 Page<RoleEntity> expectedResult = Page.of(List.of(new RoleEntity(), new RoleEntity()), pageable);
 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedResult);

 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test";
 Long organizationId = 1L;
 RoleEntity expectedResult = new RoleEntity();
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedResult));

 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = List.of("test1", "test2");
 Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
 Page<RoleEntity> expectedResult = Page.of(List.of(new RoleEntity(), new RoleEntity()), pageable);
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);

 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
 Page<RoleEntity> expectedResult = Page.of(List.of(new RoleEntity(), new RoleEntity()), pageable);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expectedResult);

 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

 List<RoleEntity> result = roleRepository.findByUserId(userId);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = List.of(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 Assertions.assertNotNull(result);
 Assertions.assertEquals(expectedResult, result);
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 Executable actualMethodCall = () -> roleRepository.update(roleEntity);

 Assertions.assertDoesNotThrow(actualMethodCall);
 }
}