package com.oceanbase.odc.metadb.iam;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByIdIn(any())).thenReturn(roles);

        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(1, result.size());
        assertEquals(roleEntity, result.get(0));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        when(roleRepository.findByName(any())).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByName("TestName");

        assertEquals(1, result.size());
        assertEquals(roleEntity, result.get(0));
    }

    // Add test methods for other methods in RoleRepository class

}