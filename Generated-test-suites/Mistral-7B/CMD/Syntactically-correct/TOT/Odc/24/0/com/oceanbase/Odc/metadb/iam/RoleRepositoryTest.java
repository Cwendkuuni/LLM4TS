import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Test
 public void testFindByIdIn() {
 RoleEntity roleEntity1 = new RoleEntity();
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity2.setId(2L);

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 Mockito.when(roleRepository.findByIdIn(Mockito.any()))
 .thenReturn(java.util.Stream.of(roleEntity1, roleEntity2).collect(Collectors.toList()));

 List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
 Assertions.assertEquals(2, result.size());
 Assertions.assertEquals(roleEntity1, result.get(0));
 Assertions.assertEquals(roleEntity2, result.get(1));
 }

 @Test
 public void testFindByName() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("testRole");
 roleEntity.setId(1L);

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 Mockito.when(roleRepository.findByName(Mockito.eq("testRole")))
 .thenReturn(java.util.Collections.singletonList(roleEntity));

 RoleEntity result = roleRepository.findByName("testRole").get();
 Assertions.assertEquals(roleEntity, result);
 }

 @Test
 public void testFindByOrganizationId() {
 RoleEntity roleEntity1 = new RoleEntity();
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity1.setOrganizationId(1L);
 roleEntity1.setId(1L);
 roleEntity2.setOrganizationId(1L);
 roleEntity2.setId(2L);

 Page<RoleEntity> page = new PageImpl<>(Arrays.asList(roleEntity1, roleEntity2), new Pageable() {
 @Override
 public int getPageNumber() {
 return 0;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public long getOffSet() {
 return 0;
 }

 @Override
 public Pageable nextPageable() {
 return null;
 }

 @Override
 public boolean hasPrevious() {
 return false;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public Sort getSort() {
 return null;
 }

 @Override
 public Pageable previousPageable() {
 return null;
 }

 @Override
 public boolean isFirst() {
 return true;
 }

 @Override
 public boolean isLast() {
 return false;
 }

 @Override
 public boolean hasContent() {
 return true;
 }

 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public boolean isEmpty() {
 return false;
 }

 @Override
 public Object[] getContent() {
 return new Object[] { roleEntity1, roleEntity2 };
 }

 @Override
 public long getTotalElements() {
 return 2;
 }

 @Override
 public long getNumberOfElements() {
 return 2;
 }
 }, new Pageable() {
 @Override
 public int getPageNumber() {
 return 0;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public long getOffSet() {
 return 0;
 }

 @Override
 public Pageable nextPageable() {
 return null;
 }

 @Override
 public boolean hasPrevious() {
 return false;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public Sort getSort() {
 return null;
 }

 @Override
 public Pageable previousPageable() {
 return null;
 }

 @Override
 public boolean isFirst() {
 return true;
 }

 @Override
 public boolean isLast() {
 return false;
 }

 @Override
 public boolean hasContent() {
 return true;
 }

 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public boolean isEmpty() {
 return false;
 }
 });

 Mockito.when(roleRepository.findByOrganizationId(Mockito.eq(1L), Mockito.any()))
 .thenReturn(page);

 Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
 Assertions.assertEquals(2, result.getTotalElements());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleEntity.setId(1L);

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 Mockito.when(roleRepository.findByNameAndOrganizationId(Mockito.eq("testRole"), Mockito.eq(1L)))
 .thenReturn(Optional.of(roleEntity));

 RoleEntity result = roleRepository.findByNameAndOrganizationId("testRole", 1L).get();
 Assertions.assertEquals(roleEntity, result);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 RoleEntity roleEntity1 = new RoleEntity();
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity1.setOrganizationId(1L);
 roleEntity1.setName("role1");
 roleEntity1.setId(1L);
 roleEntity2.setOrganizationId(1L);
 roleEntity2.setName("role2");
 roleEntity2.setId(2L);

 List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2);

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(Mockito.eq(1L), Mockito.any()))
 .thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("role1", "role2"));
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 RoleEntity roleEntity1 = new RoleEntity();
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity1.setOrganizationId(1L);
 roleEntity1.setId(1L);
 roleEntity2.setOrganizationId(1L);
 roleEntity2.setId(2L);

 Page<RoleEntity> page = new PageImpl<>(Arrays.asList(roleEntity1, roleEntity2), new Pageable() {
 @Override
 public int getPageNumber() {
 return 0;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public long getOffSet() {
 return 0;
 }

 @Override
 public Pageable nextPageable() {
 return null;
 }

 @Override
 public boolean hasPrevious() {
 return false;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public Sort getSort() {
 return null;
 }

 @Override
 public Pageable previousPageable() {
 return null;
 }

 @Override
 public boolean isFirst() {
 return true;
 }

 @Override
 public boolean isLast() {
 return false;
 }

 @Override
 public boolean hasContent() {
 return true;
 }

 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public boolean isEmpty() {
 return false;
 }

 @Override
 public Object[] getContent() {
 return new Object[] { roleEntity1, roleEntity2 };
 }

 @Override
 public long getTotalElements() {
 return 2;
 }

 @Override
 public long getNumberOfElements() {
 return 2;
 }
 }, new Pageable() {
 @Override
 public int getPageNumber() {
 return 0;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public long getOffSet() {
 return 0;
 }

 @Override
 public Pageable nextPageable() {
 return null;
 }

 @Override
 public boolean hasPrevious() {
 return false;
 }

 @Override
 public int getPageSize() {
 return 10;
 }

 @Override
 public Sort getSort() {
 return null;
 }

 @Override
 public Pageable previousPageable() {
 return null;
 }

 @Override
 public boolean isFirst() {
 return true;
 }

 @Override
 public boolean isLast() {
 return false;
 }

 @Override
 public boolean hasContent() {
 return true;
 }

 @Override
 public boolean hasNext() {
 return false;
 }

 @Override
 public boolean isEmpty() {
 return false;
 }
 });

 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(Mockito.eq(1L), Mockito.any(), Mockito.any()))
 .thenReturn(page);

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), Pageable.unpaged())
 .getContent();
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testFindByUserId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 Mockito.when(roleRepository.findByUserId(Mockito.eq(1L)))
 .thenReturn(Arrays.asList(roleEntity));

 List<RoleEntity> result = roleRepository.findByUserId(1L);
 Assertions.assertEquals(1, result.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setEnabled(true);

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 Mockito.when(roleRepository.findByUserIdAndEnabled(Mockito.eq(1L), Mockito.eq(true)))
 .thenReturn(Arrays.asList(roleEntity));

 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
 Assertions.assertEquals(1, result.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 RoleEntity roleEntity1 = new RoleEntity();
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setEnabled(true);
 roleEntity2.setId(2L);
 roleEntity2.setEnabled(false);

 List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2);

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 Mockito.when(roleRepository.findByRoleIdsAndEnabled(Mockito.any(), Mockito.eq(true)))
 .thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
 Assertions.assertEquals(1, result.size());
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("testRole");
 roleEntity.setId(1L);
 roleEntity.setEnabled(true);
 roleEntity.setDescription("test description");
 roleEntity.setUserUpdateTime(new Date());

 MockitoAnnotations.initMocks(this);
 roleRepository = Mockito.mock(RoleRepository.class);

 RoleEntity updatedRoleEntity = new RoleEntity();
 updatedRoleEntity.setName("updatedTestRole");
 updatedRoleEntity.setEnabled(true);
 updatedRoleEntity.setDescription("updated test description");
 updatedRoleEntity.setId(1L);
 updatedRoleEntity.setUserUpdateTime(new Date());

 Mockito.when(roleRepository.update(Mockito.eq(roleEntity)))
 .thenReturn(1);

 Executable executable = () -> roleRepository.update(roleEntity);
 Assertions.assertEquals(1, executable.execute());
 }
}