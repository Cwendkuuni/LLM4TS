import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupitermock.JUnitJupiter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(JUnitJupiter.SpringIntegrationTest.class)
@SpringJUnitConfig(classes = RoleRepositoryTest.TestConfig.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleEntity roleEntity;

    @BeforeAll
    public static void setup() {
        // Setup mocks or initializations here if needed
    }

    @AfterAll
    public static void tearDown() {
        // Teardown mocks or cleanups here if needed
    }

    // Test case for findByIdIn method
    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);
        List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);
        assertEquals(expectedRoles, actualRoles);
    }

    // Test case for findByName method
    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedRole));
        RoleEntity actualRole = roleRepository.findByName(name).get();
        assertEquals(expectedRole, actualRole);
    }

    // Test case for findByOrganizationId method with pageable
    @Test
    public void testFindByOrganizationIdWithPageable() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);
        when(expectedPage.getContent()).thenReturn(expectedRoles);
        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedRoles, actualPage.getContent());
    }

    // Test case for findByNameAndOrganizationId method
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRole));
        RoleEntity actualRole = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(expectedRole, actualRole);
    }

    // Test case for findByOrganizationIdAndNameIn method
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);
        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedRoles, actualRoles);
    }

    // Test case for findByOrganizationIdAndRoleIds method
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> expectedPage = Mockito.mock(Page.class);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
                .thenReturn(expectedPage);
        when(expectedPage.getContent()).thenReturn(expectedRoles);
        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, null);
        assertEquals(expectedRoles, actualPage.getContent());
    }

    // Test case for findByUserId method
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);
        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);
        assertEquals(expectedRoles, actualRoles);
    }

    // Test case for findByUserIdAndEnabled method
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoles);
        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedRoles, actualRoles);
    }

    // Test case for findByRoleIdsAndEnabled method
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);
        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedRoles, actualRoles);
    }

    // Test case for update method
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        int expectedRowsAffected = 1;
        when(roleRepository.update(roleEntity)).thenReturn(expectedRowsAffected);
        int actualRowsAffected = roleRepository.update(roleEntity);
        assertEquals(expectedRowsAffected, actualRowsAffected);
    }

    // Test case for findByUserIdAndEnabled with disabled role
    @Test
    public void testFindByUserIdAndEnabledWithDisabled() {
        Long userId = 1L;
        Boolean enabled = false;
        List<RoleEntity> expectedEmptyList = Collections.emptyList();
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedEmptyList);
        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertFalse(actualRoles.isEmpty());
    }

    // Test case for update with null roleEntity
    @Test
    public void testUpdateWithNullRoleEntity() {
        assertThrows(NullPointerException.class, () -> roleRepository.update(null));
    }

    // Test case for update with empty name
    @Test
    public void testUpdateWithEmptyName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(null);
        assertThrows(IllegalArgumentException.class, () -> roleRepository.update(roleEntity));
    }

    // Test case for update with null organizationId
    @Test
    public void testUpdateWithNullOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setOrganizationId(null);
        assertThrows(IllegalArgumentException.class, () -> roleRepository.update(roleEntity));
    }

    // Test case for update with null id
    @Test
    public void testUpdateWithNullId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(null);
        assertThrows(IllegalArgumentException.class, () -> roleRepository.update(roleEntity));
    }
}

class TestConfig {
    // Configure any mocks or test-specific setup here if needed
}