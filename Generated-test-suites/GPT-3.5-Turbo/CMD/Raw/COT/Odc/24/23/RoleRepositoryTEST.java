import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
        roles.add(new RoleEntity(1L, "role1", true, "Role 1 description"));
        roles.add(new RoleEntity(2L, "role2", true, "Role 2 description"));
        
        Mockito.when(roleRepository.findByIdIn(Mockito.any())).thenReturn(roles);
        
        List<RoleEntity> result = roleRepository.findByIdIn(List.of(1L, 2L));
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    // Add test cases for other methods
    
}