import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
        roleEntity.setName("test");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test description");
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), 1L);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleEntity);

        when(roleRepository.findByName(name)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName(name);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getName(), "test");
    }

    // Add test methods for other methods in RoleRepository

}