package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Mockito.when(roleRepository.findByIdIn(Mockito.any()))
                .thenReturn(Optional.of(new RoleEntity()));
        Mockito.when(roleRepository.findAll()).thenReturn(roleEntities);

        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(id1, id2));
        assertTrue(roles.size() > 0);
        assertTrue(roles.contains(new RoleEntity()));
    }

    @Test
    public void testFindByName() {
        String name = "test_name";
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.findByName(name)).thenReturn(Optional.of(roleEntity));
        RoleEntity result = roleRepository.findByName(name);
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable))
                .thenReturn(page);

        Page<RoleEntity> results = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(page.getSize(), results.getSize());
        assertTrue(results.containsAll(roleEntities));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test_name";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity();
        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId))
                .thenReturn(Optional.of(roleEntity));

        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(organizationId, result.getOrganizationId());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("test_name1", "test_name2");
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names))
                .thenReturn(roleEntities);

        List<RoleEntity> results = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(roleEntities.size(), results.size());
        assertTrue(results.containsAll(roleEntities));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        Pageable pageable = Mockito.mock(Pageable.class);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
                .thenReturn(new PageImpl<>(roleEntities));

        Page<RoleEntity> results = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(pageable.getPageSize(), results.getSize());
        assertTrue(results.containsAll(roleEntities));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());

        Mockito.when(roleRepository.findByUserId(userId))
                .thenReturn(roleEntities);

        List<RoleEntity> results = roleRepository.findByUserId(userId);
        assertEquals(roleEntities.size(), results.size());
        assertTrue(results.containsAll(roleEntities));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Collections.emptyList();

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled))
                .thenReturn(roleEntities);

        List<RoleEntity> results = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Collections.emptyList();

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled))
                .thenReturn(roleEntities);

        List<RoleEntity> results = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        String name = "test_name";
        Boolean enabled = true;
        String description = "test_description";

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleEntity.setName(name);
        roleEntity.setEnabled(enabled);
        roleEntity.setDescription(description);

        Mockito.when(roleRepository.findById(id)).thenReturn(Optional.of(roleEntity));

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}