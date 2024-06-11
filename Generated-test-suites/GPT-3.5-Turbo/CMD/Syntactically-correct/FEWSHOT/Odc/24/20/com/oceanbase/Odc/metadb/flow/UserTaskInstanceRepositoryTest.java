package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L, 3L));
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertNotNull(result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
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
        UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
        Mockito.when(entity.getId()).thenReturn(1L);
        int result = userTaskInstanceRepository.update(entity);
        assertNotNull(result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
        assertNotNull(result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertNotNull(result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TYPE, "activityId", 1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TYPE, "name", 1L);
        assertNotNull(result);
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.APPROVED);
        assertNotNull(result);
    }
}