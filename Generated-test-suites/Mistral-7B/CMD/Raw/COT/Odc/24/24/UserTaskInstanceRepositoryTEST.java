package com.oceanbase.odc.metadb.flow.test;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JpaTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @BeforeEach
    public void setUp() {
        // Initialize the test environment here if needed
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data
        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(status);
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance.getStatus(), actualUserTaskInstance.getStatus());
        }
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data
        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance.getUserId(), actualUserTaskInstance.getUserId());
            assertEquals(expectedUserTaskInstance.getRoleIds(), actualUserTaskInstance.getRoleIds());
        }
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data
        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance.getUserId(), actualUserTaskInstance.getUserId());
            assertEquals(expectedUserTaskInstance.getResourceRoleIdentifier(), actualUserTaskInstance.getResourceRoleIdentifier());
        }
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifier = new ArrayList<>();
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data
        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance.getResourceRoleIdentifier(), actualUserTaskInstance.getResourceRoleIdentifier());
        }
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data
        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance.getRoleId(), actualUserTaskInstance.getRoleId());
        }
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data
        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance.getUserId(), actualUserTaskInstance.getUserId());
        }
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        // Arrange: Prepare the test data

        // Act: Call the method
        int actualRowsAffected = userTaskInstanceRepository.update(entity);
        // Assert: Verify the result
        assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";
        // Arrange: Prepare the test data

        // Act: Call the method
        int actualRowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        // Assert: Verify the result
        assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        // Arrange: Prepare the test data

        // Act: Call the method
        int actualRowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
        // Assert: Verify the result
        assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        // Arrange: Prepare the test data

        // Act: Call the method
        int actualRowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        // Assert: Verify the result
        assertEquals(1, actualRowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> expected = Optional.of(new UserTaskInstanceEntity());
        // Arrange: Prepare the test data

        // Act: Call the method
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        // Assert: Verify the result
        assertEquals(expected.isPresent(), actual.isPresent());
        if (expected.isPresent()) {
            UserTaskInstanceEntity expectedUserTaskInstance = expected.get();
            UserTaskInstanceEntity actualUserTaskInstance = actual.get();
            assertEquals(expectedUserTaskInstance, actualUserTaskInstance);
        }
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "testName";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> expected = Optional.of(new UserTaskInstanceEntity());
        // Arrange: Prepare the test data

        // Act: Call the method
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        // Assert: Verify the result
        assertEquals(expected.isPresent(), actual.isPresent());
        if (expected.isPresent()) {
            UserTaskInstanceEntity expectedUserTaskInstance = expected.get();
            UserTaskInstanceEntity actualUserTaskInstance = actual.get();
            assertEquals(expectedUserTaskInstance, actualUserTaskInstance);
        }
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean expected = true;
        // Arrange: Prepare the test data

        // Act: Call the method
        Boolean actual = userTaskInstanceRepository.findConfirmById(id);
        // Assert: Verify the result
        assertEquals(expected, actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data

        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance.getFlowInstanceId(), actualUserTaskInstance.getFlowInstanceId());
            assertEquals(expectedUserTaskInstance.getStatus(), actualUserTaskInstance.getStatus());
        }
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        // Arrange: Prepare the test data

        // Act: Call the method
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findAll(PageRequest.of(0, 10));
        // Assert: Verify the result
        assertEquals(expected.size(), actual.size());
        for (UserTaskInstanceEntity expectedUserTaskInstance : expected) {
            UserTaskInstanceEntity actualUserTaskInstance = actual.get(actual.indexOf(expectedUserTaskInstance));
            assertEquals(expectedUserTaskInstance, actualUserTaskInstance);
        }
    }

    @AfterEach
    public void tearDown() {
        // Clean up the test environment here if needed
    }
}