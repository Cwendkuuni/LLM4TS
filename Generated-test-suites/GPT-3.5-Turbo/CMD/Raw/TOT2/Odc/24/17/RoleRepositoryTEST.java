import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = Arrays.asList(new RoleEntity(1L, "Role1", true, "Description1"));
        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(roles);
        
        List<RoleEntity> result = roleRepository.findByIdIn(Collections.singletonList(1L));
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Role1", result.get(0).getName());
    }

    // Test other methods in a similar manner

}