package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> expected = Arrays.asList(
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        UserTaskInstanceEntity entity1 = expected.get(0);
        UserTaskInstanceEntity entity2 = expected.get(1);
        UserTaskInstanceEntity entity3 = expected.get(2);

        // save entities

        UserTaskInstanceRepository.saveAll(expected);

        result = UserTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(expected));

        // test empty list

        result = UserTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertTrue(result.isEmpty());

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> expected = Arrays.asList(
                new UserTaskInstanceEntity().setUserId(userId), new UserTaskInstanceEntity().setRoleId(roleIds.get(0)),
                new UserTaskInstanceEntity().setRoleId(roleIds.get(1)));

        // save entities

        UserTaskInstanceRepository.saveAll(expected);

        result = UserTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected.get(0), expected.get(1))));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        assertTrue(result.isEmpty());

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> expected = Arrays.asList(
                new UserTaskInstanceEntity().setUserId(userId).setResourceRoleIdentifier("role1"),
                new UserTaskInstanceEntity().setUserId(userId).setResourceRoleIdentifier("role2"));

        // save entities

        UserTaskInstanceRepository.saveAll(expected);

        result = UserTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected.get(0), expected.get(1))));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertTrue(result.isEmpty());

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> expected = Arrays.asList(
                new UserTaskInstanceEntity().setResourceRoleIdentifier("role1"),
                new UserTaskInstanceEntity().setResourceRoleIdentifier("role1"),
                new UserTaskInstanceEntity().setResourceRoleIdentifier("role2"));

        // save entities

        UserTaskInstanceRepository.saveAll(expected);

        result = UserTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected.get(0), expected.get(1))));
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByRoleId(roleId);
        assertTrue(result.isEmpty());

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity().setRoleId(roleId),
                new UserTaskInstanceEntity().setRoleId(roleId));

        // save entities

        UserTaskInstanceRepository.saveAll(expected);

        result = UserTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(expected));
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByCandidateUserId(userId);
        assertTrue(result.isEmpty());

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity().setUserId(userId));

        // save entities

        UserTaskInstanceRepository.saveAll(expected);

        result = UserTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(1, result.size());
        assertEquals(expected.get(0), result.get(0));
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        int result = UserTaskInstanceRepository.update(entity);
        assertEquals(0, result);

        // add an entity to the database and test again

        UserTaskInstanceRepository.save(entity);

        entity.setUserTaskId("test-user-task-id");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId("test-operator-id");
        entity.setComment("test-comment");
        entity.setExpireIntervalSeconds(123);
        entity.setExternalFlowInstanceId("test-external-flow-instance-id");

        result = UserTaskInstanceRepository.update(entity);
        assertEquals(1, result);

        // check the updated entity in the database

        UserTaskInstanceEntity updatedEntity = UserTaskInstanceRepository.findById(id).orElseThrow();
        assertEquals(entity.getUserTaskId(), updatedEntity.getUserTaskId());
        assertEquals(entity.getStatus(), updatedEntity.getStatus());
        assertEquals(entity.isApproved(), updatedEntity.isApproved());
        assertEquals(entity.getOperatorId(), updatedEntity.getOperatorId());
        assertEquals(entity.getComment(), updatedEntity.getComment());
        assertEquals(entity.getExpireIntervalSeconds(), updatedEntity.getExpireIntervalSeconds());
        assertEquals(entity.getExternalFlowInstanceId(), updatedEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "test-user-task-id";

        int result = UserTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(0, result);

        // add an entity to the database and test again

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceRepository.save(entity);

        result = UserTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);

        // check the updated entity in the database

        UserTaskInstanceEntity updatedEntity = UserTaskInstanceRepository.findById(id).orElseThrow();
        assertEquals(userTaskId, updatedEntity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int result = UserTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(0, result);

        // add an entity to the database and test again

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceRepository.save(entity);

        result = UserTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);

        // check the updated entity in the database

        UserTaskInstanceEntity updatedEntity = UserTaskInstanceRepository.findById(id).orElseThrow();
        assertEquals(status, updatedEntity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = UserTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(0, result);

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        UserTaskInstanceRepository.saveAll(entities);

        result = UserTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(2, result);

        // check if the deleted entities are gone

        List<UserTaskInstanceEntity> resultEntities = UserTaskInstanceRepository.findAll();
        assertFalse(resultEntities.containsAll(entities));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);
        assertFalse(result.isPresent());

        // add a matching entity to the database and test again

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity().setInstanceType(instanceType).setActivityId(activityId)
                .setFlowInstanceId(flowInstanceId);
        UserTaskInstanceRepository.save(entity);

        result = UserTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String name = "activity1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertFalse(result.isPresent());

        // add a matching entity to the database and test again

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity().setInstanceType(instanceType).setName(name)
                .setFlowInstanceId(flowInstanceId);
        UserTaskInstanceRepository.save(entity);

        result = UserTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        Boolean result = UserTaskInstanceRepository.findConfirmById(id);
        assertFalse(result != null);

        // add an entity to the database and test again

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity().setConfirm(confirm);
        UserTaskInstanceRepository.save(entity);

        result = UserTaskInstanceRepository.findConfirmById(id);
        assertTrue(result != null);
        assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> result = UserTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);
        assertTrue(result.isEmpty());

        // add some entities to the database and test again

        List<UserTaskInstanceEntity> entities = Arrays.asList(new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceIds.get(0))
                .setStatus(status), new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceIds.get(1))
                .setStatus(FlowNodeStatus.REJECTED));
        UserTaskInstanceRepository.saveAll(entities);

        result = UserTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertEquals(1, result.size());
        assertEquals(entities.get(0), result.get(0));
    }
}