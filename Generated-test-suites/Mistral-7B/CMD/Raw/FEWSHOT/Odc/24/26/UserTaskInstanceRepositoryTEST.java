package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

/**
 * Test class for UserTaskInstanceRepository
 *
 * @author your_name
 * @since ODC_release_3.3.0
 */
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    /**
     * Test findByStatus method
     */
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> statusList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        statusList.get(0).setStatus(FlowNodeStatus.APPROVED);
        statusList.get(1).setStatus(FlowNodeStatus.REJECTED);
        statusList.get(2).setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceRepository.saveAll(statusList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, resultList.size());
        UserTaskInstanceEntity userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(FlowNodeStatus.APPROVED, userTaskInstance.getStatus());

        resultList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED);
        assertEquals(1, resultList.size());
        userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(FlowNodeStatus.REJECTED, userTaskInstance.getStatus());

        resultList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertEquals(1, resultList.size());
        userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(FlowNodeStatus.PENDING, userTaskInstance.getStatus());

        resultList = userTaskInstanceRepository.findByStatus(null);
        assertEquals(3, resultList.size());
    }

    /**
     * Test findByCandidateUserIdOrRoleIds method
     */
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> statusList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        statusList.get(0).setUserId(1L);
        statusList.get(1).setUserId(1L);
        statusList.get(1).setRoleIds(Arrays.asList(1L, 2L));
        userTaskInstanceRepository.saveAll(statusList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L));
        assertEquals(2, resultList.size());
        UserTaskInstanceEntity userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(1L, userTaskInstance.getUserId());

        resultList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L));
        assertEquals(1, resultList.size());
        userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(1L, userTaskInstance.getUserId());

        resultList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Arrays.asList(1L, 2L));
        assertEquals(3, resultList.size());
    }

    /**
     * Test findByCandidateUserIdOrResourceRoleIdentifier method
     */
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> statusList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        statusList.get(0).setUserId(1L);
        statusList.get(1).setResourceRoleIdentifier("role1");
        statusList.get(2).setResourceRoleIdentifier("role1");
        userTaskInstanceRepository.saveAll(statusList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1"));
        assertEquals(2, resultList.size());
        UserTaskInstanceEntity userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(1L, userTaskInstance.getUserId());

        resultList = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, Arrays.asList("role1"));
        assertEquals(3, resultList.size());
    }

    /**
     * Test findByResourceRoleIdentifierIn method
     */
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> statusList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        statusList.get(0).setResourceRoleIdentifier("role1");
        statusList.get(1).setResourceRoleIdentifier("role2");
        statusList.get(2).setResourceRoleIdentifier("role1");
        userTaskInstanceRepository.saveAll(statusList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));
        assertEquals(2, resultList.size());
        UserTaskInstanceEntity userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals("role1", userTaskInstance.getResourceRoleIdentifier());

        resultList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.emptyList());
        assertEquals(3, resultList.size());
    }

    /**
     * Test findByRoleId method
     */
    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> statusList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        statusList.get(0).setRoleId(1L);
        statusList.get(1).setRoleId(1L);
        userTaskInstanceRepository.saveAll(statusList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(2, resultList.size());
        UserTaskInstanceEntity userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(1L, userTaskInstance.getRoleId());

        resultList = userTaskInstanceRepository.findByRoleId(null);
        assertEquals(3, resultList.size());
    }

    /**
     * Test findByCandidateUserId method
     */
    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> statusList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        statusList.get(0).setUserId(1L);
        userTaskInstanceRepository.saveAll(statusList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(1, resultList.size());
        UserTaskInstanceEntity userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(1L, userTaskInstance.getUserId());

        resultList = userTaskInstanceRepository.findByCandidateUserId(null);
        assertEquals(3, resultList.size());
    }

    /**
     * Test update method
     */
    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        UserTaskInstanceEntity newEntity = new UserTaskInstanceEntity();
        newEntity.setUserTaskId("newUserTaskId");
        newEntity.setStatus(FlowNodeStatus.APPROVED);
        newEntity.setApproved(true);
        newEntity.setOperatorId(1L);
        newEntity.setComment("new Comment");
        newEntity.setExpireIntervalSeconds(1000L);
        newEntity.setExternalFlowInstanceId("newExternalFlowInstanceId");

        userTaskInstanceRepository.update(newEntity);

        UserTaskInstanceEntity resultEntity = userTaskInstanceRepository.findById(1L).get();
        assertNotNull(resultEntity);
        assertEquals("newUserTaskId", resultEntity.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, resultEntity.getStatus());
        assertTrue(resultEntity.isApproved());
        assertEquals(1L, resultEntity.getOperatorId());
        assertEquals("new Comment", resultEntity.getComment());
        assertEquals(1000L, resultEntity.getExpireIntervalSeconds());
        assertEquals("newExternalFlowInstanceId", resultEntity.getExternalFlowInstanceId());
    }

    /**
     * Test updateUserTaskIdById method
     */
    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("oldUserTaskId");

        userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId");

        UserTaskInstanceEntity resultEntity = userTaskInstanceRepository.findById(1L).get();
        assertNotNull(resultEntity);
        assertEquals("newUserTaskId", resultEntity.getUserTaskId());
    }

    /**
     * Test updateStatusById method
     */
    @Test
    @Transactional
    public void testUpdateStatusById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);

        userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        UserTaskInstanceEntity resultEntity = userTaskInstanceRepository.findById(1L).get();
        assertNotNull(resultEntity);
        assertEquals(FlowNodeStatus.APPROVED, resultEntity.getStatus());
    }

    /**
     * Test deleteByFlowInstanceId method
     */
    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);

        userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        Optional<UserTaskInstanceEntity> resultEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.USER_TASK, "activityId", 1L);
        assertTrue(resultEntityOptional.isEmpty());
    }

    /**
     * Test findByInstanceTypeAndActivityId method
     */
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setInstanceType(FlowNodeType.USER_TASK);
        userTaskInstance.setActivityId("activityId");
        userTaskInstanceRepository.save(userTaskInstance);

        Optional<UserTaskInstanceEntity> resultEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.USER_TASK, "activityId", 1L);
        assertTrue(resultEntityOptional.isPresent());
        UserTaskInstanceEntity resultEntity = resultEntityOptional.get();
        assertNotNull(resultEntity);
        assertEquals(1L, resultEntity.getId());
        assertEquals(FlowNodeType.USER_TASK, resultEntity.getInstanceType());
        assertEquals("activityId", resultEntity.getActivityId());
        assertEquals(1L, resultEntity.getFlowInstanceId());
    }

    /**
     * Test findByInstanceTypeAndName method
     */
    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setInstanceType(FlowNodeType.USER_TASK);
        userTaskInstance.setName("name");
        userTaskInstanceRepository.save(userTaskInstance);

        Optional<UserTaskInstanceEntity> resultEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.USER_TASK, "name", 1L);
        assertTrue(resultEntityOptional.isPresent());
        UserTaskInstanceEntity resultEntity = resultEntityOptional.get();
        assertNotNull(resultEntity);
        assertEquals(1L, resultEntity.getId());
        assertEquals(FlowNodeType.USER_TASK, resultEntity.getInstanceType());
        assertEquals("name", resultEntity.getName());
        assertEquals(1L, resultEntity.getFlowInstanceId());
    }

    /**
     * Test findConfirmById method
     */
    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setWaitForConfirm(true);
        userTaskInstanceRepository.save(userTaskInstance);

        boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(result);
    }

    /**
     * Test findApprovalInstanceIdByFlowInstanceIdAndStatus method
     */
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> statusList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        statusList.get(0).setFlowInstanceId(1L);
        statusList.get(0).setStatus(FlowNodeStatus.APPROVED);
        statusList.get(1).setFlowInstanceId(1L);
        statusList.get(1).setStatus(FlowNodeStatus.REJECTED);
        statusList.get(2).setFlowInstanceId(2L);
        statusList.get(2).setStatus(FlowNodeStatus.APPROVED);
        statusList.get(3).setFlowInstanceId(2L);
        statusList.get(3).setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceRepository.saveAll(statusList);

        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(2, resultList.size());
        UserTaskInstanceEntity userTaskInstance = resultList.get(0);
        assertNotNull(userTaskInstance);
        assertEquals(1L, userTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeStatus.APPROVED, userTaskInstance.getStatus());

        userTaskInstance = resultList.get(1);
        assertNotNull(userTaskInstance);
        assertEquals(2L, userTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeStatus.APPROVED, userTaskInstance.getStatus());

        resultList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L),
                FlowNodeStatus.REJECTED);
        assertEquals(0, resultList.size());
    }

}