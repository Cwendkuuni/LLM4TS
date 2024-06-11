package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {
    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Mock
    private Page<RoleEntity> roleEntityPage;

    private final Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
    private final Long organizationId = 1L;
    private final Pageable pageable = PageRequest.of(0, 1);
    private final Long userId = 2L;
    
    @Before
    public void setup() {
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        when(roleRepository.findByIdIn(ids)).thenReturn(Arrays.asList(roleEntity));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByIdIn(ids));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("test")).thenReturn(Arrays.asList(roleEntity));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByName("test"));
    }

    @Test
    public void testFindByOrganizationId() {
        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(roleEntityPage);
        assertEquals(roleEntityPage, roleRepository.findByOrganizationId(organizationId, pageable));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        when(roleRepository.findByNameAndOrganizationId("test", organizationId)).thenReturn(Optional.of(roleEntity));
        assertEquals(Optional.of(roleEntity), roleRepository.findByNameAndOrganizationId("test", organizationId));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, Arrays.asList("test"))).thenReturn(Arrays.asList(roleEntity));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByOrganizationIdAndNameIn(organizationId, Arrays.asList("test")));
    }

    @Test
    public void testFindByUserId() {
        when(roleRepository.findByUserId(userId)).thenReturn(Arrays.asList(roleEntity));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByUserId(userId));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        when(roleRepository.findByUserIdAndEnabled(userId, true)).thenReturn(Arrays.asList(roleEntity));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByUserIdAndEnabled(userId, true));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        when(roleRepository.findByRoleIdsAndEnabled(ids, true)).thenReturn(Arrays.asList(roleEntity));
        assertEquals(Arrays.asList(roleEntity), roleRepository.findByRoleIdsAndEnabled(ids, true));
    }

    @Test
    public void testUpdate() {
        when(roleRepository.update(roleEntity)).thenReturn(1);
        assertEquals(1, roleRepository.update(roleEntity));
    }
}
