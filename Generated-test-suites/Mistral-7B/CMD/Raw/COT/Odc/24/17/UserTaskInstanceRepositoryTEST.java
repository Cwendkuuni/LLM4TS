package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

/**
 * Test class for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2023-03-13 17:52
 * @since ODC_release_3.3.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { UserTaskInstanceRepositoryTestConfig.class })
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    /**
     * Test method for {@link UserTaskInstanceRepository#findByStatus(FlowNodeStatus)}
     */
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstanceEntities);
        assertEquals(1, userTaskInstanceEntities.size());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrRoleIds(Long, Collection)}
     */
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));
        assertNotNull(userTaskInstanceEntities);
        assertEquals(2, userTaskInstanceEntities.size());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrResourceRoleIdentifier(Long, Collection)}
     */
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));
        assertNotNull(userTaskInstanceEntities);
        assertEquals(2, userTaskInstanceEntities.size());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByResourceRoleIdentifierIn(Collection)}
     */
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("role1", "role2"));
        assertNotNull(userTaskInstanceEntities);
        assertEquals(2, userTaskInstanceEntities.size());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByRoleId(Long)}
     */
    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(userTaskInstanceEntities);
        assertEquals(1, userTaskInstanceEntities.size());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserId(Long)}
     */
    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(userTaskInstanceEntities);
        assertEquals(1, userTaskInstanceEntities.size());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#update(UserTaskInstanceEntity)}
     */
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        UserTaskInstanceEntity updatedUserTaskInstanceEntity = new UserTaskInstanceEntity();
        updatedUserTaskInstanceEntity.setUserTaskId("newUserTaskId");
        updatedUserTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        updatedUserTaskInstanceEntity.setApproved(true);
        updatedUserTaskInstanceEntity.setOperatorId(1L);
        updatedUserTaskInstanceEntity.setComment("updated comment");
        updatedUserTaskInstanceEntity.setExpireIntervalSeconds(3600);
        updatedUserTaskInstanceEntity.setExternalFlowInstanceId("newExternalFlowInstanceId");

        int updatedRows = userTaskInstanceRepository.update(updatedUserTaskInstanceEntity);
        assertEquals(1, updatedRows);

        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(1L).get();
        assertEquals("newUserTaskId", updatedUserTaskInstance.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, updatedUserTaskInstance.getStatus());
        assertTrue(updatedUserTaskInstance.isApproved());
        assertEquals(1L, updatedUserTaskInstance.getOperatorId().longValue());
        assertEquals("updated comment", updatedUserTaskInstance.getComment());
        assertEquals(3600, updatedUserTaskInstance.getExpireIntervalSeconds().intValue());
        assertEquals("newExternalFlowInstanceId", updatedUserTaskInstance.getExternalFlowInstanceId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#updateUserTaskIdById(Long, String)}
     */
    @Test
    public void testUpdateUserTaskIdById() {
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId");
        assertEquals(1, updatedRows);

        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceRepository.findById(1L).get();
        assertEquals("newUserTaskId", userTaskInstanceEntity.getUserTaskId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#updateStatusById(Long, FlowNodeStatus)}
     */
    @Test
    public void testUpdateStatusById() {
        int updatedRows = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(1, updatedRows);

        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceRepository.findById(1L).get();
        assertEquals(FlowNodeStatus.APPROVED, userTaskInstanceEntity.getStatus());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#deleteByFlowInstanceId(Long)}
     */
    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, deletedRows);

        assertNull(userTaskInstanceRepository.findById(1L));
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndActivityId(FlowNodeType, String, Long)}
     */
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL, "activityId1", 1L);
        assertNotNull(userTaskInstanceEntityOptional);
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        assertEquals("activityId1", userTaskInstanceEntity.getActivityId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndName(FlowNodeType, String, Long)}
     */
    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.APPROVAL, "name1", 1L);
        assertNotNull(userTaskInstanceEntityOptional);
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        assertEquals("name1", userTaskInstanceEntity.getName());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findConfirmById(Long)}
     */
    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertFalse(confirm);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findApprovalInstanceIdByFlowInstanceIdAndStatus(Collection, FlowNodeStatus)}
     */
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstanceEntities);
        assertEquals(1, userTaskInstanceEntities.size());
    }
}