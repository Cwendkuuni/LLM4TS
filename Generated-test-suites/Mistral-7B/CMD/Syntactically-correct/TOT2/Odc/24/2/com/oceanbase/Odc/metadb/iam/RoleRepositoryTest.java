import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@JpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

 @Autowired
 private RoleRepository roleRepository;

 @Test
 public void testFindByIdIn() {
 RoleEntity role1 = new RoleEntity();
 role1.setId(1L);
 RoleEntity role2 = new RoleEntity();
 role2.setId(2L);
 RoleEntity role3 = new RoleEntity();
 role3.setId(3L);

 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);

 List<RoleEntity> expectedRoles = List.of(role1, role2);
 List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);

 Assertions.assertNotNull(actualRoles);
 Assertions.assertIterableEquals(expectedRoles, actualRoles);
 }

 @Test
 public void testFindByName() {
 RoleEntity roleExpected = new RoleEntity();
 roleExpected.setName("admin");
 roleExpected.setId(1L);

 RoleEntity roleActual = roleRepository.findByName("admin").orElse(null);

 Assertions.assertNotNull(roleActual);
 Assertions.assertEquals(roleExpected, roleActual);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);

 Page<RoleEntity> expectedRoles = Page.of(new Page<RoleEntity>(List.of(new RoleEntity(), new RoleEntity(), new RoleEntity()), 0, 1, 3), "nextPage");
 Page<RoleEntity> actualRoles = roleRepository.findByOrganizationId(organizationId, pageable);

 Assertions.assertEquals(expectedRoles.getContent().size(), actualRoles.getContent().size());
 for (int i = 0; i < expectedRoles.getContent().size(); i++) {
 Assertions.assertEquals(expectedRoles.getContent().get(i), actualRoles.getContent().get(i));
 }
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 RoleEntity roleExpected = new RoleEntity();
 roleExpected.setName("admin");
 roleExpected.setOrganizationId(1L);
 roleExpected.setId(1L);

 Optional<RoleEntity> roleActual = roleRepository.findByNameAndOrganizationId("admin", 1L);

 Assertions.assertTrue(roleActual.isPresent());
 Assertions.assertEquals(roleExpected, roleActual.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("admin");
 names.add("user");

 List<RoleEntity> expectedRoles = List.of(new RoleEntity(), new RoleEntity());
 List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 Assertions.assertEquals(expectedRoles.size(), actualRoles.size());
 for (int i = 0; i < expectedRoles.size(); i++) {
 Assertions.assertEquals(expectedRoles.get(i), actualRoles.get(i));
 }
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);

 Page<RoleEntity> expectedRoles = Page.of(new Page<RoleEntity>(List.of(new RoleEntity(), new RoleEntity()), 0, 1, 2), "nextPage");
 Page<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));

 Assertions.assertEquals(expectedRoles.getContent().size(), actualRoles.getContent().size());
 for (int i = 0; i < expectedRoles.getContent().size(); i++) {
 Assertions.assertEquals(expectedRoles.getContent().get(i), actualRoles.getContent().get(i));
 }
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> expectedRoles = List.of(new RoleEntity());
 List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

 Assertions.assertEquals(expectedRoles.size(), actualRoles.size());
 for (int i = 0; i < expectedRoles.size(); i++) {
 Assertions.assertEquals(expectedRoles.get(i), actualRoles.get(i));
 }
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> expectedRoles = List.of(new RoleEntity());
 List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

 Assertions.assertEquals(expectedRoles.size(), actualRoles.size());
 for (int i = 0; i < expectedRoles.size(); i++) {
 Assertions.assertEquals(expectedRoles.get(i), actualRoles.get(i));
 }
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;

 List<RoleEntity> expectedRoles = List.of(new RoleEntity(), new RoleEntity());
 List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 Assertions.assertEquals(expectedRoles.size(), actualRoles.size());
 for (int i = 0; i < expectedRoles.size(); i++) {
 Assertions.assertEquals(expectedRoles.get(i), actualRoles.get(i));
 }
 }

 @Test
 public void testUpdate() {
 RoleEntity roleExpected = new RoleEntity();
 roleExpected.setName("admin");
 roleExpected.setIsEnabled(true);
 roleExpected.setDescription("Super Admin Role");
 roleExpected.setId(1L);

 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("testRole");
 roleEntity.setIsEnabled(false);
 roleEntity.setDescription("Test Role");
 roleEntity.setId(null);

 int rowsAffected = roleRepository.update(roleEntity);
 RoleEntity roleActual = roleRepository.findById(1L).orElse(null);

 Assertions.assertEquals(1, rowsAffected);
 Assertions.assertEquals(roleExpected, roleActual);
 }

 @Test(expected = RuntimeException.class)
 public void testUpdateWithNullEntity() {
 RoleEntity roleEntity = null;
 int rowsAffected = roleRepository.update(roleEntity);
 }

 @Test(expected = RuntimeException.class)
 public void testUpdateWithNullId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(null);
 int rowsAffected = roleRepository.update(roleEntity);
 }
}