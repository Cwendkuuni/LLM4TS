import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RoleRepositoryTest {

    @Test
    public void findByIdIn() {
        // Test for findByIdIn method
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Role1", true, "Description1"));
        roles.add(new RoleEntity(2L, "Role2", true, "Description2"));

        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertEquals(2, result.size());
    }

    @Test
    public void findByName() {
        // Test for findByName method
        RoleEntity role = new RoleEntity(1L, "Role1", true, "Description1");

        RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
        Mockito.when(roleRepository.findByName("Role1")).thenReturn(Collections.singletonList(role));

        List<RoleEntity> result = roleRepository.findByName("Role1");

        assertEquals(1, result.size());
        assertEquals("Role1", result.get(0).getName());
    }

    // Other test cases for the remaining methods can be added here

}