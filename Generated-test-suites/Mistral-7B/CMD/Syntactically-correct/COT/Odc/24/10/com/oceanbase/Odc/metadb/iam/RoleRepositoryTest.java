package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Page;
import java.util.Pages;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private IamUserRepository iamUserRepository;

    private RoleEntity roleEntity;
    private Long organizationId = 1L;
    private Long userId = 1L;
    private Long roleId = 1L;
    private List<Long> roleIds = new ArrayList<>();

    @Before
    public void setup() {
        roleEntity = new RoleEntity();
        roleEntity.setId(roleId);
        roleEntity.setName("testRole");
        roleEntity.setOrganizationId(organizationId);
        roleEntity.setIsEnabled(true);
        roleEntity.setDescription("test description");
    }

    @Test
    public void testFindByIdIn() {
        roleIds.add(roleId);
        List<RoleEntity> expectedResult = List.of(roleEntity);
        when(roleRepository.findByIdIn(roleIds)).thenReturn(expectedResult);
        assertEquals(expectedResult, roleRepository.findByIdIn(roleIds));
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        List<RoleEntity> expectedResult = List.of(roleEntity);
        when(roleRepository.findByName(name)).thenReturn(expectedResult);
        assertEquals(expectedResult, roleRepository.findByName(name));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> expectedResult = new PageImpl<>(List.of(roleEntity), pageable, 1);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedResult);
        assertEquals(expectedResult, roleRepository.findByOrganizationId(organizationId, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> expectedResult = Optional.of(roleEntity);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedResult);
        assertTrue(roleRepository.findByNameAndOrganizationId(name, organizationId).isPresent());
        assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId(name, organizationId).get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = List.of("testRole");
        List<RoleEntity> expectedResult = List.of(roleEntity);
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);
        assertEquals(expectedResult, roleRepository.findByOrganizationIdAndNameIn(organizationId, names));
    }

    @Test(expected = RuntimeException.class)
    public void testFindByOrganizationIdAndRoleIdsWithInvalidArgument() {
        roleRepository.findByOrganizationIdAndRoleIds(organizationId, null);
    }

    @Test(expected = RuntimeException.class)
    public void testFindByOrganizationIdAndRoleIdsWithEmptyRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(organizationId, new ArrayList<>());
    }

    @Test(expected = RuntimeException.class)
    public void testFindByOrganizationIdAndRoleIdsWithInvalidRoleId() {
        Collection<Long> roleIds = List.of(-1L, roleId);
        roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds);
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> expectedResult = List.of(roleEntity);
        when(iamUserRepository.findById(userId)).thenReturn(Optional.of(new IamUserEntity()));
        when(iamUserRepository.findById(userId).get().getRoleEntities()).thenReturn(expectedResult);
        assertEquals(expectedResult, roleRepository.findByUserId(userId));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> expectedResult = List.of(roleEntity);
        when(iamUserRepository.findById(userId)).thenReturn(Optional.of(new IamUserEntity()));
        when(iamUserRepository.findById(userId).get().getRoleEntities()).thenReturn(expectedResult);
        assertEquals(expectedResult, roleRepository.findByUserIdAndEnabled(userId, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> expectedResult = List.of(roleEntity);
        when(roleRepository.findAll()).thenReturn(expectedResult);
        assertEquals(expectedResult, roleRepository.findByRoleIdsAndEnabled(roleIds, true));
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateWithInvalidRoleEntity() {
        roleRepository.update(null);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateWithInvalidRoleId() {
        RoleEntity invalidRoleEntity = new RoleEntity();
        invalidRoleEntity.setId(-1L);
        roleRepository.update(invalidRoleEntity);
    }

    @Test
    public void testUpdate() {
        String newName = "updatedRoleName";
        Boolean newEnabled = false;
        String newDescription = "updated description";
        roleEntity.setName(newName);
        roleEntity.setEnabled(newEnabled);
        roleEntity.setDescription(newDescription);
        int expectedRowsAffected = 1;
        when(roleRepository.update(roleEntity)).thenReturn(expectedRowsAffected);
        assertEquals(expectedRowsAffected, roleRepository.update(roleEntity));
    }
}