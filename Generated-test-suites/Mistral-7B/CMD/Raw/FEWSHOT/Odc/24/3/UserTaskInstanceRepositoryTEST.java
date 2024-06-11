package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository repository;

    @Before
    public void setUp() {
        // Initialize the repository
        repository = new UserTaskInstanceRepositoryImpl();
    }

    @Test
    public void testFindByStatus() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().withStatus(FlowNodeStatus.APPROVED).build(),
                new UserTaskInstanceEntityBuilder().withStatus(FlowNodeStatus.REJECTED).build(),
                new UserTaskInstanceEntityBuilder().withStatus(FlowNodeStatus.APPROVED).build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().withCandidateUserId(userId).build(),
                new UserTaskInstanceEntityBuilder().withRoleId(1L).build(),
                new UserTaskInstanceEntityBuilder().withRoleId(2L).build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Given
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().withCandidateUserId(userId).build(),
                new UserTaskInstanceEntityBuilder().withResourceRoleIdentifier("role1").build(),
                new UserTaskInstanceEntityBuilder().withResourceRoleIdentifier("role2").build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Given
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().withResourceRoleIdentifier("role1").build(),
                new UserTaskInstanceEntityBuilder().withResourceRoleIdentifier("role2").build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByRoleId() {
        // Given
        Long roleId = 1L;

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().withRoleId(roleId).build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findByRoleId(roleId);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        Long userId = 1L;

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().withCandidateUserId(userId).build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findByCandidateUserId(userId);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntityBuilder().build();
        Long id = entity.getId();
        String userTaskId = "testUserTaskId";
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        Boolean approved = true;
        Long operatorId = 1L;
        String comment = "Test comment";
        LocalDateTime expireIntervalSeconds = LocalDateTime.now().plusDays(1);
        String externalFlowInstanceId = "testExternalFlowInstanceId";

        // When
        int rowsAffected = repository.update(entity);

        // Then
        assertTrue(rowsAffected > 0);

        // Given (update entity values)
        entity.setUserTaskId(userTaskId);
        entity.setStatus(status);
        entity.setApproved(approved);
        entity.setOperatorId(operatorId);
        entity.setComment(comment);
        entity.setExpireIntervalSeconds(expireIntervalSeconds);
        entity.setExternalFlowInstanceId(externalFlowInstanceId);

        // When
        rowsAffected = repository.update(entity);

        // Then
        assertTrue(rowsAffected > 0);

        // Given (retrieve updated entity from the database)
        Optional<UserTaskInstanceEntity> updatedEntityOptional = repository.findById(id);

        // Then
        assertTrue(updatedEntityOptional.isPresent());
        UserTaskInstanceEntity updatedEntity = updatedEntityOptional.get();

        // Then (verify updated values)
        assertEquals(userTaskId, updatedEntity.getUserTaskId());
        assertEquals(status, updatedEntity.getStatus());
        assertEquals(approved, updatedEntity.isApproved());
        assertEquals(operatorId, updatedEntity.getOperatorId());
        assertEquals(comment, updatedEntity.getComment());
        assertEquals(expireIntervalSeconds, updatedEntity.getExpireIntervalSeconds());
        assertEquals(externalFlowInstanceId, updatedEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        Long id = 1L;
        String userTaskId = "testUserTaskId";

        // When
        int rowsAffected = repository.updateUserTaskIdById(id, userTaskId);

        // Then
        assertTrue(rowsAffected > 0);

        // Given (retrieve updated entity from the database)
        Optional<UserTaskInstanceEntity> updatedEntityOptional = repository.findById(id);

        // Then
        assertTrue(updatedEntityOptional.isPresent());
        UserTaskInstanceEntity updatedEntity = updatedEntityOptional.get();

        // Then (verify updated values)
        assertEquals(userTaskId, updatedEntity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        // When
        int rowsAffected = repository.updateStatusById(id, status);

        // Then
        assertTrue(rowsAffected > 0);

        // Given (retrieve updated entity from the database)
        Optional<UserTaskInstanceEntity> updatedEntityOptional = repository.findById(id);

        // Then
        assertTrue(updatedEntityOptional.isPresent());
        UserTaskInstanceEntity updatedEntity = updatedEntityOptional.get();

        // Then (verify updated values)
        assertEquals(status, updatedEntity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;

        // When
        int rowsAffected = repository.deleteByFlowInstanceId(instanceId);

        // Then
        assertTrue(rowsAffected > 0);

        // Given (retrieve deleted entity from the database)
        Optional<UserTaskInstanceEntity> deletedEntityOptional = repository.findById(1L);

        // Then
        assertFalse(deletedEntityOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntityBuilder().withInstanceType(instanceType)
                .withActivityId(activityId).withFlowInstanceId(flowInstanceId).build();

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstanceOptional = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        // Then
        assertTrue(actualUserTaskInstanceOptional.isPresent());
        UserTaskInstanceEntity actualUserTaskInstance = actualUserTaskInstanceOptional.get();

        // Then (verify values)
        assertEquals(expectedUserTaskInstance.getInstanceType(), actualUserTaskInstance.getInstanceType());
        assertEquals(expectedUserTaskInstance.getActivityId(), actualUserTaskInstance.getActivityId());
        assertEquals(expectedUserTaskInstance.getFlowInstanceId(), actualUserTaskInstance.getFlowInstanceId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "name";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntityBuilder().withInstanceType(instanceType)
                .withName(name).withFlowInstanceId(flowInstanceId).build();

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstanceOptional = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        // Then
        assertTrue(actualUserTaskInstanceOptional.isPresent());
        UserTaskInstanceEntity actualUserTaskInstance = actualUserTaskInstanceOptional.get();

        // Then (verify values)
        assertEquals(expectedUserTaskInstance.getInstanceType(), actualUserTaskInstance.getInstanceType());
        assertEquals(expectedUserTaskInstance.getName(), actualUserTaskInstance.getName());
        assertEquals(expectedUserTaskInstance.getFlowInstanceId(), actualUserTaskInstance.getFlowInstanceId());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;

        // When
        Boolean confirm = repository.findConfirmById(id);

        // Then
        assertNull(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().withStatus(status).withFlowInstanceId(flowInstanceId).build(),
                new UserTaskInstanceEntityBuilder().withStatus(status).withFlowInstanceId(flowInstanceId).build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status);

        // Then
        assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindAll() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(
                new UserTaskInstanceEntityBuilder().build(),
                new UserTaskInstanceEntityBuilder().build()
        );

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));

        // Then
        assertEquals(expectedUserTaskInstances.size(), actualUserTaskInstances.size());
        for (int i = 0; i < expectedUserTaskInstances.size(); i++) {
            assertEquals(expectedUserTaskInstances.get(i), actualUserTaskInstances.get(i));
        }
    }

    @Test
    public void testFindAllWithFilter() {
        // Given
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntityBuilder().build();
        List<UserTaskInstanceEntity> expectedUserTaskInstances = Collections.singletonList(userTaskInstance);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = repository.findAll(UserTaskInstanceEntitySpecifications.hasUserTaskId("testUserTaskId"));

        // Then
        assertEquals(expectedUserTaskInstances.size(), actualUserTaskInstances.size());
        assertEquals(expectedUserTaskInstances.get(0), actualUserTaskInstances.get(0));
    }
}