import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private RoleEntity roleEntity;

    @Test
    public void findByIdInTest() {
        when(roleRepository.findByIdIn(any())).thenReturn(Arrays.asList(roleEntity));
        assertNotNull(roleRepository.findByIdIn(Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    public void findByNameTest() {
        when(roleRepository.findByName(any())).thenReturn(Arrays.asList(roleEntity));
        assertNotNull(roleRepository.findByName("Admin"));
    }

    @Test
    public void findByOrganizationIdTest() {
        when(roleRepository.findByOrganizationId(any(), any())).thenReturn(any());
        assertNotNull(roleRepository.findByOrganizationId(1L, any(Pageable.class)));
    }

    @Test
    public void findByNameAndOrganizationIdTest() {
        when(roleRepository.findByNameAndOrganizationId(any(), any())).thenReturn(Optional.of(roleEntity));
        assertTrue(roleRepository.findByNameAndOrganizationId("Admin", 1L).isPresent());
    }
    
    // Continue with the same pattern for the rest of the methods
    // Then add tests for edge cases and exceptions ...

    @Test(expected = IllegalArgumentException.class)
    public void findByIdIn_NullListTest() {
        roleRepository.findByIdIn(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void findByName_NullNameTest() {
        roleRepository.findByName(null);
    }
    
    // Continue with the same pattern for the rest of the methods ...
    
    @Test
    public void updateTest() {
        when(roleRepository.update(any())).thenReturn(1);
        assertEquals(1, roleRepository.update(roleEntity));
    }
}