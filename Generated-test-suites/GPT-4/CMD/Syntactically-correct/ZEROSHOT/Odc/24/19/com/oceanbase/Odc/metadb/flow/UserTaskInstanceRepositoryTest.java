
package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository repository;

    final Long id = 100L;
    final FlowNodeStatus status = FlowNodeStatus.FAILED;
    final Collection<Long> roleIds = new ArrayList<>();
    final Long userId = 200L;
    final Collection<String> resourceRoleIdentifiers = new ArrayList<>();
    final String userTaskId = "testUserTaskId";
    final FlowNodeType instanceType = FlowNodeType.ACTIVITY;
    final String activityId = "testActivityId";
    final Long flowInstanceId = 300L;
    final String name = "testName";

    @Test
    public void testFindByStatus() {
        when(repository.findByStatus(status)).thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> resultList = repository.findByStatus(status);
        verify(repository).findByStatus(status);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        when(repository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> resultList = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        verify(repository).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        when(repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers)).thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> resultList = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        verify(repository).findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        when(repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> resultList = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        verify(repository).findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
    }

    @Test
    public void testFindByCandidateUserId() {
        when(repository.findByCandidateUserId(userId)).thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> resultList = repository.findByCandidateUserId(userId);
        verify(repository).findByCandidateUserId(userId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        when(repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId)).thenReturn(Optional.empty());
        Optional<UserTaskInstanceEntity> resultOpt = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        verify(repository).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        when(repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId)).thenReturn(Optional.empty());
        Optional<UserTaskInstanceEntity> resultOpt = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        verify(repository).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test
    public void testFindConfirmById() {
        when(repository.findConfirmById(id)).thenReturn(Boolean.FALSE);
        Boolean resultBool = repository.findConfirmById(id);
        verify(repository).findConfirmById(id);
        assertEquals(resultBool, Boolean.FALSE);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(400L);
        flowInstanceIds.add(500L);
        when(repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status)).thenReturn(new ArrayList<>());
        List<UserTaskInstanceEntity> resultList = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        verify(repository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }
}
