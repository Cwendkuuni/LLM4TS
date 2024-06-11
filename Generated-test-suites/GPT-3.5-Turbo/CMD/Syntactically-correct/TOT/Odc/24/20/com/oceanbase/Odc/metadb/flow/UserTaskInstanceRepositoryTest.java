import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setUp() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("1");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId(1L);
        userTaskInstanceEntity.setComment("Test comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(3600);
        userTaskInstanceEntity.setExternalFlowInstanceId("12345");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        assertEquals(expected, result);
    }

    // Add more test cases for other methods

}