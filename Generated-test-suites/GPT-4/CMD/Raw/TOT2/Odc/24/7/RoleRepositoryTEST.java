import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private RoleEntity roleEntity;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void findByIdInTest() {
        RoleEntity roleEntity = mock(RoleEntity.class);
        when(roleRepository.findByIdIn((Arrays.asList(roleEntity.getId())))).thenReturn(Arrays.asList(roleEntity));
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(roleEntity));
        assertEquals(1, result.size());
    }

    // Similarly write JUnit test cases for other methods
}