package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupitermock.SpringJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.iam.entities.RoleEntity;
import com.oceanbase.odc.metadb.iam.exceptions.RoleNotFoundException;

@RunWith(SpringJUnitRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
@Transactional
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setup() {
        roleRepository = Mockito.spy(RoleRepositoryImpl.class);
    }

    @Test
    public void testFindById() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));

        RoleEntity result = roleRepository.findById(1L).get();
        assertEquals("role1", result.getName());
    }

    @Test
    public void testFindById_RoleNotFound() {
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleRepository.findById(1L).get());
    }

    @Test
    public void testFindAll() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("role1");

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("role2");

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);

        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findAll();
        assertEquals(2, result.size());
        assertEquals("role1", result.get(0).getName());
        assertEquals("role2", result.get(1).getName());
    }

    @Test
    public void testFindByName() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");

        Mockito.when(roleRepository.findByName("role1")).thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByName("role1").get(0);
        assertEquals("role1", result.getName());
    }

    @Test
    public void testFindByName_NoMatchingRole() {
        Mockito.when(roleRepository.findByName("role1")).thenReturn(Collections.emptyList());

        assertNull(roleRepository.findByName("role1").get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("role1");
        roleEntity1.setOrganizationId(1L);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("role2");
        roleEntity2.setOrganizationId(1L);

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);

        Pageable pageable = new PageRequest(0, 10);

        Mockito.when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(Mockito.mock(Page.class));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertTrue(result.hasContent());
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");
        roleEntity.setOrganizationId(1L);

        Mockito.when(roleRepository.findByNameAndOrganizationId("role1", 1L)).thenReturn(Optional.of(roleEntity));

        RoleEntity result = roleRepository.findByNameAndOrganizationId("role1", 1L).get();
        assertEquals("role1", result.getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("role1");
        roleEntity1.setOrganizationId(1L);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("role2");
        roleEntity2.setOrganizationId(1L);

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);

        Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("role1", "role2"))).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("role1", "role2"));
        assertEquals(2, result.size());
        assertEquals("role1", result.get(0).getName());
        assertEquals("role2", result.get(1).getName());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIdsAndEnabled() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("role1");
        roleEntity1.setOrganizationId(1L);
        roleEntity1.setIsEnabled(true);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("role2");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setIsEnabled(false);

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);

        Mockito.when(roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(1L, Arrays.asList(1L, 2L), true)).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIdsAndEnabled(1L, Arrays.asList(1L, 2L), true);
        assertEquals(2, result.size());
        assertTrue(result.get(0).isEnabled());
        assertFalse(result.get(1).isEnabled());
    }

    @Test
    public void testFindByUserId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");
        roleEntity.setUserId(1L);

        Mockito.when(roleRepository.findByUserId(1L)).thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByUserId(1L).get(0);
        assertEquals("role1", result.getName());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");
        roleEntity.setUserId(1L);
        roleEntity.setIsEnabled(true);

        Mockito.when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(Collections.singletonList(roleEntity));

        RoleEntity result = roleRepository.findByUserIdAndEnabled(1L, true).get(0);
        assertEquals("role1", result.getName());
        assertTrue(result.isEnabled());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setName("role1");
        roleEntity1.setIsEnabled(true);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setName("role2");
        roleEntity2.setIsEnabled(false);

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity1);
        roles.add(roleEntity2);

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true)).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(2, result.size());
        assertTrue(result.get(0).isEnabled());
        assertFalse(result.get(1).isEnabled());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1");

        Mockito.when(roleRepository.update(Mockito.any(RoleEntity.class))).thenReturn(1);

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullRoleEntity() {
        roleRepository.update(null);
    }
}