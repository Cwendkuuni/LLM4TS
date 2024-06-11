import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository repository;

    @Test
    public void findByIdInTest() {
        repository.findByIdIn(Arrays.asList(1L, 2L, 3L));
        verify(repository, times(1)).findByIdIn(any());
    }

    @Test
    public void findByNameTest() {
        repository.findByName("test");
        verify(repository, times(1)).findByName(anyString());
    }

    @Test
    public void findByOrganizationIdTest() {
        repository.findByOrganizationId(1L, PageRequest.of(0, 1));
        verify(repository, times(1)).findByOrganizationId(anyLong(), any());
    }

    @Test
    public void findByNameAndOrganizationIdTest() {
        repository.findByNameAndOrganizationId("test", 1L);
        verify(repository, times(1)).findByNameAndOrganizationId(anyString(), anyLong());
    }

    @Test
    public void findByOrganizationIdAndNameInTest() {
        repository.findByOrganizationIdAndNameIn(1L, Arrays.asList("test1", "test2"));
        verify(repository, times(1)).findByOrganizationIdAndNameIn(anyLong(), anyList());
    }

    // Add remaining test methods...

    @Test(expected = IllegalArgumentException.class)
    public void nullInputTest() {
        repository.findByName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyInputTest() {
        repository.findByIdIn(Arrays.asList());
    }
}