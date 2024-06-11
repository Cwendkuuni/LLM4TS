package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
        roleRepository = mock(RoleRepository.class);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(ArgumentMatchers.any(Collection.class)))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(1L));
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testFindByName() {
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByName("test").get();
        Assertions.assertEquals(roleEntity, result);
    }

    @Test
    void testFindByOrganizationId() {
        roleRepository = mock(RoleRepository.class);
        Pageable pageable = Pageable.ofSize(10);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        when(roleRepository.findByOrganizationId(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        Assertions.assertEquals(page, result);
    }

    @Test
    void testFindByNameAndOrganizationId() {
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity result = roleRepository.findByNameAndOrganizationId("test", 1L).get();
        Assertions.assertEquals(roleEntity, result);
    }

    @Test
    void testFindByOrganizationIdAndNameIn() {
        roleRepository = mock(RoleRepository.class);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(ArgumentMatchers.anyLong(), ArgumentMatchers.anyList()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, List.of("test"));
        Assertions.assertEquals(roleEntities, result);
    }

    @Test
    void testFindByOrganizationIdAndRoleIds() {
        roleRepository = mock(RoleRepository.class);
        Pageable pageable = Pageable.ofSize(10);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        when(roleRepository.findByOrganizationIdAndRoleIds(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Collection.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, List.of(1L), pageable);
        Assertions.assertEquals(page, result);
    }

    @Test
    void testFindByUserId() {
        roleRepository = mock(RoleRepository.class);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(1L);
        Assertions.assertEquals(roleEntities, result);
    }

    @Test
    void testFindByUserIdAndEnabled() {
        roleRepository = mock(RoleRepository.class);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        Assertions.assertEquals(roleEntities, result);
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        roleRepository = mock(RoleRepository.class);
        List<RoleEntity> roleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(ArgumentMatchers.any(Collection.class), ArgumentMatchers.anyBoolean()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(List.of(1L), true);
        Assertions.assertEquals(roleEntities, result);
    }

    @Test
    void testUpdate() {
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = new RoleEntity();

        Executable executable = () -> roleRepository.update(roleEntity);
        Assertions.assertDoesNotThrow(executable);
    }

    @Test
    void testFindByUserIdAndEnabledWithEmptyResult() {
        roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByUserIdAndEnabled(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                .thenReturn(Collections.emptyList());

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testFindByUserIdAndEnabledWithNullResult() {
        roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByUserIdAndEnabled(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                .thenReturn(Optional.empty());

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        Assertions.assertNull(result);
    }

    @Test
    void testFindByRoleIdsAndEnabledWithEmptyResult() {
        roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByRoleIdsAndEnabled(ArgumentMatchers.any(Collection.class), ArgumentMatchers.anyBoolean()))
                .thenReturn(Collections.emptyList());

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(List.of(1L), true);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testFindByRoleIdsAndEnabledWithNullResult() {
        roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByRoleIdsAndEnabled(ArgumentMatchers.any(Collection.class), ArgumentMatchers.anyBoolean()))
                .thenReturn(Optional.empty());

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(List.of(1L), true);
        Assertions.assertNull(result);
    }

    @Test
    void testFindByOrganizationIdAndRoleIdsWithEmptyResult() {
        roleRepository = mock(RoleRepository.class);
        Pageable pageable = Pageable.ofSize(10);
        Slice<RoleEntity> emptySlice = new SliceImpl<>();

        when(roleRepository.findByOrganizationIdAndRoleIds(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Collection.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(emptySlice);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, List.of(1L), pageable);
        Assertions.assertEquals(emptySlice, result);
    }

    @Test
    void testFindByOrganizationIdAndRoleIdsWithNullResult() {
        roleRepository = mock(RoleRepository.class);
        Pageable pageable = Pageable.ofSize(10);

        when(roleRepository.findByOrganizationIdAndRoleIds(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Collection.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(null);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, List.of(1L), pageable);
        Assertions.assertNull(result);
    }
}