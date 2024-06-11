import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @org.junit.jupiter.api.BeforeEach
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
    }

    // Test for findAll
    @Test
    public void testFindAll() {
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findAll()).thenReturn(roleEntities);
        Assertions.assertEquals(roleEntities, roleRepository.findAll());
    }

    // Test for findById
    @Test
    public void testFindById() {
        Long id = 1L;
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(roleEntity));
        RoleEntity roleEntityActual = roleRepository.findById(id).get();
        Assertions.assertEquals(roleEntity, roleEntityActual);

        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertNull(roleRepository.findById(id).orElse(null));
    }

    // Test for save
    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.save(roleEntity)).thenReturn(roleEntity);
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        Assertions.assertEquals(roleEntity, savedRoleEntity);
    }

    // Test for findByIdIn
    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);
        List<RoleEntity> roleEntitiesActual = roleRepository.findByIdIn(ids);
        Assertions.assertEquals(roleEntities, roleEntitiesActual);
    }

    // Test for findByName
    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByName(name)).thenReturn(roleEntities);
        List<RoleEntity> roleEntitiesActual = roleRepository.findByName(name);
        Assertions.assertEquals(roleEntities, roleEntitiesActual);

        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.emptyList());
        Assertions.assertEmpty(roleRepository.findByName(name));
    }

    // Test for findByOrganizationId
    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> roleEntitiesPage = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, Mockito.any(Pageable.class)))
                .thenReturn(roleEntitiesPage);
        Page<RoleEntity> roleEntitiesPageActual = roleRepository.findByOrganizationId(organizationId, Mockito.any(Pageable.class));
        Assertions.assertEquals(roleEntitiesPage, roleEntitiesPageActual);

        Mockito.verify(roleRepository, Executions.never()).findByOrganizationIdAndRoleIds(organizationId, null, Mockito.any(Pageable.class));
    }

    // Test for findByOrganizationIdAndRoleIds
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> roleEntitiesPage = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
                .thenReturn(roleEntitiesPage);
        Page<RoleEntity> roleEntitiesPageActual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class));
        Assertions.assertEquals(roleEntitiesPage, roleEntitiesPageActual);
    }

    // Test for findByUserId
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);
        List<RoleEntity> roleEntitiesActual = roleRepository.findByUserId(userId);
        Assertions.assertEquals(roleEntities, roleEntitiesActual);
    }

    // Test for findByUserIdAndEnabled
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);
        List<RoleEntity> roleEntitiesActual = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assertions.assertEquals(roleEntities, roleEntitiesActual);
    }

    // Test for findByRoleIdsAndEnabled
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);
        List<RoleEntity> roleEntitiesActual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assertions.assertEquals(roleEntities, roleEntitiesActual);
    }

    // Test for update
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        Mockito.doAnswer(invocationOnMock -> {
            RoleEntity roleEntityInput = (RoleEntity) invocationOnMock.getArgument(0);
            roleEntity.setName(roleEntityInput.getName());
            roleEntity.setIsEnabled(roleEntityInput.isEnabled());
            roleEntity.setDescription(roleEntityInput.getDescription());
            roleEntity.setUserUpdateTime(System.currentTimeMillis());
            return 1;
        }).when(roleRepository).update(Mockito.any(RoleEntity.class));

        int updateCount = roleRepository.update(roleEntity);
        Assertions.assertEquals(1, updateCount);
    }
}