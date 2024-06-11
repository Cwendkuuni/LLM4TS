package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    private List<UserTaskInstanceEntity> userTaskInstanceList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userTaskInstanceList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED))
                .thenReturn(userTaskInstanceList);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> expectedUserTasks = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds))
                .thenReturn(expectedUserTasks);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> expectedUserTasks = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers))
                .thenReturn(expectedUserTasks);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expectedUserTasks = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByRoleId(roleId))
                .thenReturn(expectedUserTasks);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByRoleId(roleId);

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByRoleId(roleId);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> expectedUserTasks = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByCandidateUserId(userId))
                .thenReturn(expectedUserTasks);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserId(userId);

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserId(userId);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        Long userId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        boolean approved = true;
        Long operatorId = 1L;
        String comment = "test comment";
        int expireIntervalSeconds = 1;
        String userTaskId = "testUserTaskId";
        Long externalFlowInstanceId = 1L;

        when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(entity));

        int result = userTaskInstanceService.update(entity, userId, status, approved, operatorId, comment, expireIntervalSeconds, userTaskId, externalFlowInstanceId);

        verify(userTaskInstanceRepository).findById(id);
        assertTrue(result > 0);
        verify(userTaskInstanceRepository).save(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";

        int result = userTaskInstanceService.updateUserTaskIdById(id, userTaskId);

        verify(userTaskInstanceRepository).findById(id);
        assertTrue(result > 0);
        verify(userTaskInstanceRepository).save(any(UserTaskInstanceEntity.class));
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int result = userTaskInstanceService.updateStatusById(id, status);

        verify(userTaskInstanceRepository).findById(id);
        assertTrue(result > 0);
        verify(userTaskInstanceRepository).save(any(UserTaskInstanceEntity.class));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = userTaskInstanceService.deleteByFlowInstanceId(instanceId);

        verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
        assertTrue(result > 0);
    }

    @Test
    public void testFindUserTaskInstanceByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.TEST;
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expectedUserTask = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(expectedUserTask));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertEquals(expectedUserTask, result.orElse(null));
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testFindUserTaskInstanceByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.TEST;
        String name = "testName";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expectedUserTask = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(expectedUserTask));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        assertEquals(expectedUserTask, result.orElse(null));
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean result = userTaskInstanceService.findConfirmById(id);

        assertEquals(confirm, result);
        verify(userTaskInstanceRepository).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedUserTasks = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
                .thenReturn(expectedUserTasks);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }

    @Test
    public void testFindByFlowInstanceIdAndName() {
        Long flowInstanceId = 1L;
        String name = "testName";
        UserTaskInstanceEntity expectedUserTask = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByFlowInstanceIdAndName(flowInstanceId, name))
                .thenReturn(Optional.of(expectedUserTask));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByFlowInstanceIdAndName(flowInstanceId, name);

        assertEquals(expectedUserTask, result.orElse(null));
        verify(userTaskInstanceRepository).findByFlowInstanceIdAndName(flowInstanceId, name);
    }

    @Test
    public void testFindByFlowInstanceIdAndUserTaskId() {
        Long flowInstanceId = 1L;
        String userTaskId = "testUserTaskId";
        UserTaskInstanceEntity expectedUserTask = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByFlowInstanceIdAndUserTaskId(flowInstanceId, userTaskId))
                .thenReturn(Optional.of(expectedUserTask));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByFlowInstanceIdAndUserTaskId(flowInstanceId, userTaskId);

        assertEquals(expectedUserTask, result.orElse(null));
        verify(userTaskInstanceRepository).findByFlowInstanceIdAndUserTaskId(flowInstanceId, userTaskId);
    }

    @Test
    public void testFindByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedUserTasks = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(flowInstanceId, status))
                .thenReturn(expectedUserTasks);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByFlowInstanceIdAndStatus(flowInstanceId, status);

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByFlowInstanceIdAndStatus(flowInstanceId, status);
    }

    @Test
    public void testFindByUserTaskId() {
        String userTaskId = "testUserTaskId";
        UserTaskInstanceEntity expectedUserTask = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByUserTaskId(userTaskId))
                .thenReturn(Optional.of(expectedUserTask));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findByUserTaskId(userTaskId);

        assertEquals(expectedUserTask, result.orElse(null));
        verify(userTaskInstanceRepository).findByUserTaskId(userTaskId);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        UserTaskInstanceEntity expectedUserTask = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findById(id))
                .thenReturn(Optional.of(expectedUserTask));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceService.findById(id);

        assertEquals(expectedUserTask, result.orElse(null));
        verify(userTaskInstanceRepository).findById(id);
    }
}