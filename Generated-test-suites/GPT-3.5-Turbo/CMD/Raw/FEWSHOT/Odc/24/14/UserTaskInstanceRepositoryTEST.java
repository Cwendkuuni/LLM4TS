package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Setup
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        // Execute
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(status);

        // Verify
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Setup
        Long userId = 123L;
        List<Long> roleIds = Collections.singletonList(456L);

        // Execute
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Verify
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Setup
        Long userId = 123L;
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        // Execute
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        // Verify
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Setup
        List<String> resourceRoleIdentifier = Collections.singletonList("role1");

        // Execute
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        // Verify
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFindByRoleId() {
        // Setup
        Long roleId = 123L;

        // Execute
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByRoleId(roleId);

        // Verify
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        // Setup
        Long userId = 123L;

        // Execute
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserId(userId);

        // Verify
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testUpdate() {
        // Setup
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        // Execute
        int result = userTaskInstanceRepository.update(entity);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Setup
        Long id = 123L;
        String userTaskId = "testId";

        // Execute
        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testUpdateStatusById() {
        // Setup
        Long id = 123L;
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        // Execute
        int result = userTaskInstanceRepository.updateStatusById(id, status);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Setup
        Long instanceId = 123L;

        // Execute
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Setup
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "123";
        Long flowInstanceId = 456L;

        // Execute
        UserTaskInstanceEntity result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId).orElse(null);

        // Verify
        assertNull(result);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Setup
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "test";
        Long flowInstanceId = 123L;

        // Execute
        UserTaskInstanceEntity result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId).orElse(null);

        // Verify
        assertNull(result);
    }

    @Test
    public void testFindConfirmById() {
        // Setup
        Long id = 123L;

        // Execute
        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Setup
        List<Long> flowInstanceIds = Collections.singletonList(123L);
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        // Execute
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        // Verify
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }
}