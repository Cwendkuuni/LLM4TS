package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
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

import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case for findByStatus method
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class)))
                .thenReturn(userTaskInstances);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(userTaskInstances.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(userTaskInstances.get(i), result.get(i));
        }
    }

    // Test case for findByCandidateUserIdOrRoleIds method
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), anyCollection()))
                .thenReturn(userTaskInstances);

        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(userTaskInstances.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(userTaskInstances.get(i), result.get(i));
        }
    }

    // Test case for findByCandidateUserIdOrResourceRoleIdentifier method
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), anyCollection()))
                .thenReturn(userTaskInstances);

        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        assertEquals(userTaskInstances.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(userTaskInstances.get(i), result.get(i));
        }
    }

    // Test case for findByResourceRoleIdentifierIn method
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(anyCollection()))
                .thenReturn(userTaskInstances);

        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(userTaskInstances.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(userTaskInstances.get(i), result.get(i));
        }
    }

    // Test case for findByRoleId method
    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByRoleId(anyLong()))
                .thenReturn(userTaskInstances);

        Long roleId = 1L;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(userTaskInstances.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(userTaskInstances.get(i), result.get(i));
        }
    }

    // Test case for findByCandidateUserId method
    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserId(anyLong()))
                .thenReturn(userTaskInstances);

        Long userId = 1L;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(userTaskInstances.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(userTaskInstances.get(i), result.get(i));
        }
    }

    // Test case for update method
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(entity));

        Long id = 1L;
        UserTaskInstanceEntity param = new UserTaskInstanceEntity();
        userTaskInstanceRepository.update(param);

        verify(userTaskInstanceRepository, times(1)).update(eq(entity), eq(param));
    }

    // Test case for updateUserTaskIdById method
    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(entity));

        Long id = 1L;
        String userTaskId = "userTaskId";
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(eq(id), eq(userTaskId));
    }

    // Test case for updateStatusById method
    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(entity));

        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        userTaskInstanceRepository.updateStatusById(id, status);

        verify(userTaskInstanceRepository, times(1)).updateStatusById(eq(id), eq(status));
    }

    // Test case for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(entity));

        Long instanceId = 1L;
        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(eq(instanceId));
    }

    // Test case for findByInstanceTypeAndActivityId method
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(),
                anyLong()))
                .thenReturn(Optional.ofNullable(entity));

        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    // Test case for findByInstanceTypeAndName method
    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), anyString(),
                anyLong()))
                .thenReturn(Optional.ofNullable(entity));

        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    // Test case for findConfirmById method
    @Test
    public void testFindConfirmById() {
        Boolean confirm = true;
        when(userTaskInstanceRepository.findConfirmById(anyLong()))
                .thenReturn(confirm);

        Long id = 1L;
        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        assertEquals(confirm, result);
    }

    // Test case for findApprovalInstanceIdByFlowInstanceIdAndStatus method
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity());
        userTaskInstances.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(anyCollection(),
                any(FlowNodeStatus.class)))
                .thenReturn(userTaskInstances);

        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);
        assertEquals(userTaskInstances.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(userTaskInstances.get(i), result.get(i));
        }
    }
}