package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role1"));
        assertNotNull(result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("role1"));
        assertNotNull(result);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("task1");
        entity.setStatus(FlowNodeStatus.COMPLETED);
        int rowsUpdated = userTaskInstanceRepository.update(entity);
        assertEquals(1, rowsUpdated);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int rowsUpdated = userTaskInstanceRepository.updateUserTaskIdById(1L, "task1");
        assertEquals(1, rowsUpdated);
    }

    @Test
    public void testUpdateStatusById() {
        int rowsUpdated = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(1, rowsUpdated);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsDeleted = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, rowsDeleted);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activity1", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TASK, "name1", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.PENDING);
        assertNotNull(result);
    }
}