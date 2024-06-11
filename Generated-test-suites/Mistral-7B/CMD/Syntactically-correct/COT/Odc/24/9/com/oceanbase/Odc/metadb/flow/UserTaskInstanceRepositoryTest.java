package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstances);
        assertTrue(userTaskInstances.isEmpty());

        // Add some userTaskInstances to the database and test again
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceRepository.save(userTaskInstance1);

        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstanceRepository.save(userTaskInstance2);

        userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
        assertEquals(userTaskInstance1.getId(), userTaskInstances.get(0).getId());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity().setUserId(1L).setRoleIds(Arrays.asList(1L, 2L)),
                new UserTaskInstanceEntity().setUserId(2L).setRoleIds(Collections.singletonList(1L)),
                new UserTaskInstanceEntity().setUserId(3L).setRoleIds(Collections.emptyList()));

        userTaskInstanceRepository.saveAll(userTaskInstances);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(userTaskInstances.get(0)));
        assertTrue(result.contains(userTaskInstances.get(1)));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity().setResourceRoleIdentifier("role1"),
                new UserTaskInstanceEntity().setResourceRoleIdentifier("role2"),
                new UserTaskInstanceEntity().setResourceRoleIdentifier("role1"));

        userTaskInstanceRepository.saveAll(userTaskInstances);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(1))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByResourceRoleIdentifierWithNullCollection() {
        userTaskInstanceRepository.findByResourceRoleIdentifierIn(null);
    }

    @Test
    public void testFindByRoleId() {
        // Given
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity().setRoleId(1L),
                new UserTaskInstanceEntity().setRoleId(2L),
                new UserTaskInstanceEntity().setRoleId(1L));

        userTaskInstanceRepository.saveAll(userTaskInstances);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(1))));
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity().setUserId(1L),
                new UserTaskInstanceEntity().setUserId(2L),
                new UserTaskInstanceEntity().setUserId(1L));

        userTaskInstanceRepository.saveAll(userTaskInstances);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(userTaskInstances.get(0), userTaskInstances.get(1))));
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setUserTaskId("userTaskId1");
        userTaskInstance.setStatus(FlowNodeStatus.PENDING);

        // When
        int rowsAffected = userTaskInstanceRepository.update(userTaskInstance);

        // Then
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(1L).get();
        assertEquals("userTaskId1", updatedUserTaskInstance.getUserTaskId());
        assertEquals(FlowNodeStatus.PENDING, updatedUserTaskInstance.getStatus());

        // Given
        userTaskInstance.setUserTaskId("userTaskId2");
        userTaskInstance.setStatus(FlowNodeStatus.APPROVED);

        // When
        rowsAffected = userTaskInstanceRepository.update(userTaskInstance);

        // Then
        assertEquals(1, rowsAffected);

        updatedUserTaskInstance = userTaskInstanceRepository.findById(1L).get();
        assertEquals("userTaskId2", updatedUserTaskInstance.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, updatedUserTaskInstance.getStatus());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        long id = 1L;
        String userTaskId = "userTaskId1";

        // When
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Then
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(id).get();
        assertEquals(userTaskId, userTaskInstance.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        // When
        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);

        // Then
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(id).get();
        assertEquals(status, userTaskInstance.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        long instanceId = 1L;
        int expectedNumberOfUserTaskInstances = 3;

        // When
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        for (int i = 0; i < expectedNumberOfUserTaskInstances; i++) {
            userTaskInstances.add(new UserTaskInstanceEntity().setFlowInstanceId(instanceId));
            userTaskInstanceRepository.save(userTaskInstances.get(i));
        }

        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(expectedNumberOfUserTaskInstances, rowsAffected);

        List<UserTaskInstanceEntity> remainingUserTaskInstances = userTaskInstanceRepository.findAll();
        assertEquals(0, remainingUserTaskInstances.size());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity()
                .setInstanceType(FlowNodeType.ACTIVITY)
                .setActivityId("activityId1")
                .setFlowInstanceId(1L);

        userTaskInstanceRepository.save(userTaskInstance);

        // When
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.ACTIVITY, "activityId1", 1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(userTaskInstance, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity()
                .setInstanceType(FlowNodeType.DECISION)
                .setName("name1")
                .setFlowInstanceId(1L);

        userTaskInstanceRepository.save(userTaskInstance);

        // When
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.DECISION, "name1", 1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(userTaskInstance, result.get());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        long id = 1L;
        boolean expectedConfirm = true;

        // When
        boolean result = userTaskInstanceRepository.findConfirmById(id);

        // Then
        assertEquals(expectedConfirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(
                new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceId).setStatus(FlowNodeStatus.APPROVED),
                new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceId).setStatus(FlowNodeStatus.REJECTED),
                new UserTaskInstanceEntity().setFlowInstanceId(flowInstanceId).setStatus(FlowNodeStatus.PENDING));

        userTaskInstanceRepository.saveAll(userTaskInstances);

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.APPROVED);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(userTaskInstances.get(0)));
    }

}