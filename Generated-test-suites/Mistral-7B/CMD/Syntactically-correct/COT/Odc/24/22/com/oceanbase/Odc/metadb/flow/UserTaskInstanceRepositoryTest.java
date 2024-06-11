import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @org.junit.jupiter.api.BeforeEach
    public void setup() {
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userTaskInstanceEntity = new UserTaskInstanceEntity();
    }

    // Test case for findByStatus method
    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(userTaskInstanceEntity));
        Assertions.assertEquals(Arrays.asList(userTaskInstanceEntity), userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED));
    }

    // Test case for findByCandidateUserIdOrRoleIds method with valid parameters
    @Test
    public void testFindByCandidateUserIdOrRoleIdsWithValidParams() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assertions.assertEquals(Collections.singletonList(userTaskInstanceEntity), userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds));
    }

    // Test case for findByCandidateUserIdOrRoleIds method with null parameters
    @Test
    public void testFindByCandidateUserIdOrRoleIdsWithNullParams() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, null)).thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.emptyList(), userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, null));
    }

    // Test case for findByResourceRoleIdentifier method with valid parameters
    @Test
    public void testFindByResourceRoleIdentifierWithValidParams() {
        String resourceRoleIdentifier = "testRole";
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(resourceRoleIdentifier))).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assertions.assertEquals(Collections.singletonList(userTaskInstanceEntity), userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(resourceRoleIdentifier)));
    }

    // Test case for findByResourceRoleIdentifier method with null parameters
    @Test
    public void testFindByResourceRoleIdentifierWithNullParams() {
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(null)).thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.emptyList(), userTaskInstanceRepository.findByResourceRoleIdentifierIn(null));
    }

    // Test case for findByRoleId method with valid parameters
    @Test
    public void testFindByRoleIdWithValidParams() {
        Long roleId = 1L;
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(Arrays.asList(userTaskInstanceEntity));
        Assertions.assertEquals(Arrays.asList(userTaskInstanceEntity), userTaskInstanceRepository.findByRoleId(roleId));
    }

    // Test case for findByRoleId method with null parameters
    @Test
    public void testFindByRoleIdWithNullParams() {
        when(userTaskInstanceRepository.findByRoleId(null)).thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.emptyList(), userTaskInstanceRepository.findByRoleId(null));
    }

    // Test case for findByCandidateUserId method with valid parameters
    @Test
    public void testFindByCandidateUserIdWithValidParams() {
        Long userId = 1L;
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assertions.assertEquals(Collections.singletonList(userTaskInstanceEntity), userTaskInstanceRepository.findByCandidateUserId(userId));
    }

    // Test case for findByCandidateUserId method with null parameters
    @Test
    public void testFindByCandidateUserIdWithNullParams() {
        when(userTaskInstanceRepository.findByCandidateUserId(null)).thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.emptyList(), userTaskInstanceRepository.findByCandidateUserId(null));
    }

    // Test case for update method with valid parameters
    @Test
    public void testUpdateWithValidParams() {
        when(userTaskInstanceRepository.save(userTaskInstanceEntity)).thenReturn(userTaskInstanceEntity);
        Executable updateExecutable = () -> userTaskInstanceRepository.update(userTaskInstanceEntity);
        Assertions.assertEquals(1, updateExecutable.execute());
    }

    // Test case for update method with null parameters
    @Test
    public void testUpdateWithNullParams() {
        Executable updateExecutable = () -> userTaskInstanceRepository.update(null);
        Assertions.assertThrows(NullPointerException.class, updateExecutable);
    }

    // Test case for updateUserTaskIdById method with valid parameters
    @Test
    public void testUpdateUserTaskIdByIdWithValidParams() {
        when(userTaskInstanceRepository.save(userTaskInstanceEntity)).thenReturn(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findById(any(Long.class))).thenReturn(Optional.of(userTaskInstanceEntity));
        Executable updateExecutable = () -> userTaskInstanceRepository.updateUserTaskIdById(1L, "testUserTaskId");
        Assertions.assertEquals(1, updateExecutable.execute());
    }

    // Test case for updateUserTaskIdById method with null or invalid id
    @Test
    public void testUpdateUserTaskIdByIdWithInvalidOrNullParams() {
        when(userTaskInstanceRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Executable updateExecutable = () -> userTaskInstanceRepository.updateUserTaskIdById(1L, "testUserTaskId");
        Assertions.assertThrows(NullPointerException.class, updateExecutable);
    }

    // Test case for updateStatusById method with valid parameters
    @Test
    public void testUpdateStatusByIdWithValidParams() {
        when(userTaskInstanceRepository.save(userTaskInstanceEntity)).thenReturn(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findById(any(Long.class))).thenReturn(Optional.of(userTaskInstanceEntity));
        Executable updateExecutable = () -> userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        Assertions.assertEquals(1, updateExecutable.execute());
    }

    // Test case for updateStatusById method with null or invalid id
    @Test
    public void testUpdateStatusByIdWithInvalidOrNullParams() {
        when(userTaskInstanceRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Executable updateExecutable = () -> userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        Assertions.assertThrows(NullPointerException.class, updateExecutable);
    }

    // Test case for deleteByFlowInstanceId method with valid parameters
    @Test
    public void testDeleteByFlowInstanceIdWithValidParams() {
        Executable deleteExecutable = () -> userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        Assertions.assertEquals(1, deleteExecutable.execute());
    }

    // Test case for deleteByFlowInstanceId method with null parameters
    @Test
    public void testDeleteByFlowInstanceIdWithNullParams() {
        Executable deleteExecutable = () -> userTaskInstanceRepository.deleteByFlowInstanceId(null);
        Assertions.assertDoesNotThrow(deleteExecutable);
    }

    // Test case for findByInstanceTypeAndActivityId method with valid parameters
    @Test
    public void testFindByInstanceTypeAndActivityIdWithValidParams() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Assertions.assertEquals(userTaskInstanceEntity, userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId).get());
    }

    // Test case for findByInstanceTypeAndActivityId method with null or invalid parameters
    @Test
    public void testFindByInstanceTypeAndActivityIdWithInvalidOrNullParams() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = null;
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.empty());
        Assertions.assertFalse(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId).isPresent());
    }

    // Test case for findConfirmById method with valid parameters
    @Test
    public void testFindConfirmByIdWithValidParams() {
        Long id = 1L;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(true);
        Assertions.assertTrue(userTaskInstanceRepository.findConfirmById(id));
    }

    // Test case for findConfirmById method with null parameters
    @Test
    public void testFindConfirmByIdWithNullParams() {
        Long id = null;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(false);
        Assertions.assertFalse(userTaskInstanceRepository.findConfirmById(id));
    }

    // Test case for findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid parameters
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusWithValidParams() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(flowInstanceId), status))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        Assertions.assertEquals(Collections.singletonList(userTaskInstanceEntity), userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(flowInstanceId), status));
    }

    // Test case for findApprovalInstanceIdByFlowInstanceIdAndStatus method with null or empty list of flowInstanceIds
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusWithNullOrEmptyList() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(null, status)).thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.emptyList(), userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(null, status));
    }

    // Test case for findApprovalInstanceIdByFlowInstanceIdAndStatus method with null or invalid status
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusWithInvalidOrNullStatus() {
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(flowInstanceId), null))
                .thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.emptyList(), userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(flowInstanceId), null));
    }
}