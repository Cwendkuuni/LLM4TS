import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepo;

    @Before
    public void setup() {
        // Setup mock data for testing
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> mockRoles = new ArrayList<>();
        when(roleRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(mockRoles);

        List<RoleEntity> roles = roleRepo.findByIdIn(Collections.singletonList(1L));

        assertEquals(mockRoles, roles);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> mockRoles = new ArrayList<>();
        when(roleRepository.findByName("Admin")).thenReturn(mockRoles);

        List<RoleEntity> roles = roleRepo.findByName("Admin");

        assertEquals(mockRoles, roles);
    }

    // More test cases for other methods can be added here

}