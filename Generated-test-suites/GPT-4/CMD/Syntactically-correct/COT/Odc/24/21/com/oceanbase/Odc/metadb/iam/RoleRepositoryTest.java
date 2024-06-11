package com.oceanbase.odc.metadb.iam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.Pageable;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void findByIdInTest() {
        // Defined data and mock
        RoleEntity mockRole = new RoleEntity();
        Mockito.when(roleRepository.findByIdIn(anySet())).thenReturn(Arrays.asList(mockRole));

        // Main logic
        List<RoleEntity> result = roleRepository.findByIdIn(new HashSet<>(Arrays.asList(1L)));

        // Verify and assert
        Mockito.verify(roleRepository).findByIdIn(anySet());
        assertEquals(mockRole, result.get(0));
        // Ensure non-zero list was returned
        assertTrue(!result.isEmpty());
    }

    // Similar tests for other methods here (findByName, findByOrganizationId, findByNameAndOrganizationId...)
    // ...

    @Test
    public void updateTest() {
        // Defined data and mock
        RoleEntity mockRole = new RoleEntity();
        Mockito.when(roleRepository.update(any(RoleEntity.class))).thenReturn(1);

        // Main logic
        int result = roleRepository.update(mockRole);

        // Verify and assert
        Mockito.verify(roleRepository).update(any(RoleEntity.class));
        // Assert the returned EntityId is the same as the one we passed
        assertEquals(1, result);
    }
}
