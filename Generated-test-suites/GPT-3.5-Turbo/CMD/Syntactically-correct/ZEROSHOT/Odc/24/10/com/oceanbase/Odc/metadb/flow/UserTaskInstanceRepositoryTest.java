import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        // Initialize necessary setup before each test case
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class))).thenReturn(userTaskInstances);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), anyCollection())).thenReturn(userTaskInstances);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // Add more test cases for the remaining methods in UserTaskInstanceRepository

}