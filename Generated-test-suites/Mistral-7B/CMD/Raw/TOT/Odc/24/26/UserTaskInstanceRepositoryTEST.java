package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepositoryImpl;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceRepository = new UserTaskInstanceRepositoryImpl();
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.save(any(UserTaskInstanceEntity.class))).thenReturn(userTaskInstanceEntity);
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(Collections.singletonList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity newEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(userTaskInstanceEntity));
        userTaskInstanceRepository.update(newEntity);
        verify(userTaskInstanceEntity).setUserTaskId(newEntity.getUserTaskId())
                .setStatus(newEntity.getStatus())
                .setApproved(newEntity.isApproved())
                .setOperatorId(newEntity.getOperatorId())
                .setComment(newEntity.getComment())
                .setExpireIntervalSeconds(newEntity.getExpireIntervalSeconds())
                .setExternalFlowInstanceId(newEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";
        when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(userTaskInstanceEntity));
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        verify(userTaskInstanceEntity).setUserTaskId(userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(userTaskInstanceEntity));
        userTaskInstanceRepository.updateStatusById(id, status);
        verify(userTaskInstanceEntity).setStatus(status.name());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        verify(userTaskInstanceRepository, times(1)).delete(any(UserTaskInstanceEntity.class));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "name";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);
        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status)).thenReturn(Collections.singletonList(userTaskInstanceEntity.getId()));
        List<Long> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status);
        assertEquals(1, result.size());
    }
}