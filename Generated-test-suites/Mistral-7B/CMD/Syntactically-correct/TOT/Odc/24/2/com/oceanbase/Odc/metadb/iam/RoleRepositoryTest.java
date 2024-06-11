import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

 private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);

 @Test
 public void testFindByIdIn() {
 Mockito.when(roleRepository.findByIdIn(Mockito.anyCollection()))
 .thenReturn(List.of(new RoleEntity(), new RoleEntity()));

 List<Long> ids = List.of(1L, 2L);
 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testFindByName() {
 Mockito.when(roleRepository.findByName(Mockito.anyString()))
 .thenReturn(List.of(new RoleEntity()));

 String name = "test";
 List<RoleEntity> result = roleRepository.findByName(name);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(1, result.size());
 }

 @Test
 public void testFindByOrganizationId() {
 Pageable pageable = Mockito.mock(Pageable.class);
 Long organizationId = 1L;

 Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable))
 .thenReturn(new PageImpl<>(List.of(new RoleEntity()), pageable, 1));

 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(1, result.getContent().size());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> roleEntityOptional = Optional.ofNullable(new RoleEntity());

 Mockito.when(roleRepository.findByNameAndOrganizationId(Mockito.anyString(), Mockito.anyLong()))
 .thenReturn(roleEntityOptional);

 String name = "test";
 Long organizationId = 1L;
 RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).orElse(null);

 Assertions.assertNotNull(result);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());

 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, Mockito.anyList()))
 .thenReturn(roleEntities);

 Long organizationId = 1L;
 List<String> names = List.of("test1", "test2");
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Pageable pageable = Mockito.mock(Pageable.class);
 Long organizationId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);

 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
 .thenReturn(new PageImpl<>(List.of(new RoleEntity()), pageable, 1));

 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(2, result.getContent().size());
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());

 Mockito.when(roleRepository.findByUserId(1L))
 .thenReturn(roleEntities);

 Long userId = 1L;
 List<RoleEntity> result = roleRepository.findByUserId(userId);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());

 Mockito.when(roleRepository.findByUserIdAndEnabled(1L, true))
 .thenReturn(roleEntities);

 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());

 Mockito.when(roleRepository.findByRoleIdsAndEnabled(Mockito.anyCollection(), true))
 .thenReturn(roleEntities);

 Collection<Long> roleIds = List.of(1L, 2L);
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

 Assertions.assertNotNull(result);
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();

 Mockito.doAnswer(invocation -> {
 int rowsAffected = 1;
 return rowsAffected;
 })
 .when(roleRepository)
 .update(Mockito.any(RoleEntity.class));

 RoleEntity updatedRoleEntity = roleRepository.update(roleEntity);

 Assertions.assertNotNull(updatedRoleEntity);
 }
}