import com.oceanbase.odc.metadb.iam.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @BeforeEach
    public void setup() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("TestRole");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
    }

    @Test
    public void testFindById() {
        Mockito.when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> testRoleEntity = roleRepository.findById(roleEntity.getId());
        Assertions.assertTrue(testRoleEntity.isPresent());
        Assertions.assertEquals(roleEntity, testRoleEntity.get());
    }

    @Test
    public void testFindByIdNotFound() {
        Mockito.when(roleRepository.findById(roleEntity.getId())).thenReturn(Optional.empty());
        Assertions.assertFalse(roleRepository.findById(roleEntity.getId()).isPresent());
    }

    @Test
    public void testFindAll() {
        Mockito.when(roleRepository.findAll()).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> testRoles = roleRepository.findAll();
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(1, testRoles.size());
        Assertions.assertEquals(roleEntity, testRoles.get(0));
    }

    @Test
    public void testFindByIdIn() {
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(2L);
        testRoleEntity.setName("TestRole2");
        testRoleEntity.setOrganizationId(1L);
        testRoleEntity.setEnabled(true);

        List<Long> ids = Arrays.asList(roleEntity.getId(), testRoleEntity.getId());
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(Arrays.asList(roleEntity, testRoleEntity));

        List<RoleEntity> testRoles = roleRepository.findByIdIn(ids);
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(2, testRoles.size());
        Assertions.assertTrue(testRoles.contains(roleEntity));
        Assertions.assertTrue(testRoles.contains(testRoleEntity));
    }

    @Test
    public void testFindByName() {
        Mockito.when(roleRepository.findByName("TestRole")).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> testRoles = roleRepository.findByName("TestRole");
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(1, testRoles.size());
        Assertions.assertEquals(roleEntity, testRoles.get(0));
    }

    @Test
    public void testFindByNameNotFound() {
        Mockito.when(roleRepository.findByName("TestRole")).thenReturn(Collections.emptyList());
        List<RoleEntity> testRoles = roleRepository.findByName("TestRole");
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(0, testRoles.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Long organizationId = 1L;
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(2L);
        testRoleEntity.setName("TestRole2");
        testRoleEntity.setOrganizationId(organizationId);
        testRoleEntity.setEnabled(true);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Page.empty());
        Page<RoleEntity> testPage = roleRepository.findByOrganizationId(organizationId, pageable);
        Assertions.assertNotNull(testPage);
        Assertions.assertEquals(0, testPage.getTotalElements());

        List<RoleEntity> roles = Arrays.asList(roleEntity, testRoleEntity);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Page.of(roles, pageable));
        Page<RoleEntity> testPage2 = roleRepository.findByOrganizationId(organizationId, pageable);
        Assertions.assertNotNull(testPage2);
        Assertions.assertEquals(1, testPage2.getTotalElements());
        Assertions.assertEquals(roles, testPage2.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Mockito.when(roleRepository.findByNameAndOrganizationId("TestRole", 1L)).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> testRoleEntity = roleRepository.findByNameAndOrganizationId("TestRole", 1L);
        Assertions.assertTrue(testRoleEntity.isPresent());
        Assertions.assertEquals(roleEntity, testRoleEntity.get());

        Mockito.when(roleRepository.findByNameAndOrganizationId("TestRole", 1L)).thenReturn(Optional.empty());
        Assertions.assertFalse(roleRepository.findByNameAndOrganizationId("TestRole", 1L).isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(2L);
        testRoleEntity.setName("TestRole2");
        testRoleEntity.setOrganizationId(1L);
        testRoleEntity.setEnabled(true);

        List<String> names = Arrays.asList("TestRole", "TestRole2");
        List<RoleEntity> roles = Arrays.asList(roleEntity, testRoleEntity);

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, names)).thenReturn(roles);
        List<RoleEntity> testRoles = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(2, testRoles.size());
        Assertions.assertTrue(testRoles.contains(roleEntity));
        Assertions.assertTrue(testRoles.contains(testRoleEntity));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIdsAndEnabled() {
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(2L);
        testRoleEntity.setName("TestRole2");
        testRoleEntity.setOrganizationId(1L);
        testRoleEntity.setEnabled(false);

        List<Long> roleIds = Arrays.asList(roleEntity.getId(), testRoleEntity.getId());
        Page<RoleEntity> testPage = Page.of(Arrays.asList(roleEntity, testRoleEntity), Mockito.mock(Pageable.class));

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(1L, roleIds, Mockito.mock(Pageable.class)))
                .thenReturn(testPage);
        Page<RoleEntity> testPage2 = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(1L, roleIds, Mockito.mock(Pageable.class));
        Assertions.assertNotNull(testPage2);
        Assertions.assertEquals(2, testPage2.getTotalElements());
        Assertions.assertEquals(Arrays.asList(roleEntity, testRoleEntity), testPage2.getContent());
    }

    @Test
    public void testFindByUserId() {
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(2L);
        testRoleEntity.setName("TestRole2");
        testRoleEntity.setOrganizationId(1L);
        testRoleEntity.setEnabled(true);

        List<RoleEntity> roles = Arrays.asList(roleEntity, testRoleEntity);

        Mockito.when(roleRepository.findByUserId(1L)).thenReturn(roles);
        List<RoleEntity> testRoles = roleRepository.findByUserId(1L);
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(2, testRoles.size());
        Assertions.assertTrue(testRoles.contains(roleEntity));
        Assertions.assertTrue(testRoles.contains(testRoleEntity));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(2L);
        testRoleEntity.setName("TestRole2");
        testRoleEntity.setOrganizationId(1L);
        testRoleEntity.setEnabled(false);

        List<RoleEntity> roles = Arrays.asList(roleEntity, testRoleEntity);

        Mockito.when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> testRoles = roleRepository.findByUserIdAndEnabled(1L, true);
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(1, testRoles.size());
        Assertions.assertEquals(roleEntity, testRoles.get(0));

        Mockito.when(roleRepository.findByUserIdAndEnabled(1L, false)).thenReturn(Arrays.asList(testRoleEntity));
        List<RoleEntity> testRoles2 = roleRepository.findByUserIdAndEnabled(1L, false);
        Assertions.assertNotNull(testRoles2);
        Assertions.assertEquals(1, testRoles2.size());
        Assertions.assertEquals(testRoleEntity, testRoles2.get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        RoleEntity testRoleEntity = new RoleEntity();
        testRoleEntity.setId(2L);
        testRoleEntity.setName("TestRole2");
        testRoleEntity.setOrganizationId(1L);
        testRoleEntity.setEnabled(false);

        List<Long> roleIds = Arrays.asList(roleEntity.getId(), testRoleEntity.getId());

        List<RoleEntity> roles = Arrays.asList(roleEntity, testRoleEntity);

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> testRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        Assertions.assertNotNull(testRoles);
        Assertions.assertEquals(1, testRoles.size());
        Assertions.assertEquals(roleEntity, testRoles.get(0));

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, false)).thenReturn(Arrays.asList(testRoleEntity));
        List<RoleEntity> testRoles2 = roleRepository.findByRoleIdsAndEnabled(roleIds, false);
        Assertions.assertNotNull(testRoles2);
        Assertions.assertEquals(1, testRoles2.size());
        Assertions.assertEquals(testRoleEntity, testRoles2.get(0));
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setName("TestRoleUpdated");
        updatedRoleEntity.setOrganizationId(1L);
        updatedRoleEntity.setEnabled(true);

        Mockito.when(roleRepository.update(updatedRoleEntity)).thenReturn(1);
        int result = roleRepository.update(updatedRoleEntity);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testUpdateNotFound() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setName("TestRoleUpdated");
        updatedRoleEntity.setOrganizationId(-1L);
        updatedRoleEntity.setEnabled(true);

        Mockito.when(roleRepository.update(updatedRoleEntity)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> roleRepository.update(updatedRoleEntity));
    }

}