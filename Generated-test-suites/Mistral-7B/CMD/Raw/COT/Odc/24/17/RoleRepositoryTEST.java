package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.SpecificationSupport;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        Collection<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);

        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.findByIdIn(ids));
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByName(name)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.findByName(name));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);

        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Page<RoleEntity> page = Mockito.mock(Page.class);
        page.setContent(expectedResult);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        assertEquals(expectedResult, roleRepository.findByOrganizationId(organizationId, pageable).getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        Optional<RoleEntity> expectedResult = Optional.ofNullable(Mockito.mock(RoleEntity.class));
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(expectedResult);

        assertTrue(roleRepository.findByNameAndOrganizationId(name, organizationId).isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("testName1");
        names.add("testName2");

        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.findByOrganizationIdAndNameIn(organizationId, names));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndRoleIdsWithNullPageable() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();

        roleRepository.findByOrganizationIdAndRoleIds(organizationId, null, roleIds);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, Mockito.any(Pageable.class), roleIds)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.findByOrganizationIdAndRoleIds(organizationId, Mockito.mock(Pageable.class), roleIds).getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.findByUserId(userId));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.findByUserIdAndEnabled(userId, enabled));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Mockito.mock(List.class);
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.findByRoleIdsAndEnabled(roleIds, enabled));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = Mockito.mock(RoleEntity.class);
        int expectedResult = 1;

        Mockito.when(roleRepository.update(roleEntity)).thenReturn(expectedResult);

        assertEquals(expectedResult, roleRepository.update(roleEntity));
    }
}