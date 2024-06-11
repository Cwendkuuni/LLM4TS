package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        roleEntity = new RoleEntity();
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResults = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> results = roleRepository.findByIdIn(ids);
        assertEquals(expectedResults, results);
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity expectedResult = new RoleEntity();
        RoleEntity result = roleRepository.findByName(name).get();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> expectedResults = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> results = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedResults, results.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity expectedResult = new RoleEntity();
        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testRole1", "testRole2");
        List<RoleEntity> expectedResults = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> results = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedResults, results);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> expectedResults = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> results = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(expectedResults, results.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResults = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> results = roleRepository.findByUserId(userId);
        assertEquals(expectedResults, results);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResults = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> results = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedResults, results);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResults = Arrays.asList(new RoleEntity(), new RoleEntity());
        List<RoleEntity> results = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedResults, results);
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        String newName = "newName";
        Boolean newEnabled = true;
        String newDescription = "newDescription";
        RoleEntity roleEntityToBeUpdated = new RoleEntity();
        roleEntityToBeUpdated.setId(id);
        roleEntityToBeUpdated.setName(newName);
        roleEntityToBeUpdated.setEnabled(newEnabled);
        roleEntityToBeUpdated.setDescription(newDescription);

        int result = roleRepository.update(roleEntityToBeUpdated);
        assertEquals(1, result);
    }
}