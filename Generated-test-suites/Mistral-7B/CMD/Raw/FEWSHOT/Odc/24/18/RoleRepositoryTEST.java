package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;

public class RoleRepositoryTest {

    @Mock
    private RoleEntity roleEntity;

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity, new RoleEntity());
        when(roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId())))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(roleEntity.getId()));
        assertEquals(roleEntities, result);
        verify(roleRepository).findByIdIn(Collections.singletonList(roleEntity.getId()));
    }

    @Test
    public void testFindByName() {
        String name = "testName";
        when(roleRepository.findByName(name)).thenReturn(Arrays.asList(roleEntity));

        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(1, result.size());
        assertEquals(roleEntity, result.get(0));
        verify(roleRepository).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity, new RoleEntity());
        Pageable pageable = PageRequest.of(0, 10);

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(
                new PageImpl<>(roleEntities, pageable, roleEntities.size()));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(roleEntities, result.getContent());
        assertEquals(1, result.getTotalElements());
        verify(roleRepository).findByOrganizationId(organizationId, pageable);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "testName";
        Long organizationId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);

        when(roleRepository.findByNameAndOrganizationId(name, organizationId))
                .thenReturn(optionalRoleEntity);

        RoleEntity result = roleRepository.findByNameAndOrganizationId(name, organizationId).get();
        assertEquals(roleEntity, result);
        verify(roleRepository).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("testName1", "testName2");
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity, new RoleEntity());

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(roleEntities, result);
        verify(roleRepository).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(roleEntity.getId());
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity, new RoleEntity());
        Pageable pageable = PageRequest.of(0, 10);

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
                .thenReturn(new PageImpl<>(roleEntities, pageable, roleEntities.size()));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(roleEntities, result.getContent());
        assertEquals(1, result.getTotalElements());
        verify(roleRepository).findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity, new RoleEntity());

        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(roleEntities, result);
        verify(roleRepository).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity, new RoleEntity());

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(roleEntities, result);
        verify(roleRepository).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(roleEntity.getId());
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity, new RoleEntity());

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(roleEntities, result);
        verify(roleRepository).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        RoleEntity newRoleEntity = new RoleEntity();

        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                RoleEntity roleEntityParam = (RoleEntity) invocation.getArgument(0);
                RoleEntity actualRoleEntity = (RoleEntity) invocation.getMock().findById(roleEntityParam.getId()).get();
                assertSame(roleEntityParam, actualRoleEntity);
                newRoleEntity.setName("updatedName");
                return 1;
            }
        }).when(roleRepository).update(newRoleEntity);

        int result = roleRepository.update(newRoleEntity);
        assertEquals(1, result);
        verify(roleRepository).update(newRoleEntity);
    }
}