import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING),
                new UserTaskInstanceEntity(2L, 1L, FlowNodeStatus.APPROVED),
                new UserTaskInstanceEntity(3L, 1L, FlowNodeStatus.REJECTED),
                new UserTaskInstanceEntity(4L, 1L, FlowNodeStatus.PENDING)
        );

        when(userTaskInstanceRepository.findByStatus(eq(FlowNodeStatus.PENDING))).thenReturn(userTaskInstances.subList(0, 2));

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.PENDING);

        assertEquals(2, result.size());
        for (UserTaskInstanceEntity entity : result) {
            assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
        }
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, userId),
                new UserTaskInstanceEntity(2L, 1L, FlowNodeStatus.APPROVED, userId),
                new UserTaskInstanceEntity(3L, 1L, FlowNodeStatus.REJECTED, 2L),
                new UserTaskInstanceEntity(4L, 1L, FlowNodeStatus.PENDING, 1L),
                new UserTaskInstanceEntity(5L, 1L, FlowNodeStatus.PENDING, 2L)
        );

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(eq(userId), anyCollection()))
                .thenReturn(userTaskInstances.subList(0, 3));

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(3, result.size());
        for (UserTaskInstanceEntity entity : result) {
            assertEquals(userId, entity.getUserId());
            assertTrue(roleIds.contains(entity.getRoleId()));
        }
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, userId, "role1"),
                new UserTaskInstanceEntity(2L, 1L, FlowNodeStatus.APPROVED, userId, "role1"),
                new UserTaskInstanceEntity(3L, 1L, FlowNodeStatus.REJECTED, 2L, "role2"),
                new UserTaskInstanceEntity(4L, 1L, FlowNodeStatus.PENDING, userId, "role1"),
                new UserTaskInstanceEntity(5L, 1L, FlowNodeStatus.PENDING, 2L, "role2")
        );

        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(eq(userId), anyCollection()))
                .thenReturn(userTaskInstances.subList(0, 3));

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);

        assertEquals(3, result.size());
        for (UserTaskInstanceEntity entity : result) {
            assertEquals(userId, entity.getUserId());
            assertTrue(resourceRoleIdentifiers.contains(entity.getResourceRoleIdentifier()));
        }
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, null, "role1"),
                new UserTaskInstanceEntity(2L, 1L, FlowNodeStatus.APPROVED, null, "role2"),
                new UserTaskInstanceEntity(3L, 1L, FlowNodeStatus.REJECTED, 2L, null),
                new UserTaskInstanceEntity(4L, 1L, FlowNodeStatus.PENDING, null, "role1"),
                new UserTaskInstanceEntity(5L, 1L, FlowNodeStatus.PENDING, null, "role2")
        );

        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(anyCollection()))
                .thenReturn(userTaskInstances.subList(0, 3));

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        assertEquals(3, result.size());
        for (UserTaskInstanceEntity entity : result) {
            assertTrue(resourceRoleIdentifier.contains(entity.getResourceRoleIdentifier()));
        }
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, null, null, 1L),
                new UserTaskInstanceEntity(2L, 1L, FlowNodeStatus.APPROVED, null, null, 2L),
                new UserTaskInstanceEntity(3L, 1L, FlowNodeStatus.REJECTED, 2L, null, 1L),
                new UserTaskInstanceEntity(4L, 1L, FlowNodeStatus.PENDING, null, null, 1L),
                new UserTaskInstanceEntity(5L, 1L, FlowNodeStatus.PENDING, null, null, 2L)
        );

        when(userTaskInstanceRepository.findByRoleId(eq(roleId))).thenReturn(userTaskInstances.subList(0, 2));

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByRoleId(roleId);

        assertEquals(2, result.size());
        for (UserTaskInstanceEntity entity : result) {
            assertEquals(roleId, entity.getRoleId());
        }
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, userId, null, 1L),
                new UserTaskInstanceEntity(2L, 1L, FlowNodeStatus.APPROVED, userId, null, 2L),
                new UserTaskInstanceEntity(3L, 1L, FlowNodeStatus.REJECTED, 2L, null, 1L),
                new UserTaskInstanceEntity(4L, 1L, FlowNodeStatus.PENDING, userId, null, 1L),
                new UserTaskInstanceEntity(5L, 1L, FlowNodeStatus.PENDING, 2L, null, 2L)
        );

        when(userTaskInstanceRepository.findByCandidateUserId(eq(userId))).thenReturn(userTaskInstances.subList(0, 3));

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserId(userId);

        assertEquals(3, result.size());
        for (UserTaskInstanceEntity entity : result) {
            assertEquals(userId, entity.getUserId());
        }
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(id, 1L, FlowNodeStatus.PENDING, 1L, null, null);

        doNothing().when(userTaskInstanceRepository).update(entity);

        userTaskInstanceService.update(entity);

        verify(userTaskInstanceRepository).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "task1";

        doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        userTaskInstanceService.updateUserTaskIdById(id, userTaskId);

        verify(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

        userTaskInstanceService.updateStatusById(id, status);

        verify(userTaskInstanceRepository).updateStatusById(id, status);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        userTaskInstanceService.deleteByFlowInstanceId(instanceId);

        verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.SEQUENTIAL;
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, 1L, null, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(eq(instanceType), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.SEQUENTIAL;
        String name = "name1";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, 1L, null, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(eq(instanceType), eq(name), eq(flowInstanceId)))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        when(userTaskInstanceRepository.findConfirmById(eq(id))).thenReturn(true);

        boolean result = userTaskInstanceService.findConfirmById(id);

        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, FlowNodeStatus.PENDING, 1L, null, null),
                new UserTaskInstanceEntity(2L, 1L, FlowNodeStatus.APPROVED, 1L, null, null),
                new UserTaskInstanceEntity(3L, 2L, FlowNodeStatus.REJECTED, 2L, null, null),
                new UserTaskInstanceEntity(4L, 1L, FlowNodeStatus.PENDING, 1L, null, null),
                new UserTaskInstanceEntity(5L, 1L, FlowNodeStatus.PENDING, 1L, null, null)
        );

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(anyCollection(), eq(status)))
                .thenReturn(Collections.singletonList(userTaskInstances.get(0)));

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                status);

        assertEquals(1, result.size());
        assertEquals(userTaskInstances.get(0), result.get(0));
    }
}