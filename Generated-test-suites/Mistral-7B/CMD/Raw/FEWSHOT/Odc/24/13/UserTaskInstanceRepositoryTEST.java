package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, FlowNodeStatus.APPROVED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(2L, FlowNodeStatus.REJECTED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(3L, FlowNodeStatus.APPROVED));

        when(repository.findAll()).thenReturn(userTaskInstances);

        List<UserTaskInstanceEntity> expected = repository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(2, expected.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", FlowNodeStatus.APPROVED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(2L, "user2", FlowNodeStatus.APPROVED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(3L, "user3", FlowNodeStatus.APPROVED));

        when(repository.findAll()).thenReturn(userTaskInstances);

        Long userId = 1L;
        Collection<Long> roleIds = Collections.singletonList(2L);

        List<UserTaskInstanceEntity> expected = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(1, expected.size());
        assertEquals("user1", expected.get(0).getUser());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", "role1", FlowNodeStatus.APPROVED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(2L, "user2", "role2", FlowNodeStatus.APPROVED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(3L, "user3", "role1", FlowNodeStatus.APPROVED));

        when(repository.findAll()).thenReturn(userTaskInstances);

        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Collections.singletonList("role1");

        List<UserTaskInstanceEntity> expected = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        assertEquals(2, expected.size());
        assertEquals("user1", expected.get(0).getUser());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, null, "role1", FlowNodeStatus.APPROVED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(2L, null, "role2", FlowNodeStatus.APPROVED));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(3L, null, "role1", FlowNodeStatus.APPROVED));

        when(repository.findAll()).thenReturn(userTaskInstances);

        Collection<String> resourceRoleIdentifier = Collections.singletonList("role1");

        List<UserTaskInstanceEntity> expected = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        assertEquals(2, expected.size());
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", null, FlowNodeStatus.APPROVED, 1L));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(2L, "user2", null, FlowNodeStatus.APPROVED, 2L));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(3L, "user3", null, FlowNodeStatus.APPROVED, 1L));

        when(repository.findAll()).thenReturn(userTaskInstances);

        Long roleId = 1L;

        List<UserTaskInstanceEntity> expected = repository.findByRoleId(roleId);

        assertEquals(2, expected.size());
        assertEquals("user1", expected.get(0).getUser());
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", null, FlowNodeStatus.APPROVED, 1L));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(2L, "user2", null, FlowNodeStatus.APPROVED, 2L));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(3L, "user1", null, FlowNodeStatus.APPROVED, 3L));

        when(repository.findAll()).thenReturn(userTaskInstances);

        Long userId = 1L;

        List<UserTaskInstanceEntity> expected = repository.findByCandidateUserId(userId);

        assertEquals(2, expected.size());
        assertEquals("user1", expected.get(0).getUser());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", null, FlowNodeStatus.APPROVED, 1L);

        when(repository.update(any(UserTaskInstanceEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserTaskInstanceEntity updatedEntity = repository.update(entity);

        assertEquals(entity, updatedEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        Long id = 1L;
        String userTaskId = "taskId";

        when(repository.updateUserTaskIdById(id, userTaskId)).thenReturn(1);

        int result = repository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        when(repository.updateStatusById(id, status)).thenReturn(1);

        int result = repository.updateStatusById(id, status);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        Long instanceId = 1L;

        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstance = UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", FlowNodeStatus.APPROVED, 1L, 1L);
        userTaskInstances.add(userTaskInstance);

        when(repository.findAll()).thenReturn(userTaskInstances);

        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> expected = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertTrue(expected.isPresent());
        assertEquals(userTaskInstance, expected.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstance = UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", FlowNodeStatus.APPROVED, null, 1L, "name");
        userTaskInstances.add(userTaskInstance);

        when(repository.findAll()).thenReturn(userTaskInstances);

        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "name";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> expected = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        assertTrue(expected.isPresent());
        assertEquals(userTaskInstance, expected.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        Long id = 1L;
        Boolean confirm = true;

        when(repository.findConfirmById(id)).thenReturn(confirm);

        Boolean result = repository.findConfirmById(id);

        assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(1L, "user1", FlowNodeStatus.APPROVED, 1L, 1L));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(2L, "user2", FlowNodeStatus.APPROVED, 1L, 2L));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(3L, "user3", FlowNodeStatus.APPROVED, 1L, 3L));
        userTaskInstances.add(UserTaskInstanceEntityBuilder.createUserTaskInstanceEntity(4L, "user4", FlowNodeStatus.REJECTED, 1L, 4L));

        when(repository.findAll()).thenReturn(userTaskInstances);

        Collection<Long> flowInstanceIds = Collections.singletonList(1L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> expected = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        assertEquals(3, expected.size());
        assertEquals(1L, expected.get(0).getId());
        assertEquals(2L, expected.get(1).getId());
        assertEquals(3L, expected.get(2).getId());
    }
}