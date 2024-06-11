import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    private RoleEntity roleEntity = new RoleEntity();

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByIdIn(ids);

        Assertions.assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findByName(name)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByName(name);

        Assertions.assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findAll(pageable)).thenReturn(expectedPage);
        when(expectedPage.getContent()).thenReturn(expectedRoleEntities);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

        Assertions.assertEquals(expectedRoleEntities, actualPage.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRoleEntity);

        RoleEntity actualRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId).get();

        Assertions.assertEquals(roleEntity, actualRoleEntity);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("test");
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findAll()).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        Assertions.assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);
        when(expectedPage.getContent()).thenReturn(expectedRoleEntities);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.mock(Pageable.class));

        Assertions.assertEquals(expectedRoleEntities, actualPage.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findByUserId(userId)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserId(userId);

        Assertions.assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Assertions.assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        expectedRoleEntities.add(roleEntity1);

        when(roleRepository.findAll()).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Assertions.assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = Mockito.spy(roleEntity);

        int actualResult = roleRepository.update(roleEntity);

        Assertions.assertNotEquals(0, actualResult);
    }
}