package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Test
    void testFindByIdIn() {
        roleRepository = mock(RoleRepository.class);
        Collection<Long> ids = List.of(1L, 2L);
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoleEntities);

        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        Assertions.assertEquals(expectedRoleEntities, roleEntities);
    }

    @Test
    void testFindByName() {
        roleRepository = mock(RoleRepository.class);
        String name = "testName";
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity());
        when(roleRepository.findByName(name)).thenReturn(expectedRoleEntities);

        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        Assertions.assertEquals(expectedRoleEntities, roleEntities);

        verify(roleRepository, times(1)).findByName(name);
    }

    @Test
    void testFindByOrganizationId() {
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity());
        Page<RoleEntity> expectedPage = Page.of(expectedRoleEntities, pageable);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
        Assertions.assertEquals(expectedPage, page);

        verify(roleRepository, times(1)).findByOrganizationId(organizationId, pageable);
    }

    @Test
    void testFindByNameAndOrganizationId() {
        roleRepository = mock(RoleRepository.class);
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity expectedRoleEntity = new RoleEntity();
        Optional<RoleEntity> optionalRoleEntity = Optional.of(expectedRoleEntity);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRoleEntity);

        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        Assertions.assertEquals(expectedRoleEntity, roleEntity.get());

        verify(roleRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    void testFindByOrganizationIdAndNameIn() {
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        List<String> names = List.of("testName1", "testName2");
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoleEntities);

        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        Assertions.assertEquals(expectedRoleEntities, roleEntities);

        verify(roleRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    void testFindByOrganizationIdAndRoleIds() {
        roleRepository = mock(RoleRepository.class);
        Long organizationId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> expectedPage = Page.of(expectedRoleEntities, Pageable.unpaged());

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged()))
                .thenReturn(expectedPage);

        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        Assertions.assertEquals(expectedPage, page);

        verify(roleRepository, times(1)).findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
    }

    @Test
    void testFindByUserId() {
        roleRepository = mock(RoleRepository.class);
        Long userId = 1L;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId)).thenReturn(expectedRoleEntities);

        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        Assertions.assertEquals(expectedRoleEntities, roleEntities);

        verify(roleRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindByUserIdAndEnabled() {
        roleRepository = mock(RoleRepository.class);
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoleEntities);

        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assertions.assertEquals(expectedRoleEntities, roleEntities);

        verify(roleRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        roleRepository = mock(RoleRepository.class);
        Collection<Long> roleIds = List.of(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = List.of(new RoleEntity(), new RoleEntity());

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoleEntities);

        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assertions.assertEquals(expectedRoleEntities, roleEntities);

        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    void testUpdate() {
        roleRepository = mock(RoleRepository.class);
        RoleEntity roleEntity = new RoleEntity();

        Executable executable = () -> roleRepository.update(roleEntity);
        Assertions.assertDoesNotThrow(executable);

        verify(roleRepository, times(1)).update(roleEntity);
    }

    @Test
    void testSpecification() {
        roleRepository = mock(RoleRepository.class);
        RoleRepository roleRepositoryMock = spy(roleRepository);

        Specification<RoleEntity> specification = Mockito.spy(Specification.class);
        when(roleRepositoryMock.findAll(specification)).thenReturn(List.of(new RoleEntity()));

        roleRepositoryMock.findAll(specification);

        verify(roleRepositoryMock, times(1)).findAll(specification);
    }
}