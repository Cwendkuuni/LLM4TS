package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeApprovalInstanceId;
import com.oceanbase.odc.service.flow.model.FlowNodeApprovalInstanceIdBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setUserTaskId("task1");
        userTaskInstance.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstance.setApproved(true);
        userTaskInstance.setOperatorId("operator1");
        userTaskInstance.setComment("comment1");
        userTaskInstance.setExpireIntervalSeconds(3600);
        userTaskInstance.setExternalFlowInstanceId("instance1");

        int rowsAffected = userTaskInstanceRepository.update(userTaskInstance);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "task1";

        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.REJECTED;

        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertNotNull(userTaskInstanceOptional);
        UserTaskInstanceEntity userTaskInstance = userTaskInstanceOptional.get();
        assertNotNull(userTaskInstance);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertNull(userTaskInstanceOptional);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        assertFalse(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByCandidateUserIdOrRoleIdsWithNullUserId() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Long userId = null;

        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByCandidateUserIdOrResourceRoleIdentifierWithNullUserId() {
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        Long userId = null;

        userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByCandidateUserIdOrRoleIdsWithEmptyRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Collections.emptyList();

        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByResourceRoleIdentifierInWithNullResourceRoleIdentifier() {
        Collection<String> resourceRoleIdentifier = null;

        userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByResourceRoleIdentifierInWithEmptyResourceRoleIdentifier() {
        Collection<String> resourceRoleIdentifier = Collections.emptyList();

        userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByRoleIdWithNullRoleId() {
        Long roleId = null;

        userTaskInstanceRepository.findByRoleId(roleId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByCandidateUserIdWithNullUserId() {
        Long userId = null;

        userTaskInstanceRepository.findByCandidateUserId(userId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullUserTaskInstance() {
        userTaskInstanceRepository.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserTaskIdByIdWithNullId() {
        String userTaskId = "task1";
        Long id = null;

        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserTaskIdByIdWithNullUserTaskId() {
        Long id = 1L;
        String userTaskId = null;

        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatusByIdWithNullId() {
        FlowNodeStatus status = FlowNodeStatus.REJECTED;
        Long id = null;

        userTaskInstanceRepository.updateStatusById(id, status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateStatusByIdWithNullStatus() {
        Long id = 1L;
        FlowNodeStatus status = null;

        userTaskInstanceRepository.updateStatusById(id, status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullInstanceId() {
        Long instanceId = null;

        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceTypeAndActivityIdWithNullInstanceType() {
        FlowNodeType instanceType = null;
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceTypeAndActivityIdWithNullActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = null;
        Long flowInstanceId = 1L;

        userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceTypeAndActivityIdWithNullFlowInstanceId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activity1";
        Long flowInstanceId = null;

        userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceTypeAndNameWithNullInstanceType() {
        FlowNodeType instanceType = null;
        String name = "name1";
        Long flowInstanceId = 1L;

        userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceTypeAndNameWithNullName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = null;
        Long flowInstanceId = 1L;

        userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceTypeAndNameWithNullFlowInstanceId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name1";
        Long flowInstanceId = null;

        userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByConfirmByIdWithNullId() {
        Long id = null;

        userTaskInstanceRepository.findConfirmById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusWithNullFlowInstanceIds() {
        Collection<Long> flowInstanceIds = null;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusWithNullStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = null;

        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }

    @Test
    public void testCreateUserTaskInstance() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        FlowNodeApprovalInstanceId userTaskInstanceId = new FlowNodeApprovalInstanceIdBuilder()
                .withInstanceId(1L)
                .withActivityId("activity1")
                .withUserId(1L)
                .build();
        userTaskInstance.setId(userTaskInstanceId.getId());
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setUserTaskId("task1");
        userTaskInstance.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstance.setApproved(true);
        userTaskInstance.setOperatorId("operator1");
        userTaskInstance.setComment("comment1");
        userTaskInstance.setExpireIntervalSeconds(3600);
        userTaskInstance.setExternalFlowInstanceId("instance1");

        UserTaskInstanceEntity savedUserTaskInstance = userTaskInstanceRepository.save(userTaskInstance);
        assertNotNull(savedUserTaskInstance);
        assertEquals(userTaskInstance, savedUserTaskInstance);
    }

}