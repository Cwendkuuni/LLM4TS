package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByIdIn(anyCollection())).thenReturn(expectedResult);

        Collection<Long> ids = Arrays.asList(id1, id2);
        List<RoleEntity> result = roleRepositoryTest.findByIdIn(ids);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByName(name)).thenReturn(Arrays.asList(expectedRole));

        List<RoleEntity> result = roleRepositoryTest.findByName(name);

        assertEquals(expectedRole, result.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        Page<RoleEntity> expectedPage = Page.empty();
        when(roleRepository.findByOrganizationId(organizationId, any(Pageable.class))).thenReturn(expectedPage);

        Page<RoleEntity> result = roleRepositoryTest.findByOrganizationId(organizationId, Pageable.unpaged());

        verify(roleRepository, times(1)).findByOrganizationId(organizationId, any(Pageable.class));
        assertNull(result.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity expectedRole = new RoleEntity();
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedRole));

        RoleEntity result = roleRepositoryTest.findByNameAndOrganizationId(name, organizationId).get();

        assertEquals(expectedRole, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testName1", "testName2");
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, anyList())).thenReturn(expectedResult);

        List<String> namesList = Arrays.asList("testName1", "testName2");
        List<RoleEntity> result = roleRepositoryTest.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, any(Pageable.class))).thenReturn(expectedResult);

        Page<RoleEntity> result = roleRepositoryTest.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());

        assertEquals(expectedResult, result.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepositoryTest.findByUserId(userId);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepositoryTest.findByUserIdAndEnabled(userId, enabled);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepositoryTest.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        int expectedNumberOfRowsAffected = 1;

        doReturn(expectedNumberOfRowsAffected)
                .when(roleRepository)
                .update(any(RoleEntity.class));

        int result = roleRepositoryTest.update(roleEntity);

        assertEquals(expectedNumberOfRowsAffected, result);
    }
}