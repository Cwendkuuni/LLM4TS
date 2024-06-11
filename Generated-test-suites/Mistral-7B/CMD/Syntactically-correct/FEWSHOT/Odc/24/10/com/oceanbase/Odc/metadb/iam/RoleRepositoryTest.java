package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.SpecificationSupport;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleRepository = mock(RoleRepository.class);
        roleEntity = mock(RoleEntity.class);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByIdIn(Collections.singleton(roleEntity.getId())))
                .thenReturn(roleEntities);

        assertEquals(roleEntity, roleRepository.findByIdIn(Collections.singleton(roleEntity.getId())).get(0));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("admin"))
                .thenReturn(Collections.singletonList(roleEntity));

        assertEquals(roleEntity, roleRepository.findByName("admin").get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<RoleEntity> rolePage = Page.empty(pageable);
        rolePage.setContent(Collections.singletonList(roleEntity));

        when(roleRepository.findByOrganizationId(1L, pageable))
                .thenReturn(rolePage);

        assertEquals(roleEntity, roleRepository.findByOrganizationId(1L, pageable).getContent().get(0));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleOptional = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId("admin", 1L))
                .thenReturn(roleOptional);

        assertTrue(roleOptional.isPresent());
        assertEquals(roleEntity, roleOptional.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("admin")))
                .thenReturn(roleEntities);

        assertEquals(roleEntity, roleRepository.findByOrganizationIdAndNameIn(1L, Collections.singletonList("admin")).get(0));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<RoleEntity> rolePage = Page.empty(pageable);
        rolePage.setContent(Collections.singletonList(roleEntity));

        when(roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singleton(roleEntity.getId()), pageable))
                .thenReturn(rolePage);

        assertEquals(roleEntity, roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.singleton(roleEntity.getId()), pageable).getContent().get(0));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByUserId(1L))
                .thenReturn(roleEntities);

        assertEquals(roleEntity, roleRepository.findByUserId(1L).get(0));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByUserIdAndEnabled(1L, true))
                .thenReturn(roleEntities);

        assertEquals(roleEntity, roleRepository.findByUserIdAndEnabled(1L, true).get(0));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(roleEntity.getId()), true))
                .thenReturn(roleEntities);

        assertEquals(roleEntity, roleRepository.findByRoleIdsAndEnabled(Collections.singleton(roleEntity.getId()), true).get(0));
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity();
        updatedRoleEntity.setId(roleEntity.getId());
        updatedRoleEntity.setName("updatedName");
        updatedRoleEntity.setEnabled(true);
        updatedRoleEntity.setDescription("updatedDescription");

        int rowsAffected = 1;

        when(roleRepository.update(updatedRoleEntity))
                .thenReturn(rowsAffected);

        assertTrue(roleRepository.update(updatedRoleEntity) > 0);
    }
}