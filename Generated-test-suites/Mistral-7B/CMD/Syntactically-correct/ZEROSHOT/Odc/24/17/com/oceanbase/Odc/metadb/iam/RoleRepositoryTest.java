import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.repository.RoleRepositoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    private RoleRepositoryImpl roleRepositoryMock = Mockito.spy(new RoleRepositoryImpl());

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> roleEntities = List.of(new RoleEntity(id1), new RoleEntity(id2));

        when(roleRepositoryMock.findByIdIn(Mockito.any()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(List.of(id1, id2));

        Assertions.assertSame(roleEntities, result);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName(name);

        when(roleRepositoryMock.findByName(Mockito.eq(name)))
                .thenReturn(List.of(roleEntity));

        List<RoleEntity> result = roleRepository.findByName(name);

        Assertions.assertSame(roleEntity, result.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Pageable.ofSize(10, Sort.by(Sort.Direction.ASC, "id"));
        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L), new RoleEntity(2L));

        when(roleRepositoryMock.findAll((Specification<RoleEntity>) any()))
                .thenReturn(roleEntities);

        when(roleRepositoryMock.findByOrganizationId(organizationId, pageable))
                .thenReturn(Page.of(roleEntities, pageable));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        Assertions.assertSame(roleEntities, result.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);

        when(roleRepositoryMock.findByNameAndOrganizationId(Mockito.eq(name), Mockito.eq(organizationId)))
                .thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertSame(roleEntity, result.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("testRole1", "testRole2");
        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, organizationId, "testRole1"),
                new RoleEntity(2L, organizationId, "testRole2"));

        when(roleRepositoryMock.findAll((Specification<RoleEntity>) any()))
                .thenReturn(roleEntities);

        when(roleRepositoryMock.findByOrganizationIdAndNameIn(organizationId, names))
                .thenReturn(List.of(roleEntities.get(0), roleEntities.get(1)));

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        Assertions.assertSame(roleEntities.subList(0, 2), result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, organizationId), new RoleEntity(2L, organizationId));

        when(roleRepositoryMock.findByOrganizationIdAndRoleIds(organizationId, roleIds, any()))
                .thenReturn(Page.of(roleEntities, any()));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());

        Assertions.assertSame(roleEntities, result.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, null), new RoleEntity(2L, userId));

        when(roleRepositoryMock.findByUserId(userId))
                .thenReturn(roleEntities.subList(1, roleEntities.size()));

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        Assertions.assertSame(roleEntities.subList(1, roleEntities.size()), result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, userId, enabled), new RoleEntity(2L, userId, false));

        when(roleRepositoryMock.findByUserIdAndEnabled(userId, enabled))
                .thenReturn(roleEntities.stream()
                        .filter(role -> role.isEnabled().equals(enabled))
                        .collect(Collectors.toList()));

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Assertions.assertSame(roleEntities.stream()
                .filter(role -> role.isEnabled().equals(enabled))
                .collect(Collectors.toList()), result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, null, enabled), new RoleEntity(2L, null, enabled),
                new RoleEntity(3L, null, false));

        when(roleRepositoryMock.findAll((Specification<RoleEntity>) any()))
                .thenReturn(roleEntities);

        when(roleRepositoryMock.findByRoleIdsAndEnabled(roleIds, enabled))
                .thenReturn(List.of(roleEntities.get(0), roleEntities.get(1)));

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Assertions.assertSame(roleEntities.stream()
                .filter(role -> role.isEnabled().equals(enabled))
                .collect(Collectors.toList()), result);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);

        when(roleRepositoryMock.save(Mockito.eq(roleEntity)))
                .thenReturn(roleEntity);

        int result = roleRepository.update(roleEntity);

        Assertions.assertEquals(1, result);
    }
}