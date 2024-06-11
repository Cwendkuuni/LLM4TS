import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        roleEntity = new RoleEntity(1L, "TestRole", true, "Test Role Description");
    }

    @Test
    public void testFindByIdIn() {
        Mockito.when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(1, roles.size());
        assertEquals(roleEntity.getName(), roles.get(0).getName());
    }

    @Test
    public void testFindByName() {
        Mockito.when(roleRepository.findByName("TestRole")).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> roles = roleRepository.findByName("TestRole");
        assertEquals(1, roles.size());
        assertEquals(roleEntity.getName(), roles.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, Pageable.unpaged());
        assertNotNull(page);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Mockito.when(roleRepository.findByNameAndOrganizationId("TestRole", 1L)).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> optionalRole = roleRepository.findByNameAndOrganizationId("TestRole", 1L);
        assertTrue(optionalRole.isPresent());
        assertEquals(roleEntity.getName(), optionalRole.get().getName());
    }

    // Add more test methods for other repository methods...

}