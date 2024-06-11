package com.oceanbase.odc.metadb.iam;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    RoleEntity role1 = new RoleEntity(1L, "ROLE1", true, "Description for Role1");
    RoleEntity role2 = new RoleEntity(2L, "ROLE2", true, "Description for Role2");

    @Before
    public void setup(){
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(role1, role2));
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertEquals(role1, roles.get(0));
        assertEquals(role2, roles.get(1));
    }

    // ... rest of test cases
}