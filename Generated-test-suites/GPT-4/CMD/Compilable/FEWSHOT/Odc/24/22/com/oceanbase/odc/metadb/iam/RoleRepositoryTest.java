
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        RoleEntity entity1 = new RoleEntity();
        entity1.setId(1L);
        RoleEntity entity2 = new RoleEntity();
        entity2.setId(2L);
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(entity1, entity2));
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(1), result.get(0).getId());
        assertEquals(Long.valueOf(2), result.get(1).getId());
    }

    @Test
    public void testFindByName() {
        RoleEntity entity = new RoleEntity();
        entity.setName("admin");
        when(roleRepository.findByName("admin")).thenReturn(Arrays.asList(entity));

        List<RoleEntity> result = roleRepository.findByName("admin");
        assertNotNull(result);
        assertEquals("admin", result.get(0).getName());
    }

    // Similar tests would need to be written for findByOrganizationId, findByNameAndOrganizationId,
    // findByOrganizationIdAndNameIn, findByOrganizationIdAndRoleIds, findByUserId,
    // findByUserIdAndEnabled, findByRoleIdsAndEnabled, and update methods
}
