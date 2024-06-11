package com.oceanbase.odc.metadb.iam.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

class RoleRepositoryTest {

    @Test
    void testFindByIdIn() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity role1 = new RoleEntity();
        RoleEntity role2 = new RoleEntity();
        List<RoleEntity> roles = Arrays.asList(role1, role2);
        given(roleRepository.findByIdIn(roles.stream().map(it -> it.getId()).collect(Collectors.toList()))).willReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(roles.stream().map(it -> it.getId()).collect(Collectors.toList()));
        assertThat(result).containsExactlyInAnyOrder(role1, role2);
    }

    @Test
    void testFindByName() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();
        String name = "testName";
        given(roleRepository.findByName(name)).willReturn(Collections.singletonList(roleExpected));

        RoleEntity result = roleRepository.findByName(name).get();
        assertThat(result).isEqualTo(roleExpected);
    }

    @Test
    void testFindByOrganizationId() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> pageExpected = new PageImpl<>(Arrays.asList(roleExpected), pageable, 1);

        given(roleRepository.findByOrganizationId(organizationId, pageable)).willReturn(pageExpected);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertThat(result).hasSize(1);
        ArgumentCaptor<Slice<RoleEntity>> sliceCaptor = ArgumentCaptor.forClass(Slice.class);
        verify(roleRepository).findAll(eq(organizationId), any(Pageable.class), eq(false), sliceCaptor.capture());
        Slice<RoleEntity> slice = sliceCaptor.getValue();
        Assertions.assertEquals(1, slice.getNumberOfElements());
        Assertions.assertEquals(pageExpected.getContent().get(0).getId(), slice.getContent().get(0).getId());
    }

    @Test
    void testFindByNameAndOrganizationId() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();
        String name = "testName";
        Long organizationId = 1L;

        given(roleRepository.findByNameAndOrganizationId(name, organizationId)).willReturn(Optional.of(roleExpected));

        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertThat(result).isEqualTo(roleExpected);
    }

    @Test
    void testFindByOrganizationIdAndNameIn() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected1 = new RoleEntity();
        RoleEntity roleExpected2 = new RoleEntity();
        List<String> names = Arrays.asList("name1", "name2");
        Long organizationId = 1L;
        List<RoleEntity> roles = Arrays.asList(roleExpected1, roleExpected2);

        given(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).willReturn(roles);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertThat(result).containsExactlyInAnyOrder(roleExpected1, roleExpected2);
    }

    @Test
    void testFindByOrganizationIdAndRoleIds() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();
        Long organizationId = 1L;
        Long roleId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        given(roleRepository.findByOrganizationIdAndRoleIds(organizationId, Arrays.asList(roleId), pageable))
                .willReturn(new PageImpl<>(Arrays.asList(roleExpected), pageable, 1));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, Arrays.asList(roleId), pageable);
        assertThat(result).hasSize(1);
        ArgumentCaptor<Slice<RoleEntity>> sliceCaptor = ArgumentCaptor.forClass(Slice.class);
        verify(roleRepository).findAll(eq(organizationId), any(Pageable.class), eq(false), sliceCaptor.capture());
        Slice<RoleEntity> slice = sliceCaptor.getValue();
        Assertions.assertEquals(1, slice.getNumberOfElements());
        Assertions.assertEquals(roleExpected.getId(), slice.getContent().get(0).getId());
    }

    @Test
    void testFindByUserId() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();
        Long userId = 1L;

        given(roleRepository.findByUserId(userId)).willReturn(Arrays.asList(roleExpected));

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByUserIdAndEnabled() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();
        Long userId = 1L;
        Boolean enabled = true;

        given(roleRepository.findByUserIdAndEnabled(userId, enabled)).willReturn(Arrays.asList(roleExpected));

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();
        Collection<Long> roleIds = Arrays.asList(1L);
        Boolean enabled = true;

        given(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).willReturn(Arrays.asList(roleExpected));

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertThat(result).hasSize(1);
    }

    @Test
    void testUpdate() {
        RoleRepository roleRepository = mock(RoleRepository.class);
        RoleEntity roleExpected = new RoleEntity();

        given(roleRepository.update(any(RoleEntity.class))).willAnswer(invocation -> 1);

        int result = roleRepository.update(roleExpected);
        assertThat(result).isGreaterThan(0);
    }
}