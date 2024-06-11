package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        List<UserTaskInstanceEntity> userTaskInstances = repository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstances);
        assertEquals(0, userTaskInstances.size());

        userTaskInstances = repository.findByStatus(FlowNodeStatus.PENDING);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.size() > 0);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);

        List<UserTaskInstanceEntity> userTaskInstances = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.size() > 0);

        assertEquals(userId, userTaskInstances.get(0).getCandidate().getUserId());
        assertEquals(inList(roleIds), userTaskInstances.stream().map(UserTaskInstanceEntity::getCandidate).map(Candidate::getRoleIds).toList());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");

        List<UserTaskInstanceEntity> userTaskInstances = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.size() > 0);

        assertEquals(userId, userTaskInstances.get(0).getCandidate().getUserId());
        assertEquals(inList(resourceRoleIdentifiers), userTaskInstances.stream().map(UserTaskInstanceEntity::getCandidate).map(Candidate::getResourceRoleIdentifier).toList());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");

        List<UserTaskInstanceEntity> userTaskInstances = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.size() > 0);

        assertEquals(inList(resourceRoleIdentifiers), userTaskInstances.stream().map(UserTaskInstanceEntity::getCandidate).map(Candidate::getResourceRoleIdentifier).toList());
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long roleId = 1L;

        List<UserTaskInstanceEntity> userTaskInstances = repository.findByRoleId(roleId);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.size() > 0);

        assertEquals(roleId, userTaskInstances.get(0).getRole().getId());
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long userId = 1L;

        List<UserTaskInstanceEntity> userTaskInstances = repository.findByCandidateUserId(userId);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.size() > 0);

        assertEquals(userId, userTaskInstances.get(0).getCandidate().getUserId());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);

        Long newUserTaskId = 2L;
        FlowNodeStatus newStatus = FlowNodeStatus.APPROVED;
        Boolean approved = true;
        Long operatorId = 3L;
        String comment = "Comment";
        Long expireIntervalSeconds = 10L;
        String externalFlowInstanceId = "externalFlowInstanceId";

        int updatedRows = invokeMethod(UserTaskInstanceRepository.class, "update", repository, entity, newUserTaskId, newStatus, approved, operatorId, comment, expireIntervalSeconds, externalFlowInstanceId);
        assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long id = 1L;
        String newUserTaskId = "newUserTaskId";

        int updatedRows = invokeMethod(UserTaskInstanceRepository.class, "updateUserTaskIdById", repository, id, newUserTaskId);
        assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long id = 1L;
        FlowNodeStatus newStatus = FlowNodeStatus.APPROVED;

        int updatedRows = invokeMethod(UserTaskInstanceRepository.class, "updateStatusById", repository, id, newStatus);
        assertEquals(1, updatedRows);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long instanceId = 1L;

        int deletedRows = invokeMethod(UserTaskInstanceRepository.class, "deleteByFlowInstanceId", repository, instanceId);
        assertEquals(1, deletedRows);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = invokeMethod(UserTaskInstanceRepository.class, "findByInstanceTypeAndActivityId", repository, instanceType, activityId, flowInstanceId);
        assertTrue(userTaskInstanceEntityOptional.isPresent());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        assertEquals(instanceType, userTaskInstanceEntity.getNode().getInstanceType());
        assertEquals(activityId, userTaskInstanceEntity.getNode().getActivityId());
        assertEquals(flowInstanceId, userTaskInstanceEntity.getInstanceId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "name";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = invokeMethod(UserTaskInstanceRepository.class, "findByInstanceTypeAndName", repository, instanceType, name, flowInstanceId);
        assertTrue(userTaskInstanceEntityOptional.isPresent());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        assertEquals(instanceType, userTaskInstanceEntity.getNode().getInstanceType());
        assertEquals(name, userTaskInstanceEntity.getNode().getName());
        assertEquals(flowInstanceId, userTaskInstanceEntity.getInstanceId());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Long id = 1L;

        Boolean confirm = invokeMethod(UserTaskInstanceRepository.class, "findConfirmById", repository, id);
        assertFalse(confirm);

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(id);
        userTaskInstanceEntity.setWaitForConfirm(true);
        invokeMethod(UserTaskInstanceRepository.class, "save", repository, userTaskInstanceEntity);

        confirm = invokeMethod(UserTaskInstanceRepository.class, "findConfirmById", repository, id);
        assertTrue(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        List<UserTaskInstanceEntity> userTaskInstances = invokeMethod(UserTaskInstanceRepository.class, "findApprovalInstanceIdByFlowInstanceIdAndStatus", repository, flowInstanceIds, status);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.size() > 0);

        for (UserTaskInstanceEntity userTaskInstance : userTaskInstances) {
            assertEquals(status, userTaskInstance.getStatus());
            assertEquals(inList(flowInstanceIds), userTaskInstance.getFlowInstanceIds());
        }
    }
}