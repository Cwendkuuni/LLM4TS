import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setUp() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("UT-1");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceEntity.setApproved(false);
        userTaskInstanceEntity.setOperatorId(1L);
        userTaskInstanceEntity.setComment("Test comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(60);
        userTaskInstanceEntity.setExternalFlowInstanceId("Ext-1");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        assertEquals(1, resultList.size());
        assertEquals(userTaskInstanceEntity, resultList.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedList = new ArrayList<>();
        expectedList.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L))).thenReturn(expectedList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

        assertEquals(1, resultList.size());
        assertEquals(userTaskInstanceEntity, resultList.get(0));
    }

    // Additional test methods for other repository methods can be added here
  
}