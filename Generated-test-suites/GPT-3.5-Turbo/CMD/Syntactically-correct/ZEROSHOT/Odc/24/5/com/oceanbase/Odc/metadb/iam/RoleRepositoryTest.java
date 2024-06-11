import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = Arrays.asList(new RoleEntity(1L, "Role1", true, "Description1"),
                new RoleEntity(2L, "Role2", true, "Description2"));

        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Role1", result.get(0).getName());
        assertEquals("Role2", result.get(1).getName());
    }

    // Add more test methods for other methods in the RoleRepository class

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(1L, "Role1", true, "Description1");

        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
    }

}