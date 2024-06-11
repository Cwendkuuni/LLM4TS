package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setName("Admin");
        roles.add(role);

        when(roleRepository.findByIdIn(ids)).thenReturn(roles);

        List result = roleRepository.findByIdIn(ids);
        assertEquals(result.size(), roles.size());
        assertEquals(result.get(0).getName(), role.getName());
    }

    // Repeat similar tests for each other public method...

    @Test(expected=IllegalArgumentException.class)
    public void testFindByIdInNullList() {
        roleRepository.findByIdIn(null);
    }

    // Write similar tests for other methods...

    @After
    public void tearDown() {
        reset(roleRepository);
    }
}