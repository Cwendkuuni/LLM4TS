package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert. assertFalse;
import static org.junit.Assert. assertNotNull;
import static org.junit.Assert. assertNull;
import static org.junit.Assert. assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {"com.oceanbase.odc.metadb.Application"})
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstance;

    @Before
    public void init() {
        userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setUserTaskId("taskId");
        userTaskInstance.setStatus(FlowNodeStatus.WAITING);
        userTaskInstance.setApproved(false);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(userTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAITING);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(userTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance, result.get(0));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(userTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Collections.singleton("roleIdentifier"));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance, result.get(0));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(userTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance, result.get(0));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(userTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance, result.get(0));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity updatedUserTaskInstance = new UserTaskInstanceEntity();
        updatedUserTaskInstance.setId(1L);
        updatedUserTaskInstance.setUserTaskId("updatedTaskId");
        updatedUserTaskInstance.setStatus(FlowNodeStatus.APPROVED);
        updatedUserTaskInstance.setApproved(true);
        updatedUserTaskInstance.setOperatorId("operatorId");
        updatedUserTaskInstance.setComment("comment");
        updatedUserTaskInstance.setExpireIntervalSeconds(1000L);
        updatedUserTaskInstance.setExternalFlowInstanceId("externalFlowInstanceId");

        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.ofNullable(userTaskInstance));
        int result = userTaskInstanceRepository.update(updatedUserTaskInstance);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.ofNullable(userTaskInstance));
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "updatedTaskId");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.ofNullable(userTaskInstance));
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(Arrays.asList(userTaskInstance));
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("taskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.WAITING);
        userTaskInstanceEntity.setApproved(false);
        userTaskInstanceEntity.setOperatorId("operatorId");
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(1000L);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L))
                .thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("taskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.WAITING);
        userTaskInstanceEntity.setApproved(false);
        userTaskInstanceEntity.setOperatorId("operatorId");
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(1000L);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L))
                .thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.ofNullable(userTaskInstance));
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(userTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstances);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.WAITING);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance, result.get(0));
    }
}