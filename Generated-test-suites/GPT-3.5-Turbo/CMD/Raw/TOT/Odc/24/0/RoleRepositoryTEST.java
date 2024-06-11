import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;
    
    @Before
    public void setUp() {
        roleRepository = mock(RoleRepository.class);
    }
    
    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "role1", true, "description1"));
        
        when(roleRepository.findByIdIn(ids)).thenReturn(roles);
        
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        
        assertEquals(1, result.size());
        assertEquals("role1", result.get(0).getName());
    }
    
    @Test
    public void testFindByName() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity(1L, "role1", true, "description1"));
        
        when(roleRepository.findByName("role1")).thenReturn(roles);
        
        List<RoleEntity> result = roleRepository.findByName("role1");
        
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    // Other test methods for each repository method
    
}