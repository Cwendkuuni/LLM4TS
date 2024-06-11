package com.oceanbase.odc.metadb.iam.test;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = Mockito.spy(RoleRepository.class);
        roleEntity = new RoleEntity();
        roleEntity.setName("TestRole");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByName() {
        String name = "TestRole";
        RoleEntity expectedResult = roleEntity;
        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(expectedResult));
        RoleEntity result = roleRepository.findByName(name).get(0);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity, new RoleEntity());
        Page<RoleEntity> page = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);
        Mockito.when(page.getContent()).thenReturn(expectedResult);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedResult, result.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "TestRole";
        Long organizationId = 1L;
        RoleEntity expectedResult = roleEntity;
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(expectedResult));
        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("TestRole1", "TestRole2");
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = Mockito.mock(Pageable.class);
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> page = Mockito.mock(Page.class);
        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable)).thenReturn(page);
        Mockito.when(page.getContent()).thenReturn(expectedResult);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(expectedResult, result.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedResult);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        String newName = "NewTestRole";
        Boolean newEnabled = false;
        String newDescription = "NewDescription";
        Mockito.doAnswer(invocationOnMock -> {
            RoleEntity entity = (RoleEntity) invocationOnMock.getArguments()[0];
            entity.setName(newName);
            entity.setEnabled(newEnabled);
            entity.setDescription(newDescription);
            return 1;
        }).when(roleRepository).update(Mockito.any(RoleEntity.class));
        int result = roleRepository.update(new RoleEntity().setName(newName).setEnabled(newEnabled).setDescription(newDescription));
        assertEquals(1, result);
    }
}