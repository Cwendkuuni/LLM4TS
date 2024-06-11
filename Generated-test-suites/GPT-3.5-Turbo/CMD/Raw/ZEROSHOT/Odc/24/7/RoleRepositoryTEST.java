import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleList = new ArrayList<>();
        roleList.add(roleEntity);
        roleList.add(roleEntity);

        when(roleRepository.findByIdIn(anyCollection())).thenReturn(roleList);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleList = new ArrayList<>();
        roleList.add(roleEntity);

        when(roleRepository.findByName(anyString())).thenReturn(roleList);

        List<RoleEntity> result = roleRepository.findByName("Admin");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    // Add more test methods for other functions in RoleRepository

}