import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;
    private Page<RoleEntity> page;

    static Stream<Arguments> providePageableArgs() {
        return Stream.of(
                Arguments.arguments(Pageable.unpaged()),
                Arguments.arguments(Pageable.ofSize(1, Sort.by(Sort.Direction.ASC, "id"))),
                Arguments.arguments(Pageable.ofSize(2, Sort.by(Sort.Direction.DESC, "id")))
        );
    }

    @Test
    void testFindById() {
        roleEntity1 = mock(RoleEntity.class);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity1));
        RoleEntity actual = roleRepository.findById(1L).get();
        assertNotNull(actual);
        assertEquals(roleEntity1, actual);
    }

    @Test
    void testFindById_NotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(roleRepository.findById(1L).get());
    }

    @ParameterizedTest(name = "[{index}] findByIdIn - ids: {0}")
    @MethodSource("provideListArgs")
    void testFindByIdIn(List<Long> ids) {
        List<RoleEntity> entities = List.of(roleEntity1, roleEntity2);
        when(roleRepository.findByIdIn(ids)).thenReturn(entities);
        List<RoleEntity> actual = roleRepository.findByIdIn(ids);
        assertEquals(entities, actual);
    }

    static Stream<Arguments> provideListArgs() {
        return Stream.of(
                Arguments.arguments(Collections.singletonList(1L)),
                Arguments.arguments(List.of(1L, 2L))
        );
    }

    @Test
    void testFindByName() {
        String name = "testName";
        roleEntity1 = mock(RoleEntity.class);
        when(roleRepository.findByName(name)).thenReturn(List.of(roleEntity1));
        List<RoleEntity> actual = roleRepository.findByName(name);
        assertEquals(1, actual.size());
        assertEquals(roleEntity1, actual.get(0));
    }

    @Test
    void testFindByName_Empty() {
        when(roleRepository.findByName(anyString())).thenReturn(Collections.emptyList());
        List<RoleEntity> actual = roleRepository.findByName(anyString());
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testFindByOrganizationId() {
        Long organizationId = 1L;
        page = mock(Page.class);
        when(roleRepository.findByOrganizationId(organizationId, Pageable.unpaged())).thenReturn(page);
        Page<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
        assertEquals(page, actual);
    }

    @Test
    void testFindByOrganizationId_Empty() {
        Long organizationId = 1L;
        when(roleRepository.findByOrganizationId(organizationId, Pageable.unpaged())).thenReturn(Page.empty());
        Page<RoleEntity> actual = roleRepository.findByOrganizationId(organizationId, Pageable.unpaged());
        assertNotNull(actual);
        assertEquals(0, actual.getContent().size());
    }

    @ParameterizedTest(name = "[{index}] findByOrganizationIdAndRoleIds - organizationId: {0}, roleIds: {1}")
    @MethodSource("providePageableArgs")
    void testFindByOrganizationIdAndRoleIds(Long organizationId, Pageable pageable) {
        List<RoleEntity> entities = List.of(roleEntity1, roleEntity2);
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, List.of(1L, 2L), pageable)).thenReturn(Page.of(entities));
        Page<RoleEntity> actual = roleRepository.findByOrganizationIdAndRoleIds(organizationId, List.of(1L, 2L), pageable);
        assertEquals(entities, actual.getContent());
        assertEquals(2, actual.getTotalElements());
        assertEquals(2, actual.getSize());
        assertEquals(1, actual.getNumber());
        assertEquals(pageable, actual.getPageable());
    }

    @Test
    void testFindByUserId() {
        Long userId = 1L;
        roleEntity1 = mock(RoleEntity.class);
        when(roleRepository.findByUserId(userId)).thenReturn(List.of(roleEntity1));
        List<RoleEntity> actual = roleRepository.findByUserId(userId);
        assertEquals(1, actual.size());
        assertEquals(roleEntity1, actual.get(0));
    }

    @Test
    void testFindByUserId_Empty() {
        Long userId = 1L;
        when(roleRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        List<RoleEntity> actual = roleRepository.findByUserId(userId);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        boolean enabled = true;
        roleEntity1 = mock(RoleEntity.class);
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(List.of(roleEntity1));
        List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(1, actual.size());
        assertEquals(roleEntity1, actual.get(0));
    }

    @Test
    void testFindByUserIdAndEnabled_Empty() {
        Long userId = 1L;
        boolean enabled = true;
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Collections.emptyList());
        List<RoleEntity> actual = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = List.of(1L, 2L);
        boolean enabled = true;
        List<RoleEntity> entities = List.of(roleEntity1, roleEntity2);
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(entities);
        List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(entities, actual);
    }

    @Test
    void testFindByRoleIdsAndEnabled_Empty() {
        List<Long> roleIds = List.of(1L, 2L);
        boolean enabled = true;
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(Collections.emptyList());
        List<RoleEntity> actual = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void testUpdate() {
        RoleEntity roleEntity = mock(RoleEntity.class);
        int rowsAffected = 1;
        when(roleRepository.update(roleEntity)).thenReturn(rowsAffected);
        int actual = roleRepository.update(roleEntity);
        assertEquals(rowsAffected, actual);
    }
}