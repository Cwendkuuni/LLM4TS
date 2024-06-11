import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @org.junit.Before
 public void setup() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 public void testFindByIdIn() {
 // Arrange
 Long id1 = 1L;
 Long id2 = 2L;
 List<Long> ids = List.of(id1, id2);
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());

 // Act
 List<RoleEntity> actualResult = roleRepository.findByIdIn(ids);

 // Assert
 Mockito.verify(roleRepository).findByIdIn(ids);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByName() {
 // Arrange
 String name = "testRole";
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());

 // Act
 Executable findByNameExecutable = () -> roleRepository.findByName(name);

 // Assert
 Mockito.when(roleRepository.findByName(name)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = findByNameExecutable.get();
 Mockito.verify(roleRepository).findByName(name);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByOrganizationId() {
 // Arrange
 Long organizationId = 1L;
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedResult = Mockito.mock(Page.class);

 // Act
 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedResult);
 Page<RoleEntity> actualResult = roleRepository.findByOrganizationId(organizationId, pageable);

 // Assert
 Mockito.verify(roleRepository).findByOrganizationId(organizationId, pageable);
 Mockito.verifyNoMoreInteractions(roleRepository); // verify no more calls to findByOrganizationId
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Arrange
 String name = "testRole";
 Long organizationId = 1L;
 RoleEntity expectedResult = new RoleEntity();

 // Act
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedResult));
 Optional<RoleEntity> actualResult = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Assert
 Mockito.verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
 Mockito.verifyNoMoreInteractions(roleRepository); // verify no more calls to findByNameAndOrganizationId
 Assertions.assertEquals(expectedResult, actualResult.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Arrange
 Long organizationId = 1L;
 List<String> names = List.of("testRole1", "testRole2");
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());

 // Act
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Assert
 Mockito.verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
 Mockito.verifyNoMoreInteractions(roleRepository); // verify no more calls to findByOrganizationIdAndNameIn
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Arrange
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> expectedResult = Mockito.mock(Page.class);

 // Act
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(expectedResult);
 Page<RoleEntity> actualResult = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Assert
 Mockito.verify(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 Mockito.verifyNoMoreInteractions(roleRepository); // verify no more calls to findByOrganizationIdAndRoleIds
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByUserId() {
 // Arrange
 Long userId = 1L;
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());

 // Act
 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByUserId(userId);

 // Assert
 Mockito.verify(roleRepository).findByUserId(userId);
 Mockito.verifyNoMoreInteractions(roleRepository); // verify no more calls to findByUserId
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Arrange
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());

 // Act
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByUserIdAndEnabled(userId, enabled);

 // Assert
 Mockito.verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
 Mockito.verifyNoMoreInteractions(roleRepository); // verify no more calls to findByUserIdAndEnabled
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Arrange
 Collection<Long> roleIds = List.of(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> expectedResult = List.of(new RoleEntity(), new RoleEntity());

 // Act
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);
 List<RoleEntity> actualResult = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 // Assert
 Mockito.verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
 Mockito.verifyNoMoreInteractions(roleRepository); // verify no more calls to findByRoleIdsAndEnabled
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testUpdate() {
 // Arrange
 RoleEntity roleEntity = new RoleEntity();

 // Act
 int actualResult = roleRepository.update(roleEntity);

 // Assert
 Mockito.verify(roleRepository).update(roleEntity);
 Assertions.assertEquals(1, actualResult);
 }
}