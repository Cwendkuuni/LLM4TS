package com.oceanbase.odc.metadb.iam.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.IamRoleRepository;

class RoleRepositoryTest {

    @InjectMocks
    private IamRoleRepository roleRepository;

    @Test
    void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByIdIn(Collections.singletonList(id1))).thenReturn(Optional.of(new RoleEntity()));
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(id1, id2))).thenReturn(Optional.of(expectedResult));

        Optional<RoleEntity> result1 = roleRepository.findByIdIn(Collections.singletonList(id1));
        assertEquals(Optional.of(new RoleEntity()), result1);

        List<RoleEntity> result2 = roleRepository.findByIdIn(Arrays.asList(id1, id2));
        assertEquals(expectedResult, result2);
    }

    @Test
    void testFindByName() {
        String name = "testName";
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.findByName(name)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(Optional.of(roleEntity), result);
    }

    @Test
    void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Pageable.ofSize(10).sort(Sort.by(Sort.Direction.ASC, "id"));
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(Mockito.mock(Page.class));
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable).getContent()).thenReturn(expectedResult);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedResult, result.getContent());
    }

    @Test
    void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertEquals(Optional.of(roleEntity), result);
    }

    @Test
    void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("name1", "name2");
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedResult, result);
    }

    @Test
    void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged())).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertEquals(expectedResult, result);
    }

    @Test
    void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(expectedResult, result);
    }

    @Test
    void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedResult, result);
    }

    @Test
    void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedResult, result);
    }

    @Test
    void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleRepository.save(roleEntity);
        Long id = roleEntity.getId();

        Mockito.doAnswer(invocation -> {
            RoleEntity entity = (RoleEntity) invocation.getArguments()[0];
            entity.setId(id);
            return 1;
        }).when(roleRepository).update(Mockito.any());

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}