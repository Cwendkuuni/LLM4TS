import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @org.junit.jupiter.api.BeforeEach
 void setUp() {
 roleRepository = Mockito.mock(RoleRepository.class);
 }

 @Test
 void testFindByIdIn() {
 // Prepare test data
 Long id1 = 1L;
 Long id2 = 2L;
 List<RoleEntity> roleEntities = List.of(new RoleEntity(id1, "role1"), new RoleEntity(id2, "role2"));
 Mockito.when(roleRepository.findByIdIn(Mockito.any()))
 .thenReturn(Optional.of(roleEntities));

 // Test
 Collection<Long> ids = List.of(id1, id2);
 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 // Verify
 Assertions.assertEquals(2, result.size());
 }

 @Test
 void testFindByName() {
 // Prepare test data
 String name = "role1";
 RoleEntity roleEntity = new RoleEntity(1L, name);
 Mockito.when(roleRepository.findByName(name))
 .thenReturn(List.of(roleEntity));

 // Test
 List<RoleEntity> result = roleRepository.findByName(name);

 // Verify
 Assertions.assertEquals(1, result.size());
 Assertions.assertEquals(name, result.get(0).getName());
 }

 @Test
 void testFindByOrganizationId() {
 // Prepare test data
 Long organizationId = 1L;
 Page<RoleEntity> page = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationId(organizationId, Mockito.any()))
 .thenReturn(page);

 // Test
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 // Verify
 Mockito.verify(page).getContent();
 }

 @Test
 void testFindByNameAndOrganizationId() {
 // Prepare test data
 String name = "role1";
 Long organizationId = 1L;
 RoleEntity roleEntity = new RoleEntity(1L, name, organizationId);
 Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId))
 .thenReturn(Optional.of(roleEntity));

 // Test
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

 // Verify
 Assertions.assertTrue(result.isPresent());
 }

 @Test
 void testFindByOrganizationIdAndNameIn() {
 // Prepare test data
 Long organizationId = 1L;
 List<String> names = List.of("role1", "role2");
 List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "role1", organizationId),
 new RoleEntity(2L, "role2", organizationId));
 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names))
 .thenReturn(roleEntities);

 // Test
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 // Verify
 Assertions.assertEquals(2, result.size());
 }

 @Test
 void testFindByOrganizationIdAndRoleIds() {
 // Prepare test data
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 Page<RoleEntity> page = Mockito.mock(Page.class);
 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any()))
 .thenReturn(page);

 // Test
 Pageable pageable = Mockito.mock(Pageable.class);
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 // Verify
 Mockito.verify(page).getContent();
 }

 @Test
 void testFindByUserId() {
 // Prepare test data
 Long userId = 1L;
 List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
 Mockito.when(roleRepository.findByUserId(userId))
 .thenReturn(roleEntities);

 // Test
 List<RoleEntity> result = roleRepository.findByUserId(userId);

 // Verify
 Assertions.assertEquals(2, result.size());
 }

 @Test
 void testFindByUserIdAndEnabled() {
 // Prepare test data
 Long userId = 1L;
 List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "role1", false),
 new RoleEntity(2L, "role2", true));
 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, true))
 .thenReturn(roleEntities);

 // Test
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, true);

 // Verify
 Assertions.assertEquals(1, result.size());
 }

 @Test
 void testFindByRoleIdsAndEnabled() {
 // Prepare test data
 Collection<Long> roleIds = List.of(1L, 2L);
 List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "role1", false),
 new RoleEntity(2L, "role2", true));
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, true))
 .thenReturn(roleEntities);

 // Test
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

 // Verify
 Assertions.assertEquals(1, result.size());
 }

 @Test
 void testUpdate() {
 // Prepare test data
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("newName");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("newDescription");

 // Test
 int result = roleRepository.update(roleEntity);

 // Verify
 Mockito.verify(roleRepository, Mockito.times(1))
 .update(Mockito.argThat(r -> r.getName().equals("newName") && r.isEnabled() == true
 && r.getDescription().equals("newDescription") && r.getId().equals(1L)));
 }
}