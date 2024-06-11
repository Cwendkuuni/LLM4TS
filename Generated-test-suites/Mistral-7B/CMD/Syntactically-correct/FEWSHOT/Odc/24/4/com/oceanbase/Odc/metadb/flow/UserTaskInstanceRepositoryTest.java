package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

/**
 * Test class for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2023-03-16 10:12
 */
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);
        assertEquals(expectedList, result);
        verify(userTaskInstanceRepository).findByStatus(status);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(expectedList, result);
        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        assertEquals(expectedList, result);
        verify(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
        assertEquals(expectedList, result);
        verify(userTaskInstanceRepository).findByResourceRoleIdentifierIn(resourceRoleIdentifier);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(expectedList, result);
        verify(userTaskInstanceRepository).findByRoleId(roleId);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(expectedList, result);
        verify(userTaskInstanceRepository).findByCandidateUserId(userId);
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();

        Long id = 1L;
        String userTaskId = "userTaskId";
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        String operatorId = "operatorId";
        String comment = "comment";
        Long expireIntervalSeconds = 1L;
        String externalFlowInstanceId = "externalFlowInstanceId";

        doNothing().when(userTaskInstanceRepository).update(entity);

        int result = userTaskInstanceRepository.update(entity);
        assertEquals(0, result);

        entity.setId(id);
        updatedEntity.setUserTaskId(userTaskId);
        updatedEntity.setStatus(status);
        updatedEntity.setApproved(true);
        updatedEntity.setOperatorId(operatorId);
        updatedEntity.setComment(comment);
        updatedEntity.setExpireIntervalSeconds(expireIntervalSeconds);
        updatedEntity.setExternalFlowInstanceId(externalFlowInstanceId);

        doNothing().when(userTaskInstanceRepository).update(entity);
        doReturn(1).when(userTaskInstanceRepository).update(updatedEntity);

        result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).update(updatedEntity);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(0, result);

        doReturn(1).when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(0, result);

        doReturn(1).when(userTaskInstanceRepository).updateStatusById(id, status);

        result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateStatusById(id, status);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(0, result);

        doReturn(1).when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> expectedOptional = Optional.of(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(expectedOptional);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);
        assertEquals(expectedOptional, result);
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "name";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> expectedOptional = Optional.of(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(expectedOptional);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertEquals(expectedOptional, result);
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean expectedBoolean = true;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(expectedBoolean);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(expectedBoolean, result);
        verify(userTaskInstanceRepository).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> expectedList = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
                .thenReturn(expectedList);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);
        assertEquals(expectedList, result);
        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }
}