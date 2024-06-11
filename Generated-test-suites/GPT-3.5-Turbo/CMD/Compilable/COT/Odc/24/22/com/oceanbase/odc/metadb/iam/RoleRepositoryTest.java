
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    public void testFindByName() {
        String name = "Admin";
        List<RoleEntity> roles = roleRepository.findByName(name);
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    // Additional test cases for other methods omitted for brevity

}

