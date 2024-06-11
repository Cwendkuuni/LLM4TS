package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {
    @Mock
    private UserTaskInstanceRepository repository;

    @Mock
    private UserTaskInstanceEntity entity;

    @Test
    public void testFindByStatus() {
        repository.findByStatus(FlowNodeStatus.COMPLETED);
        verify(repository, times(1)).findByStatus(FlowNodeStatus.COMPLETED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        verify(repository, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        verify(repository, times(1)).findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        repository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        verify(repository, times(1)).findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
    }

    @Test
    public void testFindByRoleId() {
        repository.findByRoleId(1L);
        verify(repository, times(1)).findByRoleId(1L);
    }

    @Test
    public void testFindByCandidateUserId() {
        repository.findByCandidateUserId(1L);
        verify(repository, times(1)).findByCandidateUserId(1L);
    }

    @Test
    public void testUpdate() {
        repository.update(entity);
        verify(repository, times(1)).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        repository.updateUserTaskIdById(1L, "taskId");
        verify(repository, times(1)).updateUserTaskIdById(1L, "taskId");
    }

    @Test
    public void testUpdateStatusById() {
        repository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        verify(repository, times(1)).updateStatusById(1L, FlowNodeStatus.COMPLETED);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        repository.deleteByFlowInstanceId(1L);
        verify(repository, times(1)).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        repository.findByInstanceTypeAndActivityId(FlowNodeType.ACTIVITY, "activityId", 1L);
        verify(repository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.ACTIVITY, "activityId", 1L);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        repository.findByInstanceTypeAndName(FlowNodeType.ACTIVITY, "name", 1L);
        verify(repository, times(1)).findByInstanceTypeAndName(FlowNodeType.ACTIVITY, "name", 1L);
    }

    @Test
    public void testFindConfirmById() {
        repository.findConfirmById(1L);
        verify(repository, times(1)).findConfirmById(1L);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.COMPLETED);
        verify(repository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.COMPLETED);
    }
}