package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = Mockito.spy(RoleRepositoryImpl.class); // Assuming RoleRepositoryImpl is the implementation class
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = Arrays.asList(id1, id2);
        List<RoleEntity> expectedRoles = Arrays.asList(new RoleEntity(id1, "role1"), new RoleEntity(id2, "role2"));
        Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);

        assertEquals(2, actualRoles.size());
        assertEquals("role1", actualRoles.get(0).getName());
        assertEquals("role2", actualRoles.get(1).getName());
    }

    @Test
    public void testFindByName() {
        String name = "testRole";
        RoleEntity roleEntity = new RoleEntity(1L, name, true);
        Mockito.when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> actualRoles = roleRepository.findByName(name);

        assertEquals(1, actualRoles.size());
        assertEquals(name, actualRoles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class); // Assuming Pageable is mocked properly
        RoleEntity roleEntity1 = new RoleEntity(1L, "role1", true);
        RoleEntity roleEntity2 = new RoleEntity(2L, "role2", true);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity1, roleEntity2);
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);

        Mockito.when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(2, actualPage.getContent().size());
        assertEquals(roleEntity1, actualPage.getContent().get(0));
        assertEquals(roleEntity2, actualPage.getContent().get(1));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testRole";
        Long organizationId = 1L;
        RoleEntity roleEntity = new RoleEntity(1L, name, true);

        Mockito.when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> actualRole = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(actualRole.isPresent());
        assertEquals(name, actualRole.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("role1", "role2");
        RoleEntity roleEntity1 = new RoleEntity(1L, "role1", true);
        RoleEntity roleEntity2 = new RoleEntity(2L, "role2", true);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity1, roleEntity2);

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, actualRoles.size());
        assertTrue(actualRoles.contains(roleEntity1));
        assertTrue(actualRoles.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        RoleEntity roleEntity1 = new RoleEntity(1L, "role1", true);
        RoleEntity roleEntity2 = new RoleEntity(2L, "role2", true);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity1, roleEntity2);
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedRoles);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, null);

        assertEquals(2, actualPage.getContent().size());
        assertTrue(actualPage.getContent().contains(roleEntity1));
        assertTrue(actualPage.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        RoleEntity roleEntity1 = new RoleEntity(1L, "role1", true);
        RoleEntity roleEntity2 = new RoleEntity(2L, "role2", true);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity1, roleEntity2);

        Mockito.when(roleRepository.findByUserId(userId)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByUserId(userId);

        assertEquals(2, actualRoles.size());
        assertTrue(actualRoles.contains(roleEntity1));
        assertTrue(actualRoles.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        RoleEntity roleEntity = new RoleEntity(1L, "role1", enabled);

        Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> actualRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(1, actualRoles.size());
        assertEquals(roleEntity, actualRoles.get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        Boolean enabled = true;
        RoleEntity roleEntity1 = new RoleEntity(1L, "role1", enabled);
        RoleEntity roleEntity2 = new RoleEntity(2L, "role2", enabled);
        List<RoleEntity> expectedRoles = Arrays.asList(roleEntity1, roleEntity2);

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoles);

        List<RoleEntity> actualRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, actualRoles.size());
        assertTrue(actualRoles.contains(roleEntity1));
        assertTrue(actualRoles.contains(roleEntity2));
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        String newName = "newRoleName";
        Boolean newEnabled = false;

        RoleEntity roleEntity = new RoleEntity(id, "oldRoleName", true);

        Mockito.doAnswer(invocationOnMock -> {
            RoleEntity role = (RoleEntity) invocationOnMock.getArguments()[0];
            role.setName(newName);
            role.setEnabled(newEnabled);
            return 1;
        }).when(roleRepository).update(Mockito.eq(roleEntity));

        int actualRowsAffected = roleRepository.update(new RoleEntity(id, newName, newEnabled));

        assertEquals(1, actualRowsAffected);
    }
}