package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(id1, "admin"), new RoleEntity(id2, "user"));
        Mockito.when(roleRepository.findByIdIn(Collections.singletonList(id1))).thenReturn(Optional.of(new RoleEntity(id1, "admin")));
        Mockito.when(roleRepository.findByIdIn(Collections.singletonList(id2))).thenReturn(Optional.of(new RoleEntity(id2, "user")));
        Mockito.when(roleRepository.findByIdIn(Collections.singletonList(3L))).thenReturn(Optional.empty());

        List<RoleEntity> actualRoleEntities = roleRepository.findByIdIn(List.of(id1, id2, 3L));

        Assertions.assertIterableEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, name), new RoleEntity(2L, name));
        Mockito.when(roleRepository.findByName(name)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByName(name);

        Assertions.assertIterableEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "admin"), new RoleEntity(2L, "user"));
        Page<RoleEntity> page = Mockito.mock(Page.class);
        Mockito.when(page.getContent()).thenReturn(expectedRoleEntities);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, Mockito.mock(Pageable.class))).thenReturn(page);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, Mockito.mock(Pageable.class));

        Assertions.assertEquals(expectedRoleEntities, actualPage.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "admin";
        Long organizationId = 1L;
        RoleEntity expectedRoleEntity = new RoleEntity(1L, name, organizationId);
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRoleEntity));

        Optional<RoleEntity> actualRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);

        Assertions.assertEquals(expectedRoleEntity, actualRoleEntity.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = List.of("admin", "user");
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "admin", organizationId), new RoleEntity(2L, "user", organizationId));
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        Assertions.assertIterableEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = List.of(1L, 2L);
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "admin"), new RoleEntity(2L, "user"));
        Page<RoleEntity> page = Mockito.mock(Page.class);
        Mockito.when(page.getContent()).thenReturn(expectedRoleEntities);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.mock(Pageable.class))).thenReturn(page);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.mock(Pageable.class));

        Assertions.assertEquals(expectedRoleEntities, actualPage.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "admin"), new RoleEntity(2L, "user"));
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserId(userId);

        Assertions.assertIterableEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "admin", enabled), new RoleEntity(2L, "user", false));
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Assertions.assertIterableEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(1L, "admin", true), new RoleEntity(2L, "user", true));
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoleEntities);

        List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Assertions.assertIterableEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(1L, "admin", true);

        int actualRowsAffected = roleRepository.update(roleEntity);

        Assertions.assertEquals(1, actualRowsAffected);
    }
}