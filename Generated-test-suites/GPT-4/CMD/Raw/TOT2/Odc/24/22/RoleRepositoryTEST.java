import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepositoryImpl roleRepository;

    @Mock
    private RoleEntity roleEntity1, roleEntity2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        RoleEntity entity1 = new RoleEntity();
        entity1.setId(1L);
        entity1.setName("Admin");

        RoleEntity entity2 = new RoleEntity();
        entity2.setId(2L);
        entity2.setName("User");

        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(entity1, entity2));
    }
    
    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    // similar test cases for the remaining methods
}