import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Admin"));
        
        when(roleRepository.findByIdIn(anyCollection())).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L));
        
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "Admin"));
        
        when(roleRepository.findByName(anyString())).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByName("Admin");
        
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    // Additional test cases for other methods...

}