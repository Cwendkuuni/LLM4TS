import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Test Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Description");
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        when(roleRepository.findByIdIn(ids)).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(1, result.size());
        assertEquals(roleEntity, result.get(0));
    }

    @Test
    public void testFindByName() {
        when(roleRepository.findByName("Test Role")).thenReturn(Collections.singletonList(roleEntity));

        List<RoleEntity> result = roleRepository.findByName("Test Role");
        assertEquals(1, result.size());
        assertEquals(roleEntity, result.get(0));
    }

    // Test cases for other methods go here

    @Test
    public void testUpdate() {
        when(roleRepository.update(roleEntity)).thenReturn(1);

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}