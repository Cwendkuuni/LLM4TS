import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepositoryImpl roleRepositoryImpl;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleList = Arrays.asList(new RoleEntity(1L, "Admin"), new RoleEntity(2L, "User"));
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roleList);
        List<RoleEntity> result = roleRepositoryImpl.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleList = Collections.singletonList(new RoleEntity(1L, "Admin"));
        Mockito.when(roleRepository.findByName("Admin")).thenReturn(roleList);
        List<RoleEntity> result = roleRepositoryImpl.findByName("Admin");
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    // Add tests for other methods

}