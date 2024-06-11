package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    void testFindByStatus() {
        Mockito.when(userTaskInstanceRepository.findAll(Mockito.eq(Sort.by(Sort.Direction.ASC, "id"))))
                .thenReturn(Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(), new UserTaskInstanceEntity()));

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        assertEquals(expected, actual);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findAll(Mockito.eq(Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findAll(Mockito.any(Sort.class)))
                .thenReturn(userTaskInstances);

        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        assertEquals(expected, actual);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findAll(Mockito.any(Sort.class));
    }

    @Test
    void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findAll(Mockito.any(Sort.class)))
                .thenReturn(userTaskInstances);

        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        assertEquals(expected, actual);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findAll(Mockito.any(Sort.class));
    }

    @Test
    void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findAll(Mockito.eq(Sort.by(Sort.Direction.ASC, "id"))))
                .thenReturn(userTaskInstances);

        Long roleId = 1L;

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity());
        assertEquals(expected, actual);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findAll(Mockito.eq(Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findAll(Mockito.eq(Sort.by(Sort.Direction.ASC, "id"))))
                .thenReturn(userTaskInstances);

        Long userId = 1L;

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity());
        assertEquals(expected, actual);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findAll(Mockito.eq(Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        MockitoAnnotations.initMocks(this);

        Mockito.when(userTaskInstanceRepository.findById(entity.getId()))
                .thenReturn(Optional.of(entity));

        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        updatedEntity.setId(entity.getId());

        userTaskInstanceRepository.update(updatedEntity);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .update(Mockito.eq(updatedEntity));
    }

    @Test
    void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .update("update flow_instance_node_approval set user_task_id=? where id=?", userTaskId, id);
    }

    @Test
    void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        userTaskInstanceRepository.updateStatusById(id, status);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .update("update flow_instance_node_approval set status=? where id=?", status.name(), id);
    }

    @Test
    void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .deleteFromUserTaskInstanceEntityByFlowInstanceId(instanceId);
    }

    @Test
    void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.eq(FlowNodeType.START), Mockito.eq("activityId"), Mockito.eq(1L)))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
        assertEquals(userTaskInstanceEntity, actual.get());

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findByInstanceTypeAndActivityId(Mockito.eq(FlowNodeType.START), Mockito.eq("activityId"), Mockito.eq(1L));
    }

    @Test
    void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.eq(FlowNodeType.START), Mockito.eq("name"), Mockito.eq(1L)))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L);
        assertEquals(userTaskInstanceEntity, actual.get());

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findByInstanceTypeAndName(Mockito.eq(FlowNodeType.START), Mockito.eq("name"), Mockito.eq(1L));
    }

    @Test
    void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean actual = userTaskInstanceRepository.findConfirmById(id);
        assertTrue(actual);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findConfirmById(id);
    }

    @Test
    void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findAll(Mockito.any(Sort.class)))
                .thenReturn(userTaskInstances);

        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        assertEquals(expected, actual);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1))
                .findAll(Mockito.any(Sort.class));
    }
}