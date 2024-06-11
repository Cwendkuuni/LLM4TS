package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindByStatus() {
        userTaskInstanceRepository.findByStatus(FlowNodeStatus.INITIATED);
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.INITIATED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singleton(1L));
        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(1L, Collections.singleton(1L));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singleton("role1"));
        verify(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singleton("role1"));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singleton("role1"));
        verify(userTaskInstanceRepository).findByResourceRoleIdentifierIn(Collections.singleton("role1"));
    }

    @Test
    public void testFindByRoleId() {
        userTaskInstanceRepository.findByRoleId(1L);
        verify(userTaskInstanceRepository).findByRoleId(1L);
    }

    @Test
    public void testFindByCandidateUserId() {
        userTaskInstanceRepository.findByCandidateUserId(1L);
        verify(userTaskInstanceRepository).findByCandidateUserId(1L);
    }

    @Test
    public void testUpdate() {
        userTaskInstanceRepository.update(userTaskInstanceEntity);
        verify(userTaskInstanceRepository).update(userTaskInstanceEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        userTaskInstanceRepository.updateUserTaskIdById(1L, "task1");
        verify(userTaskInstanceRepository).updateUserTaskIdById(1L, "task1");
    }

    @Test
    public void testUpdateStatusById() {
        userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.INITIATED);
        verify(userTaskInstanceRepository).updateStatusById(1L, FlowNodeStatus.INITIATED);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        verify(userTaskInstanceRepository).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activity1", 1L);
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.START, "activity1", 1L);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name1", 1L);
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(FlowNodeType.START, "name1", 1L);
    }

    @Test
    public void testFindConfirmById() {
        userTaskInstanceRepository.findConfirmById(1L);
        verify(userTaskInstanceRepository).findConfirmById(1L);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singleton(1L), FlowNodeStatus.INITIATED);
        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singleton(1L), FlowNodeStatus.INITIATED);
    }
}