import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleRepository roleRepo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleList = new ArrayList<>();
        when(roleRepository.findByIdIn(anyList())).thenReturn(roleList);
        List<RoleEntity> result = roleRepo.findByIdIn(Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertEquals(roleList, result);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleList = new ArrayList<>();
        when(roleRepository.findByName(anyString())).thenReturn(roleList);
        List<RoleEntity> result = roleRepo.findByName("Test name");
        assertNotNull(result);
        assertEquals(roleList, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> rolePage = mock(Page.class);
        when(roleRepository.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(rolePage);
        Page<RoleEntity> result = roleRepo.findByOrganizationId(1L, mock(Pageable.class));
        assertNotNull(result);
        assertEquals(rolePage, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleOpt = Optional.of(new RoleEntity());
        when(roleRepository.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(roleOpt);
        Optional<RoleEntity> result = roleRepo.findByNameAndOrganizationId("Test name", 1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
    }

    // Add more test methods for other repository methods

}