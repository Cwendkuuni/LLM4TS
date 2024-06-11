package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.springframework.util.Assert.notNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

/**
 * Test cases for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2023-03-10 10:10
 * @since ODC_release_3.3.0
 */
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    // Test findByStatus
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.REJECTED, null),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.WAITING, null));

        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(2L, FlowNodeStatus.REJECTED, null),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.WAITING, null));

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAITING);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    // Test findByCandidateUserIdOrRoleIds
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, userId),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, userId),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.REJECTED, 2L),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, 1L),
                new UserTaskInstanceEntity(5L, FlowNodeStatus.WAITING, 2L));

        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, userId),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, userId),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, userId));

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    // Test findByCandidateUserIdOrResourceRoleIdentifier
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, userId, "role1"),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, userId, "role1"),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.REJECTED, 2L, "role2"),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, 1L, "role1"),
                new UserTaskInstanceEntity(5L, FlowNodeStatus.WAITING, 2L, "role2"));

        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, userId, "role1"),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, userId, "role1"),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, userId, "role1"));

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    // Test findByResourceRoleIdentifierIn
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, "role1"),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, null, "role1"),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.REJECTED, 2L, "role2"),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, 1L, "role1"),
                new UserTaskInstanceEntity(5L, FlowNodeStatus.WAITING, 2L, "role2"));

        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, "role1"),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, null, "role1"),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, 1L, "role1"));

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    // Test findByRoleId
    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, null, roleId),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, null, null, roleId),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.REJECTED, 2L, null, roleId),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, 1L, null, roleId),
                new UserTaskInstanceEntity(5L, FlowNodeStatus.WAITING, 2L, null, roleId + 1));

        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, null, null, roleId),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, null, null, roleId),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, 1L, null, roleId));

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    // Test findByCandidateUserId
    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, userId, null, null),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, userId, null, null),
                new UserTaskInstanceEntity(3L, FlowNodeStatus.REJECTED, 2L, null, null),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, 1L, null, null),
                new UserTaskInstanceEntity(5L, FlowNodeStatus.WAITING, 2L, null, null));

        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED, userId, null, null),
                new UserTaskInstanceEntity(2L, FlowNodeStatus.WAITING, userId, null, null),
                new UserTaskInstanceEntity(4L, FlowNodeStatus.APPROVED, userId, null, null));

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    // Test update
    @Test
    public void testUpdate() {
        Long id = 1L;
        Long userTaskId = 123L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        String operatorId = "operator";
        String comment = "comment";
        Long expireIntervalSeconds = 12345L;
        String externalFlowInstanceId = "externalInstanceId";

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(id, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        notNull(entity);

        int rowsAffected = userTaskInstanceRepository.update(new UserTaskInstanceEntity(id, userTaskId, status, operatorId, comment, expireIntervalSeconds, externalFlowInstanceId));

        assertEquals(1, rowsAffected);
    }

    // Test updateUserTaskIdById
    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, rowsAffected);
    }

    // Test updateStatusById
    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);

        assertEquals(1, rowsAffected);
    }

    // Test deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, rowsAffected);
    }

    // Test findByInstanceTypeAndActivityId
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity expectedUserTaskInstanceEntity = new UserTaskInstanceEntity(1L, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertTrue(optionalUserTaskInstanceEntity.isPresent());
        assertEquals(expectedUserTaskInstanceEntity, optionalUserTaskInstanceEntity.get());
    }

    // Test findByInstanceTypeAndName
    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String name = "name";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity expectedUserTaskInstanceEntity = new UserTaskInstanceEntity(1L, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        assertTrue(optionalUserTaskInstanceEntity.isPresent());
        assertEquals(expectedUserTaskInstanceEntity, optionalUserTaskInstanceEntity.get());
    }

    // Test findConfirmById
    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);

        assertNull(confirm);
    }

    // Test findApprovalInstanceIdByFlowInstanceIdAndStatus
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FlowNodeStatus.APPROVED, 1L),
                new UserTaskInstanceEntity(2L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FlowNodeStatus.REJECTED, 1L),
                new UserTaskInstanceEntity(3L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FlowNodeStatus.WAITING, 2L),
                new UserTaskInstanceEntity(4L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FlowNodeStatus.APPROVED, 2L),
                new UserTaskInstanceEntity(5L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FlowNodeStatus.REJECTED, 2L));

        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FlowNodeStatus.APPROVED, 1L),
                new UserTaskInstanceEntity(4L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FlowNodeStatus.APPROVED, 2L));

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }
}