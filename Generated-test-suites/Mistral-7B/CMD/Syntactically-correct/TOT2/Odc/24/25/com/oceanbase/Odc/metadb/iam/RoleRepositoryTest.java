package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        roleRepository = Mockito.mock(RoleRepository.class);

        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", false));
        Mockito.when(roleRepository.findByIdIn(Collections.singletonList(1L)))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(1L));

        Assertions.assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByName() {
        roleRepository = Mockito.mock(RoleRepository.class);

        RoleEntity roleEntity = new RoleEntity(1L, "admin", true);
        Mockito.when(roleRepository.findByName(roleEntity.getName()))
                .thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByName(roleEntity.getName()).get();

        Assertions.assertEquals(roleEntity, result);
    }

    @Test
    public void testFindByOrganizationId() {
        roleRepository = Mockito.mock(RoleRepository.class);

        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", false));
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, roleEntities.size());

        Mockito.when(roleRepository.findByOrganizationId(1L, pageable))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);

        Assertions.assertEquals(roleEntities, result.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        roleRepository = Mockito.mock(RoleRepository.class);

        RoleEntity roleEntity = new RoleEntity(1L, "admin", true);
        Mockito.when(roleRepository.findByNameAndOrganizationId(roleEntity.getName(), roleEntity.getOrganizationId()))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity result = roleRepository.findByNameAndOrganizationId(roleEntity.getName(), roleEntity.getOrganizationId()).get();

        Assertions.assertEquals(roleEntity, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        roleRepository = Mockito.mock(RoleRepository.class);

        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", false),
                new RoleEntity(3L, "admin", true));
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, List.of("admin")))
                .thenReturn(List.of(roleEntities.get(0), roleEntities.get(2)));

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, List.of("admin"));

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        roleRepository = Mockito.mock(RoleRepository.class);

        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", false),
                new RoleEntity(3L, "admin", true), new RoleEntity(4L, "admin", true));
        Pageable pageable = Mockito.mock(Pageable.class);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(1L, List.of(1L, 3L), pageable))
                .thenReturn(new PageImpl<>(List.of(roleEntities.get(0), roleEntities.get(2)), pageable, 2));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, List.of(1L, 3L), pageable);

        Assertions.assertEquals(2, result.getContent().size());
    }

    @Test
    public void testFindByUserId() {
        roleRepository = Mockito.mock(RoleRepository.class);

        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", false),
                new RoleEntity(3L, "admin", true));
        Mockito.when(roleRepository.findByUserId(1L))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(1L);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        roleRepository = Mockito.mock(RoleRepository.class);

        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", false),
                new RoleEntity(3L, "admin", true), new RoleEntity(4L, "admin", false));
        Mockito.when(roleRepository.findByUserIdAndEnabled(1L, true))
                .thenReturn(List.of(roleEntities.get(0), roleEntities.get(3)));

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        roleRepository = Mockito.mock(RoleRepository.class);

        List<RoleEntity> roleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", false),
                new RoleEntity(3L, "admin", true), new RoleEntity(4L, "admin", false), new RoleEntity(5L, "admin", true));
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(List.of(1L, 3L), true))
                .thenReturn(List.of(roleEntities.get(0), roleEntities.get(3), roleEntities.get(5)));

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(List.of(1L, 3L), true);

        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void testUpdate() {
        roleRepository = Mockito.mock(RoleRepository.class);

        RoleEntity roleEntity = new RoleEntity(1L, "admin", true);

        Executable updatingRole = () -> roleRepository.update(roleEntity);

        Mockito.doNothing().when(roleRepository).update(roleEntity);

        updatingRole.execute();

        Mockito.verify(roleRepository, Mockito.times(1)).update(roleEntity);
    }
}