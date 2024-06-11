import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        roles.add(role1);
        roles.add(role2);

        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByName() {
        RoleEntity role = new RoleEntity();
        role.setName("Admin");

        when(roleRepository.findByName("Admin")).thenReturn(Arrays.asList(role));

        List<RoleEntity> result = roleRepository.findByName("Admin");
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    // Additional test cases for other methods can be added here

}