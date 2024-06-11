import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity(1L, "TestRole", true, "Description");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = Arrays.asList(roleEntity);
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L));
        assertNotNull(result);
        assertEquals(roles.size(), result.size());
        assertEquals(roles.get(0), result.get(0));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = Arrays.asList(roleEntity);
        List<RoleEntity> result = roleRepository.findByName("TestRole");
        assertNotNull(result);
        assertEquals(roles.size(), result.size());
        assertEquals(roles.get(0), result.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertNotNull(result);
        assertTrue(result.hasContent());
    }

    // Add more test methods for other query methods in RoleRepository

}