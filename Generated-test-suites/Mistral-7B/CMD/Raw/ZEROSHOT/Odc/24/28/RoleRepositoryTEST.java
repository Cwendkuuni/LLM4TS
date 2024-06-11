package com.oceanbase.odc.metadb.iam.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepositoryTest;

    private RoleEntity roleEntity;
    private Pageable pageable;
    private List<RoleEntity> roleEntities;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("testRole");
        roleEntity.setOrganizationId(1L);
        roleEntity.setIsEnabled(true);
        roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(roleEntities)).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepositoryTest.findByIdIn(roleEntities));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName(roleEntity.getName())).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepositoryTest.findByName(roleEntity.getName()));
    }

    @Test
    public void testFindByOrganizationId() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(2L);
        roleEntity1.setName("testRole1");
        roleEntity1.setOrganizationId(1L);
        roleEntities.add(roleEntity1);

        when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(Page.of(roleEntities));
        assertEquals(roleEntities, roleRepositoryTest.findByOrganizationId(1L, pageable).getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId(roleEntity.getName(), roleEntity.getOrganizationId()))
                .thenReturn(Optional.ofNullable(roleEntity));
        assertEquals(roleEntity, roleRepositoryTest.findByNameAndOrganizationId(roleEntity.getName(),
                roleEntity.getOrganizationId()).orElse(null));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(3L);
        roleEntity2.setName("testRole2");
        roleEntity2.setOrganizationId(1L);
        roleEntities.add(roleEntity2);

        when(roleRepository.findByOrganizationIdAndNameIn(roleEntity.getOrganizationId(), roleEntities))
                .thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepositoryTest.findByOrganizationIdAndNameIn(roleEntity.getOrganizationId(),
                roleEntities));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(2L);
        roleEntity1.setName("testRole1");
        roleEntity1.setOrganizationId(1L);
        roleEntities.add(roleEntity1);

        when(roleRepository.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(), roleEntities))
                .thenReturn(Page.of(roleEntities));
        assertEquals(roleEntities, roleRepositoryTest.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(),
                roleEntities).getContent());
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(1L)).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepositoryTest.findByUserId(1L));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepositoryTest.findByUserIdAndEnabled(1L, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        when(roleRepository.findByRoleIdsAndEnabled(roleEntities, true)).thenReturn(roleEntities);
        assertEquals(roleEntities, roleRepositoryTest.findByRoleIdsAndEnabled(roleEntities, true));
    }

    @Test
    public void testUpdate() {
        RoleEntity newRoleEntity = new RoleEntity();
        newRoleEntity.setId(1L);
        newRoleEntity.setName("newTestRole");
        newRoleEntity.setIsEnabled(true);

        when(roleRepository.update(newRoleEntity)).thenReturn(1);
        assertEquals(1, roleRepositoryTest.update(newRoleEntity));
    }
}