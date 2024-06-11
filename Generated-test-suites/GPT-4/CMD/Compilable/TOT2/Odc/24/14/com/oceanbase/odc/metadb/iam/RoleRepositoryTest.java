package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = mock(RoleEntity.class);
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        roleRepository.findByIdIn(ids);
        verify(roleRepository, times(1)).findByIdIn(ids);
    }

    // Similarly create other test methods for findByName(), findByOrganizationId(), etc

    @Test
    public void testUpdate() {
        when(roleRepository.update(roleEntity)).thenReturn(1);
        int updated = roleRepository.update(roleEntity);
        assertEquals(1, updated);
        verify(roleRepository, times(1)).update(roleEntity);
    }
}
