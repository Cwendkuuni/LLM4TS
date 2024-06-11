package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByStatus(status);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
        verify(userTaskInstanceRepository, times(1)).findByStatus(status);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
        verify(userTaskInstanceRepository, times(1))
                .findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers))
                .thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
        verify(userTaskInstanceRepository, times(1)).findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
        verify(userTaskInstanceRepository, times(1)).findByRoleId(roleId);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserId(userId);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(userId);
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(entity));

        userTaskInstanceRepository.update(updatedEntity);

        verify(userTaskInstanceRepository, times(1)).update(updatedEntity);
        verify(userTaskInstanceRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        userTaskInstanceRepository.updateStatusById(id, status);

        verify(userTaskInstanceRepository, times(1)).updateStatusById(id, status);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String activityId = "activityId";
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(expectedUserTaskInstance));

        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Long flowInstanceId = 1L;
        String name = "name";
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(expectedUserTaskInstance));

        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);

        assertEquals(confirm, actualConfirm);
        verify(userTaskInstanceRepository, times(1)).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedUserTaskInstances = new ArrayList<>();
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
                .thenReturn(expectedUserTaskInstances);

        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }
}