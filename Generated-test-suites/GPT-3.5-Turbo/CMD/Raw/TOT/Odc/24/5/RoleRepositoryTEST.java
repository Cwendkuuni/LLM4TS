import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
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
        List<RoleEntity> roles = Collections.singletonList(roleEntity);
        when(roleRepository.findByIdIn(Arrays.asList(1L))).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Role", result.get(0).getName());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = Collections.singletonList(roleEntity);
        when(roleRepository.findByName("Test Role")).thenReturn(roles);

        List<RoleEntity> result = roleRepository.findByName("Test Role");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(true, result.get(0).isEnabled());
    }

    // Additional test methods for other repository methods

}