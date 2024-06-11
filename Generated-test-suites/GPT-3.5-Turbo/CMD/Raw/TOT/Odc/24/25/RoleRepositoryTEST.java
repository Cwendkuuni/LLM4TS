import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(roleRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());
        List<RoleEntity> roles = roleService.findByIdIn(ids);
        assertTrue(roles.isEmpty());
    }

    @Test
    public void testFindByName() {
        String name = "Admin";

        when(roleRepository.findByName(name)).thenReturn(Collections.singletonList(new RoleEntity()));
        List<RoleEntity> roles = roleService.findByName(name);
        assertEquals(1, roles.size());
    }

    // Add more test cases for other methods...

}