package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    private SimpleJpaRepository<RoleEntity, Long> jpaRepositoryMock;

    @Before
    public void setup() {
        jpaRepositoryMock = mock(SimpleJpaRepository.class);
        RoleRepository roleRepositoryImpl = new RoleRepositoryImpl();
        roleRepositoryImpl.setJpaRepository(jpaRepositoryMock);
        this.roleRepository = roleRepositoryImpl;
    }

    @Test
    public void testFindByIdIn() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(jpaRepositoryMock.findAllById(Collections.singleton(id1))).thenReturn(Optional.of(new RoleEntity()));
        when(jpaRepositoryMock.findAllById(Collections.singleton(id2))).thenReturn(Optional.of(new RoleEntity()));
        when(jpaRepositoryMock.findAllById(Arrays.asList(id1, id2))).thenReturn(Optional.of(expectedList));

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(id1, id2));
        assertEquals(expectedList, result);
        verify(jpaRepositoryMock, times(1)).findAllById(Arrays.asList(id1, id2));
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(jpaRepositoryMock.findAll(Mockito.eq(RoleEntitySpecification.hasName(name)))).thenReturn(expectedList);

        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(expectedList, result);
        verify(jpaRepositoryMock, times(1)).findAll(Mockito.eq(RoleEntitySpecification.hasName(name)));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = Mockito.mock(Pageable.class);
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedList);

        when(jpaRepositoryMock.findAll(Mockito.eq(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))),
                Mockito.eq(organizationId))).thenReturn(expectedPage);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(expectedList, result.getContent());
        assertEquals(1, result.getTotalElements());
        verify(jpaRepositoryMock, times(1)).findAll(eq(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))),
                eq(organizationId));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        RoleEntity expectedEntity = new RoleEntity();
        when(jpaRepositoryMock.findOne(Mockito.eq(RoleEntitySpecification.hasNameAndOrganizationId(name, organizationId))))
                .thenReturn(Optional.of(expectedEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertEquals(expectedEntity, result.get());
        verify(jpaRepositoryMock, times(1)).findOne(Mockito.eq(RoleEntitySpecification.hasNameAndOrganizationId(name, organizationId)));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testName1", "testName2");
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(jpaRepositoryMock.findAll(Mockito.eq(RoleEntitySpecification.hasOrganizationIdAndNameIn(organizationId, names))))
                .thenReturn(expectedList);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(expectedList, result);
        verify(jpaRepositoryMock, times(1)).findAll(Mockito.eq(RoleEntitySpecification.hasOrganizationIdAndNameIn(organizationId, names)));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());
        Page<RoleEntity> expectedPage = new PageImpl<>(expectedList);

        when(jpaRepositoryMock.countAll(Mockito.eq(RoleEntitySpecification.hasOrganizationIdAndIdsIn(organizationId, roleIds)))).thenReturn(1L);
        when(jpaRepositoryMock.findAll(Mockito.eq(RoleEntitySpecification.hasOrganizationIdAndIdsIn(organizationId, roleIds)),
                pageable)).thenReturn(expectedPage);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(expectedList, result.getContent());
        assertEquals(1, result.getTotalElements());
        verify(jpaRepositoryMock, times(1)).countAll(Mockito.eq(RoleEntitySpecification.hasOrganizationIdAndIdsIn(organizationId, roleIds)));
        verify(jpaRepositoryMock, times(1)).findAll(Mockito.eq(RoleEntitySpecification.hasOrganizationIdAndIdsIn(organizationId, roleIds)),
                eq(pageable));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(jpaRepositoryMock.findAll(Mockito.eq(RoleUserEntitySpecification.hasUserId(userId)))).thenReturn(expectedList);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(expectedList, result);
        verify(jpaRepositoryMock, times(1)).findAll(Mockito.eq(RoleUserEntitySpecification.hasUserId(userId)));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(jpaRepositoryMock.findAll(Mockito.eq(RoleEntitySpecification.hasUserIdAndIsEnabled(userId, enabled)))).thenReturn(expectedList);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(expectedList, result);
        verify(jpaRepositoryMock, times(1)).findAll(Mockito.eq(RoleEntitySpecification.hasUserIdAndIsEnabled(userId, enabled)));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedList = Arrays.asList(new RoleEntity(), new RoleEntity());

        when(jpaRepositoryMock.findAll(Mockito.eq(RoleEntitySpecification.hasIdsAndIsEnabled(roleIds, enabled)))).thenReturn(expectedList);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(expectedList, result);
        verify(jpaRepositoryMock, times(1)).findAll(Mockito.eq(RoleEntitySpecification.hasIdsAndIsEnabled(roleIds, enabled)));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        when(jpaRepositoryMock.saveAndFlush(roleEntity)).thenReturn(roleEntity);

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        verify(jpaRepositoryMock, times(1)).saveAndFlush(roleEntity);
    }
}