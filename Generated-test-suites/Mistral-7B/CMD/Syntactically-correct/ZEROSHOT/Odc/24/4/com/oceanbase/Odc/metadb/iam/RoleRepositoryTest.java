package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(id1, id2));

        assertEquals(expected, result);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        RoleEntity expected = new RoleEntity();

        RoleEntity result = roleRepository.findByName(name).get();

        assertEquals(expected, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Pageable.ofSize(10, 0);
        Page<RoleEntity> expected = Page.empty();

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        RoleEntity expected = new RoleEntity();

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> nameList = Arrays.asList("test1", "test2");
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, nameList);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = Pageable.ofSize(10, 0);
        Page<RoleEntity> expected = Page.empty();

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expected = Arrays.asList(new RoleEntity(), new RoleEntity());

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expected = Collections.emptyList();

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expected = Collections.emptyList();

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(expected, result);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();

        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
    }
}