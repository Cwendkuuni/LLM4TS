package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.any(FlowNodeStatus.class)))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds_emptyList() {
        List<UserTaskInstanceEntity> expectedList = Collections.emptyList();
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any()))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList());

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds_nonEmptyList() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any()))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn_emptyList() {
        List<UserTaskInstanceEntity> expectedList = Collections.emptyList();
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.any()))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList());

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn_nonEmptyList() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2);
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.any()))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("identifier1", "identifier2"));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByRoleId(Mockito.anyLong()))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Mockito.anyLong()))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> actualList = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Mockito.doNothing().when(userTaskInstanceRepository).update(entity);

        int result = userTaskInstanceRepository.update(entity);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";
        Mockito.doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        Mockito.doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

        int result = userTaskInstanceRepository.updateStatusById(id, status);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Mockito.doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId_nullResult() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.empty());

        Optional<UserTaskInstanceEntity> actualOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                instanceType, activityId, flowInstanceId);

        assertNull(actualOptional.orElse(null));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId_nonNullResult() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> actualOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                instanceType, activityId, flowInstanceId);

        assertEquals(userTaskInstanceEntity, actualOptional.orElse(null));
    }

    @Test
    public void testFindConfirmById_true() {
        Long id = 1L;
        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(true);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        assertTrue(result);
    }

    @Test
    public void testFindConfirmById_false() {
        Long id = 1L;
        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(false);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus_emptyList() {
        List<FlowNodeStatus> statuses = Arrays.asList(FlowNodeStatus.APPROVED);
        List<Long> flowInstanceIds = Arrays.asList(1L);
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Mockito.any(),
                Mockito.any()))
                .thenReturn(Collections.emptyList());

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, FlowNodeStatus.APPROVED);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus_nonEmptyList() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        List<FlowNodeStatus> statuses = Arrays.asList(FlowNodeStatus.APPROVED);
        List<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Mockito.any(),
                Mockito.any()))
                .thenReturn(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, FlowNodeStatus.APPROVED);

        assertEquals(Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2), result);
    }

}