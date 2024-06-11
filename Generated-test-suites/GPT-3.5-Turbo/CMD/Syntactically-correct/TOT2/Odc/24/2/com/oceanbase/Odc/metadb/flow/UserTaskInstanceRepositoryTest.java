package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role_identifier"));
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("role_identifier"));
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(0, result.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(0, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
        assertEquals(0, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.PENDING);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        assertTrue(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TYPE, "activityId", 1L).isEmpty());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        assertTrue(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TYPE, "name", 1L).isEmpty());
    }

    @Test
    public void testFindConfirmById() {
        assertFalse(userTaskInstanceRepository.findConfirmById(1L));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.PENDING);
        assertEquals(0, result.size());
    }

}