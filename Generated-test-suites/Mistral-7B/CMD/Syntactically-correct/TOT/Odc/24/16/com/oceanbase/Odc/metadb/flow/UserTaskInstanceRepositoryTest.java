package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepositoryImpl;

/**
 * Test class for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2023-03-27 16:12
 * @since ODC_release_3.3.0
 */
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository repository;

    @InjectMocks
    private UserTaskInstanceRepositoryImpl repositoryImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for findByStatus method
     */
    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        when(repository.findByStatus(status)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findByStatus(status);

        assertEquals(expected, actual);
        verify(repository).findByStatus(status);
    }

    /**
     * Test for findByCandidateUserIdOrRoleIds method with valid parameters
     */
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        when(repository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expected, actual);
        verify(repository).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    /**
     * Test for findByCandidateUserIdOrResourceRoleIdentifier method with valid parameters
     */
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        when(repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);

        assertEquals(expected, actual);
        verify(repository).findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
    }

    /**
     * Test for findByResourceRoleIdentifierIn method with valid parameters
     */
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifier = new ArrayList<>();
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        when(repository.findByResourceRoleIdentifierIn(resourceRoleIdentifier))
                .thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        assertEquals(expected, actual);
        verify(repository).findByResourceRoleIdentifierIn(resourceRoleIdentifier);
    }

    /**
     * Test for findByRoleId method with valid parameter
     */
    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        when(repository.findByRoleId(roleId)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findByRoleId(roleId);

        assertEquals(expected, actual);
        verify(repository).findByRoleId(roleId);
    }

    /**
     * Test for findByCandidateUserId method with valid parameter
     */
    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        when(repository.findByCandidateUserId(userId)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findByCandidateUserId(userId);

        assertEquals(expected, actual);
        verify(repository).findByCandidateUserId(userId);
    }

    /**
     * Test for update method with valid parameters
     */
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        repositoryImpl.update(updatedEntity);

        verify(repository).update(updatedEntity);
        verify(entity).setUserTaskId(updatedEntity.getUserTaskId())
                .setStatus(updatedEntity.getStatus())
                .setApproved(updatedEntity.isApproved())
                .setOperatorId(updatedEntity.getOperatorId())
                .setComment(updatedEntity.getComment())
                .setExpireIntervalSeconds(updatedEntity.getExpireIntervalSeconds())
                .setExternalFlowInstanceId(updatedEntity.getExternalFlowInstanceId());
    }

    /**
     * Test for updateUserTaskIdById method with valid parameters
     */
    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        repositoryImpl.updateUserTaskIdById(id, userTaskId);

        verify(repository).updateUserTaskIdById(id, userTaskId);
    }

    /**
     * Test for updateStatusById method with valid parameters
     */
    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        repositoryImpl.updateStatusById(id, status);

        verify(repository).updateStatusById(id, status);
    }

    /**
     * Test for deleteByFlowInstanceId method with valid parameter
     */
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        repositoryImpl.deleteByFlowInstanceId(instanceId);

        verify(repository).deleteByFlowInstanceId(instanceId);
    }

    /**
     * Test for findByInstanceTypeAndActivityId method with valid parameters
     */
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        when(repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> actual = repositoryImpl.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        assertEquals(expected, actual.get());
        verify(repository).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    /**
     * Test for findByInstanceTypeAndName method with valid parameters
     */
    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "name";
        Long flowInstanceId = 1L;
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        when(repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> actual = repositoryImpl.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        assertEquals(expected, actual.get());
        verify(repository).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    /**
     * Test for findConfirmById method with valid parameter
     */
    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean expected = true;
        when(repository.findConfirmById(id)).thenReturn(expected);

        Boolean actual = repositoryImpl.findConfirmById(id);

        assertEquals(expected, actual);
        verify(repository).findConfirmById(id);
    }

    /**
     * Test for findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid parameters
     */
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = new ArrayList<>();

        when(repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
                .thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                status);

        assertEquals(expected, actual);
        verify(repository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }

    /**
     * Test for findByStatus method with empty list
     */
    @Test
    public void testFindByStatusEmptyList() {
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expected = Collections.emptyList();
        when(repository.findByStatus(status)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = repositoryImpl.findByStatus(status);

        assertEquals(expected, actual);
        verify(repository).findByStatus(status);
    }
}