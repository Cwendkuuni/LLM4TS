import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<UserTaskInstanceEntity> jpaSpecificationExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        when(jpaRepository.findByStatus(status)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(status);

        assertNotNull(resultList);
        assertEquals(expectedList, resultList);
        verify(jpaRepository, times(1)).findByStatus(status);
    }

    // Add test methods for other repository methods

}