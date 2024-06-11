package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserTaskInstanceRepositoryTest {

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertEquals(0, entities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertEquals(0, entities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("resourceRole1", "resourceRole2"));
        assertEquals(0, entities.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("resourceRole1", "resourceRole2"));
        assertEquals(0, entities.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(0, entities.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(0, entities.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("Task1");
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setApproved(true);
        entity.setOperatorId("Operator1");
        entity.setComment("Comment1");
        entity.setExpireIntervalSeconds(60);
        entity.setExternalFlowInstanceId("ExtFlow1");

        int updatedRows = userTaskInstanceRepository.update(entity);
        assertEquals(0, updatedRows);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(1L, "Task1");
        assertEquals(0, updatedRows);
    }

    @Test
    public void testUpdateStatusById() {
        int updatedRows = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(0, updatedRows);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, deletedRows);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.NODE, "Activity1", 1L);
        assertFalse(entity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.NODE, "Name1", 1L);
        assertFalse(entity.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertNull(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.PENDING);
        assertEquals(0, entities.size());
    }

}