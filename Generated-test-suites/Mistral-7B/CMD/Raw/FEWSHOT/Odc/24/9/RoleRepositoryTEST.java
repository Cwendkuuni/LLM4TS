package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private SimpleJpaRepository simpleJpaRepositoryMock;

    @Before
    public void setUp() {
        simpleJpaRepositoryMock = Mockito.mock(SimpleJpaRepository.class);
        RoleEntity roleEntity = Mockito.mock(RoleEntity.class);
        when(simpleJpaRepositoryMock.findById(anyLong())).thenReturn(Optional.of(roleEntity));
        when(simpleJpaRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        when(simpleJpaRepositoryMock.save(any(RoleEntity.class))).thenReturn(roleEntity);
        roleRepository = Mockito.spy(new RoleRepository() {
            {
                setEntityInformation(simpleJpaRepositoryMock);
            }
        });
    }

    @After
    public void tearDown() {
        reset(simpleJpaRepositoryMock);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        RoleEntity expectedRoleEntity = new RoleEntity();
        when(simpleJpaRepositoryMock.findById(id)).thenReturn(Optional.of(expectedRoleEntity));
        RoleEntity actualRoleEntity = roleRepository.findById(id).get();
        assertEquals(expectedRoleEntity, actualRoleEntity);
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        verify(simpleJpaRepositoryMock).save(roleEntity);
        assertEquals(roleEntity, savedRoleEntity);
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> expectedRoleEntities = new ArrayList<>();
        when(simpleJpaRepositoryMock.findAll()).thenReturn(expectedRoleEntities);
        List<RoleEntity> actualRoleEntities = roleRepository.findAll();
        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = Arrays.asList(id1, id2);
        List<RoleEntity> expectedRoleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(simpleJpaRepositoryMock.findByIdIn(ids)).thenReturn(new ArrayList<>(expectedRoleEntities));
        List<RoleEntity> actualRoleEntities = roleRepository.findByIdIn(ids);
        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByName() {
        String name = "roleName";
        List<RoleEntity> expectedRoleEntities = Collections.singletonList(new RoleEntity());
        when(simpleJpaRepositoryMock.findByName(name)).thenReturn(expectedRoleEntities);
        List<RoleEntity> actualRoleEntities = roleRepository.findByName(name);
        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> expectedRoleEntities = Collections.emptyList();
        Pageable pageable = Mockito.mock(Pageable.class);
        when(simpleJpaRepositoryMock.findByOrganizationId(organizationId, pageable)).thenReturn(new PageImpl<>(expectedRoleEntities));
        Page<RoleEntity> actualPage = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedRoleEntities, actualPage.getContent());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "roleName";
        Long organizationId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.of(new RoleEntity());
        when(simpleJpaRepositoryMock.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRoleEntity);
        RoleEntity actualRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(optionalRoleEntity.get(), actualRoleEntity);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("name1", "name2");
        List<RoleEntity> expectedRoleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(simpleJpaRepositoryMock.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(expectedRoleEntities);
        List<RoleEntity> actualRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> expectedPage = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()), Mockito.mock(Pageable.class), 2);
        when(simpleJpaRepositoryMock.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.any(Pageable.class))).thenReturn(expectedPage);
        Page<RoleEntity> actualPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, Mockito.mock(Pageable.class));
        assertEquals(expectedPage.getContent(), actualPage.getContent());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedRoleEntities = Collections.singletonList(new RoleEntity());
        when(simpleJpaRepositoryMock.findByUserId(userId)).thenReturn(expectedRoleEntities);
        List<RoleEntity> actualRoleEntities = roleRepository.findByUserId(userId);
        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = Collections.singletonList(new RoleEntity());
        when(simpleJpaRepositoryMock.findByUserIdAndEnabled(userId, enabled)).thenReturn(expectedRoleEntities);
        List<RoleEntity> actualRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedRoleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(simpleJpaRepositoryMock.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(expectedRoleEntities);
        List<RoleEntity> actualRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedRoleEntities, actualRoleEntities);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleRepository.update(roleEntity);
        verify(simpleJpaRepositoryMock).save(roleEntity);
    }
}