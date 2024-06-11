import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Test
    void testFindByIdIn() {
        // Given
        roleRepository = mock(RoleRepository.class);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(expectedResult);

        // When
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        // Then
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void testFindByName() {
        // Given
        roleRepository = mock(RoleRepository.class);
        String name = "testRole";
        RoleEntity expectedResult = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedResult));

        // When
        List<RoleEntity> result = roleRepository.findByName(name);

        // Then
        Assertions.assertEquals(expectedResult, result.get(0));
    }

    @Test
    void testFindByOrganizationId() {
        // Given
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        Pageable pageable = Pageable.ofSize(10).sort(Sort.by("id").descending());
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> mockPage = mock(Page.class);
        when(mockPage.getContent()).thenReturn(expectedResult);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(mockPage);

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        // Then
        Assertions.assertEquals(expectedResult, result.getContent());
    }

    @Test
    void testFindByNameAndOrganizationId() {
        // Given
        roleRepository = mock(RoleRepository.class);
        String name = "testRole";
        Long organizationId = 1L;
        Optional<RoleEntity> expectedResult = Optional.of(new RoleEntity());
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedResult);

        // When
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedResult.get(), result.get());
    }

    @Test
    void testFindByOrganizationIdAndNameIn() {
        // Given
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);

        // When
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        // Then
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void testFindByOrganizationIdAndRoleIds() {
        // Given
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = Pageable.ofSize(10).sort(Sort.by("id").descending());
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> mockPage = mock(Page.class);
        when(mockPage.getContent()).thenReturn(expectedResult);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(mockPage);

        // When
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        // Then
        Assertions.assertEquals(expectedResult, result.getContent());
    }

    @Test
    void testFindByUserId() {
        // Given
        roleRepository = mock(RoleRepository.class);
        Long userId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

        // When
        List<RoleEntity> result = roleRepository.findByUserId(userId);

        // Then
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void testFindByUserIdAndEnabled() {
        // Given
        roleRepository = mock(RoleRepository.class);
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

        // When
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        // Then
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        // Given
        roleRepository = mock(RoleRepository.class);
        Boolean enabled = true;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

        // When
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        // Then
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void testUpdate() {
        // Given
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.update(roleEntity)).thenAnswer((Executable exec) -> 1);

        // When
        int result = roleRepository.update(roleEntity);

        // Then
        Assertions.assertEquals(1, result);
    }

    @Test
    void testFindByUserIdAndEnabled_EmptyResult() {
        // Given
        roleRepository = mock(RoleRepository.class);
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> emptyList = Collections.emptyList();
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(emptyList);

        // When
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        // Then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testUpdate_NullRoleEntity() {
        // Given
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = null;

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> roleRepository.update(roleEntity));
    }
}