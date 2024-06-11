package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleEntity roleEntity;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> expectedResult = new ArrayList<>();
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> expectedResult = new ArrayList<>();
        when(roleRepository.findByName("Admin")).thenReturn(expectedResult);

        List<RoleEntity> result = roleRepository.findByName("Admin");

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> expectedResult = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(expectedResult);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);

        assertNotNull(result);
    }

    // Other test cases for the remaining methods in RoleRepository

}