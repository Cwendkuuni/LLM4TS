package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    @InjectMocks
    @Spy
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(id1, "role1"), new RoleEntity(id2, "role2"));
        when(roleRepository.findByIdIn(Arrays.asList(id1, id2))).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(id1, id2));
        assertEquals(2, result.size());
        assertEquals("role1", result.get(0).getName());
        assertEquals("role2", result.get(1).getName());
    }

    @Test
    public void testFindByName() {
        String name = "role1";
        RoleEntity roleEntity = new RoleEntity(1L, name);
        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        Page<RoleEntity> page = Page.of(roleEntities, pageable);
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals(roleEntities.get(0), result.getContent().get(0));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "role1";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity(1L, name);
        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(result.isPresent());
        assertEquals(name, result.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("role1", "role2");
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntities.get(0)));
        assertTrue(result.contains(roleEntities.get(1)));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        Page<RoleEntity> page = Page.of(roleEntities, Pageable.unpaged());
        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged())).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Pageable.unpaged());
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().containsAll(roleEntities));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1"), new RoleEntity(2L, "role2"));
        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(roleEntities));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = false;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1", enabled), new RoleEntity(2L, "role2", enabled));
        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(roleEntities));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = false;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "role1", enabled), new RoleEntity(2L, "role2", enabled));
        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(roleEntities));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(1L, "role1", true, "description1");
        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                RoleEntity updatedRoleEntity = (RoleEntity) invocation.getArguments()[0];
                updatedRoleEntity.setDescription("description2");
                return 1;
            }
        }).when(roleRepository).update(roleEntity);

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}