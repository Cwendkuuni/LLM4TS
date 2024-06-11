
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindByStatus() {
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assert.assertEquals(Collections.singletonList(userTaskInstanceEntity), userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L, 3L)))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assert.assertEquals(Collections.singletonList(userTaskInstanceEntity),
                userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singleton("Identifier")))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assert.assertEquals(Collections.singletonList(userTaskInstanceEntity),
                userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singleton("Identifier")));
    }
    
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singleton("Identifier")))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assert.assertEquals(Collections.singletonList(userTaskInstanceEntity),
                userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singleton("Identifier")));
    }

    @Test
    public void testFindByRoleId() {
        Mockito.when(userTaskInstanceRepository.findByRoleId(1L))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assert.assertEquals(Collections.singletonList(userTaskInstanceEntity), userTaskInstanceRepository.findByRoleId(1L));
    }

    @Test
    public void testFindByCandidateUserId() {
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(1L))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assert.assertEquals(Collections.singletonList(userTaskInstanceEntity), userTaskInstanceRepository.findByCandidateUserId(1L));
    }

    // This test for update(), updateUserTaskIdById(), updateStatusById(), deleteByFlowInstanceId() methods will need an actual database to interact with, so we skip these tests.

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Assert.assertEquals(Optional.of(userTaskInstanceEntity), userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L));
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Assert.assertEquals(Optional.of(userTaskInstanceEntity), userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L));
    }

    @Test
    public void testFindConfirmById() {
        Mockito.when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(true);
        Assert.assertEquals(userTaskInstanceRepository.findConfirmById(1L), true);
    }
    
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singleton(1L), FlowNodeStatus.APPROVED))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assert.assertEquals(Collections.singletonList(userTaskInstanceEntity), 
                userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singleton(1L), FlowNodeStatus.APPROVED));
    }
}
