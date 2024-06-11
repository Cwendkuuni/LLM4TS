import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Test
    public void testFindByStatus() {
        Collection<FlowNodeStatus> statusValues = new ArrayList<>();
        statusValues.add(FlowNodeStatus.PENDING);
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByStatus(any(FlowNodeStatus.class))).thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        Assertions.assertNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(expectedResult);
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(eq(userId), anyCollection()))
                .thenReturn(expectedResult);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("testRoleIdentifier");
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(expectedResult);
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(eq(userId), anyCollection()))
                .thenReturn(expectedResult);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("testRoleIdentifier");
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(expectedResult);
        when(userTaskInstanceRepositoryMock.findByResourceRoleIdentifierIn(anyCollection()))
                .thenReturn(expectedResult);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(expectedResult);
        when(userTaskInstanceRepositoryMock.findByRoleId(eq(roleId))).thenReturn(expectedResult);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(expectedResult);
        when(userTaskInstanceRepositoryMock.findByCandidateUserId(eq(userId))).thenReturn(expectedResult);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity entity = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepositoryMock.findById(id)).thenReturn(Optional.of(entity));
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepositoryMock.save(any(UserTaskInstanceEntity.class))).thenReturn(updatedEntity);
        int result = userTaskInstanceRepository.update(entity);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";
        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        int result = userTaskInstanceRepository.updateStatusById(id, status);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expectedResult = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(eq(instanceType), eq(activityId),
                eq(flowInstanceId))).thenReturn(Optional.of(expectedResult));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);
        Assertions.assertEquals(expectedResult, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "testName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expectedResult = mock(UserTaskInstanceEntity.class);
        when(userTaskInstanceRepositoryMock.findByInstanceTypeAndName(eq(instanceType), eq(name), eq(flowInstanceId)))
                .thenReturn(Optional.of(expectedResult));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        Assertions.assertEquals(expectedResult, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;
        when(userTaskInstanceRepositoryMock.findConfirmById(id)).thenReturn(confirm);
        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        Assertions.assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
        when(userTaskInstanceRepositoryMock.findAll()).thenReturn(expectedResult);
        when(userTaskInstanceRepositoryMock.findApprovalInstanceIdByFlowInstanceIdAndStatus(eq(flowInstanceIds), eq(status)))
                .thenReturn(expectedResult);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        Assertions.assertEquals(expectedResult, result);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}