package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;

public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.COMPLETED;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(status);
        assertNotNull(userTaskInstanceEntities);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(userTaskInstanceEntities);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("RoleA", "RoleB");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(userTaskInstanceEntities);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = Arrays.asList("RoleA", "RoleB");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertNotNull(userTaskInstanceEntities);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
        assertNotNull(userTaskInstanceEntities);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertNotNull(userTaskInstanceEntities);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "Task1";
        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.COMPLETED;
        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "Activity1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertTrue(optionalUserTaskInstanceEntity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "Task1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertTrue(optionalUserTaskInstanceEntity.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertNotNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);
        FlowNodeStatus status = FlowNodeStatus.COMPLETED;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertNotNull(userTaskInstanceEntities);
    }
}
