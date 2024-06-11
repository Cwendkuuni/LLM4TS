package com.oceanbase.odc.metadb.iam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleEntity roleEntity;

    @InjectMocks
    private Pageable pageable;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = Arrays.asList(roleEntity);

        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(roles, result);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);

        when(roleRepository.findByName("name")).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName("name");
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationId() {
        List<RoleEntity> roles = Arrays.asList(roleEntity);
        Page<RoleEntity> rolePage = new PageImpl<>(roles);

        when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(rolePage);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(rolePage, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId("name", 1L)).thenReturn(role);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("name", 1L);
        assertEquals(role, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);

        when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("name"))).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("name"));
        assertEquals(roleEntities, result);
    }

    @Test
    public void testUpdateRoleEntity() {
        int expectedUpdatedRows = 1;

        when(roleRepository.update(roleEntity)).thenReturn(expectedUpdatedRows);

        int result = roleRepository.update(roleEntity);
        assertEquals(expectedUpdatedRows, result);
    }
}