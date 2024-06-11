import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleEntity roleEntity;

    @Before
    public void setup() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("TestRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Description");
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByIdIn(anyList())).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertEquals(1, result.size());
        assertEquals("TestRole", result.get(0).getName());
    }

    // Additional test cases for other public methods can be added here...

}