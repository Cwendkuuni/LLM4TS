package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryMock;

    private RoleEntity roleEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
        roleEntity.setDescription("testRole description");
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(roleEntity.getId());
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> actualRoleEntities = roleRepositoryMock.findByIdIn(ids);
        assertEquals(roleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByName(name)).thenReturn(roleEntities);

        List<RoleEntity> actualRoleEntities = roleRepositoryMock.findByName(name);
        assertEquals(roleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> rolePages = new PageImpl<>(new ArrayList<>());
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(rolePages);

        Page<RoleEntity> actualRolePages = roleRepositoryMock.findByOrganizationId(organizationId, pageable);
        assertEquals(rolePages, actualRolePages);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRoleEntity);

        Optional<RoleEntity> actualRoleEntity = roleRepositoryMock.findByNameAndOrganizationId(name, organizationId);
        assertEquals(optionalRoleEntity, actualRoleEntity);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testRole");
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        List<RoleEntity> actualRoleEntities = roleRepositoryMock.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(roleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(roleEntity.getId());
        Page<RoleEntity> rolePages = new PageImpl<>(new ArrayList<>());
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10)))
                .thenReturn(rolePages);

        Page<RoleEntity> actualRolePages = roleRepositoryMock.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
        assertEquals(rolePages, actualRolePages);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> actualRoleEntities = roleRepositoryMock.findByUserId(userId);
        assertEquals(roleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> actualRoleEntities = roleRepositoryMock.findByUserIdAndEnabled(userId, enabled);
        assertEquals(roleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(roleEntity.getId());
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> actualRoleEntities = roleRepositoryMock.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(roleEntities, actualRoleEntities);
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setId(roleEntity.getId());
        updatedRoleEntity.setName("updatedTestRole");
        updatedRoleEntity.setEnabled(true);
        updatedRoleEntity.setDescription("updatedTestRole description");

        int result = 1;
        when(roleRepository.update(updatedRoleEntity)).thenReturn(result);

        int actualResult = roleRepositoryMock.update(updatedRoleEntity);
        assertEquals(result, actualResult);
    }
}