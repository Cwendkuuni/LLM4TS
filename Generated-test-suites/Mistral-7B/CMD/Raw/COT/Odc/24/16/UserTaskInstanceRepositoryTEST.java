package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    // Test method: findByStatus
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntityBuilder().build());
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(expected, actual);
    }

    // Test method: findByCandidateUserIdOrRoleIds
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = new ArrayList<>();
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntityBuilder().build());
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(expected, actual);
    }

    // Test method: findByCandidateUserIdOrResourceRoleIdentifier
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntityBuilder().build());
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        assertEquals(expected, actual);
    }

    // Test method: findByResourceRoleIdentifierIn
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntityBuilder().build(),
                new UserTaskInstanceEntityBuilder().build());
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(expected, actual);
    }

    // Test method: findByRoleId
    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntityBuilder().build());
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(expected, actual);
    }

    // Test method: findByCandidateUserId
    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntityBuilder().build());
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(expected, actual);
    }

    // Test method: update
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntityBuilder().build();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntityBuilder()
                .withId(entity.getId())
                .withUserTaskId("newUserTaskId")
                .withStatus(FlowNodeStatus.APPROVED)
                .withApproved(true)
                .withOperatorId(1L)
                .withComment("newComment")
                .withExpireIntervalSeconds(10L)
                .withExternalFlowInstanceId("newExternalFlowInstanceId")
                .build();
        int actual = userTaskInstanceRepository.update(updatedEntity);
        assertEquals(1, actual);
    }

    // Test method: updateUserTaskIdById
    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "newUserTaskId";
        int actual = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, actual);
    }

    // Test method: updateStatusById
    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        int actual = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, actual);
    }

    // Test method: deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int actual = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, actual);
    }

    // Test method: findByInstanceTypeAndActivityId
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activity1";
        Optional<UserTaskInstanceEntity> expected = Optional.of(new UserTaskInstanceEntityBuilder().build());
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);
        assertTrue(actual.isPresent());
        assertEquals(expected.get(), actual.get());
    }

    // Test method: findByInstanceTypeAndName
    @Test
    public void testFindByInstanceTypeAndName() {
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name1";
        Optional<UserTaskInstanceEntity> expected = Optional.of(new UserTaskInstanceEntityBuilder().build());
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertTrue(actual.isPresent());
        assertEquals(expected.get(), actual.get());
    }

    // Test method: findConfirmById
    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean expected = true;
        Boolean actual = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(expected, actual);
    }

    // Test method: findApprovalInstanceIdByFlowInstanceIdAndStatus
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntityBuilder().build(),
                new UserTaskInstanceEntityBuilder().build());
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceId, status);
        assertEquals(expected, actual);
    }

    // Edge cases and exception handling tests
    // Test method: findByStatus with null status
    @Test(expected = NullPointerException.class)
    public void testFindByStatusWithNullStatus() {
        userTaskInstanceRepository.findByStatus(null);
    }

    // Test method: findByCandidateUserIdOrRoleIds with null userId or empty roleIds
    @Test
    public void testFindByCandidateUserIdOrRoleIdsWithNullOrEmpty() {
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Collections.emptyList());
        assertNull(actual);
    }

    // Test method: findByCandidateUserIdOrResourceRoleIdentifier with null userId or empty resourceRoleIdentifiers
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifierWithNullOrEmpty() {
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null,
                Collections.emptyList());
        assertNull(actual);
    }

    // Test method: findByResourceRoleIdentifierIn with empty resourceRoleIdentifier list
    @Test
    public void testFindByResourceRoleIdentifierInWithEmptyList() {
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.emptyList());
        assertEquals(Collections.emptyList(), actual);
    }

    // Test method: findByRoleId with null roleId
    @Test(expected = NullPointerException.class)
    public void testFindByRoleIdWithNullRoleId() {
        userTaskInstanceRepository.findByRoleId(null);
    }

    // Test method: findByCandidateUserId with null userId
    @Test(expected = NullPointerException.class)
    public void testFindByCandidateUserIdWithNullUserId() {
        userTaskInstanceRepository.findByCandidateUserId(null);
    }

    // Test method: update with null entity
    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullEntity() {
        userTaskInstanceRepository.update(null);
    }

    // Test method: updateUserTaskIdById with null id or empty userTaskId
    @Test
    public void testUpdateUserTaskIdByIdWithNullOrEmpty() {
        Long id = null;
        String userTaskId = "";
        int actual = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(0, actual);
    }

    // Test method: updateStatusById with null id or null status
    @Test(expected = NullPointerException.class)
    public void testUpdateStatusByIdWithNullOrNull() {
        Long id = null;
        FlowNodeStatus status = null;
        int actual = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(0, actual);
    }

    // Test method: deleteByFlowInstanceId with null instanceId
    @Test(expected = NullPointerException.class)
    public void testDeleteByFlowInstanceIdWithNullInstanceId() {
        userTaskInstanceRepository.deleteByFlowInstanceId(null);
    }

    // Test method: findByInstanceTypeAndActivityId with null instanceType or activityId or flowInstanceId
    @Test
    public void testFindByInstanceTypeAndActivityIdWithNullOrEmpty() {
        FlowNodeType instanceType = null;
        String activityId = "";
        Long flowInstanceId = null;
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);
        assertFalse(actual.isPresent());
    }

    // Test method: findByInstanceTypeAndName with null instanceType or name or flowInstanceId
    @Test
    public void testFindByInstanceTypeAndNameWithNullOrEmpty() {
        FlowNodeType instanceType = null;
        String name = "";
        Long flowInstanceId = null;
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertFalse(actual.isPresent());
    }

    // Test method: findConfirmById with null id
    @Test(expected = NullPointerException.class)
    public void testFindConfirmByIdWithNullId() {
        userTaskInstanceRepository.findConfirmById(null);
    }

}