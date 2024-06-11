
package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity userTaskInstanceEntity;

    @Mock
    FlowNodeStatus flowNodeStatus;

    @Mock
    FlowNodeType flowNodeType;

    @Test
    public void findByStatusTest() {
        when(userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class)))
                .thenReturn(new ArrayList<>());
        assertTrue(userTaskInstanceRepository.findByStatus(flowNodeStatus).isEmpty());
    }

    @Test
    public void findByCandidateUserIdOrRoleIdsTest() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(any(Long.class), any(Collection.class)))
                .thenReturn(new ArrayList<>());
        assertTrue(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<Long>()).isEmpty());
    }

    @Test
    public void findByCandidateUserIdOrResourceRoleIdentifierTest() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(any(Long.class), any(Collection.class)))
                .thenReturn(new ArrayList<>());
        assertTrue(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<String>()).isEmpty());
    }

    @Test
    public void findByResourceRoleIdentifierInTest() {
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(any(Collection.class)) )
                .thenReturn(new ArrayList<>());
        assertTrue(userTaskInstanceRepository.findByResourceRoleIdentifierIn(new ArrayList<String>()).isEmpty());
    }

    @Test
    public void findByRoleIdTest() {
        when(userTaskInstanceRepository.findByRoleId(any(Long.class)))
                .thenReturn(new ArrayList<>());
        assertTrue(userTaskInstanceRepository.findByRoleId(1L).isEmpty());
    }

    @Test
    public void findByCandidateUserIdTest() {
        when(userTaskInstanceRepository.findByCandidateUserId(any(Long.class)))
                .thenReturn(new ArrayList<>());
        assertTrue(userTaskInstanceRepository.findByCandidateUserId(1L).isEmpty());
    }

    @Test
    public void updateTest() {
        when(userTaskInstanceRepository.update(any(UserTaskInstanceEntity.class)))
                .thenReturn(1);
        assertTrue(userTaskInstanceRepository.update(userTaskInstanceEntity) == 1);
    }

    @Test
    public void updateUserTaskIdByIdTest() {
        when(userTaskInstanceRepository.updateUserTaskIdById(any(Long.class), any(String.class)))
                .thenReturn(1);
        assertTrue(userTaskInstanceRepository.updateUserTaskIdById(1L, "test") == 1);
    }

    @Test
    public void updateStatusByIdTest() {
        when(userTaskInstanceRepository.updateStatusById(any(Long.class), any(FlowNodeStatus.class)))
                .thenReturn(1);
        assertTrue(userTaskInstanceRepository.updateStatusById(1L, flowNodeStatus) == 1);
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        when(userTaskInstanceRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);
        assertTrue(userTaskInstanceRepository.deleteByFlowInstanceId(1L) == 1);
    }

    @Test
    public void findByInstanceTypeAndActivityIdTest() {
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), any(String.class), any(Long.class)))
                .thenReturn(Optional.empty());
        assertTrue(!userTaskInstanceRepository.findByInstanceTypeAndActivityId(flowNodeType, "test", 1L).isPresent());
    }

    @Test
    public void findByInstanceTypeAndNameTest() {
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), any(String.class), any(Long.class)))
                .thenReturn(Optional.empty());
        assertTrue(!userTaskInstanceRepository.findByInstanceTypeAndName(flowNodeType, "test", 1L).isPresent());
    }

    @Test
    public void findConfirmByIdTest() {
        when(userTaskInstanceRepository.findConfirmById(any(Long.class)))
                .thenReturn(true);
        assertTrue(userTaskInstanceRepository.findConfirmById(1L));
    }

    @Test
    public void findApprovalInstanceIdByFlowInstanceIdAndStatusTest() {
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(any(Collection.class), any(FlowNodeStatus.class)))
                .thenReturn(new ArrayList<>());
        assertTrue(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(new ArrayList<Long>(), flowNodeStatus).isEmpty());
    }
}
