package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(list);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(list);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(list);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertNotNull(list);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByRoleId(roleId);
        assertNotNull(list);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertNotNull(list);
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        updatedEntity.setId(id);
        updatedEntity.setUserTaskId("newUserTaskId");
        updatedEntity.setStatus(FlowNodeStatus.APPROVED);
        updatedEntity.setApproved(true);
        updatedEntity.setOperatorId("newOperatorId");
        updatedEntity.setComment("newComment");
        updatedEntity.setExpireIntervalSeconds(10L);
        updatedEntity.setExternalFlowInstanceId("newExternalFlowInstanceId");

        int result = userTaskInstanceRepository.update(updatedEntity);
        assertEquals(1, result);
    }

    @Test
    @Rollback(value = false)
    @Transactional
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "newUserTaskId";

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    @Rollback(value = false)
    @Transactional
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    @Rollback(value = false)
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String activityId = "activity1";
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String name = "name1";
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        assertFalse(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertNotNull(list);
    }
}