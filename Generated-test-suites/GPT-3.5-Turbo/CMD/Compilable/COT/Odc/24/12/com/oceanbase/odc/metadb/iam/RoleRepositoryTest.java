package com.oceanbase.odc.metadb.iam;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedRoles = Arrays.asList(mock(RoleEntity.class));

        when(roleRepository.findByIdIn(ids)).thenReturn(expectedRoles);
        List<RoleEntity> actualRoles = roleRepository.findByIdIn(ids);

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> expectedRoles = Arrays.asList(mock(RoleEntity.class));

        when(roleRepository.findByName(name)).thenReturn(expectedRoles);
        List<RoleEntity> actualRoles = roleRepository.findByName(name);

        assertEquals(expectedRoles, actualRoles);
    }

    // Add test cases for other methods...

}