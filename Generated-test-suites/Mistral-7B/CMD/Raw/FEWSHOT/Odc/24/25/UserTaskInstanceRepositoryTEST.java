package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> actual = repository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setRoleId(1L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L)))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> actual = repository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setResourceRoleIdentifier("role1");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(entity1);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByResourceRoleIdentifierIn(Collections.singletonList("role1")))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> actual = repository.findByResourceRoleIdentifierIn(Collections.singletonList("role1"));
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setRoleId(1L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(entity);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByRoleId(1L)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> actual = repository.findByRoleId(1L);
        assertEquals(1, actual.size());
        assertEquals(entity, actual.get(0));
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setUserId(1L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(entity);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByCandidateUserId(1L)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> actual = repository.findByCandidateUserId(1L);
        assertEquals(1, actual.size());
        assertEquals(entity, actual.get(0));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("userTaskId");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId("operatorId");
        entity.setComment("comment");
        entity.setExpireIntervalSeconds(1000);
        entity.setExternalFlowInstanceId("externalFlowInstanceId");

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.update(entity)).thenReturn(1);

        int actual = repository.update(entity);
        assertEquals(1, actual);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("oldUserTaskId");

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.updateUserTaskIdById(1L, "newUserTaskId")).thenReturn(1);

        int actual = repository.updateUserTaskIdById(1L, "newUserTaskId");
        assertEquals(1, actual);
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.APPROVED);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.updateStatusById(1L, FlowNodeStatus.REJECTED)).thenReturn(1);

        int actual = repository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        assertEquals(1, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.deleteByFlowInstanceId(1L)).thenReturn(1);

        int actual = repository.deleteByFlowInstanceId(1L);
        assertEquals(1, actual);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> actual = repository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
        assertNotNull(actual);
        assertEquals(entity, actual.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> actual = repository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L);
        assertNotNull(actual);
        assertEquals(entity, actual.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findConfirmById(1L)).thenReturn(true);

        boolean actual = repository.findConfirmById(1L);
        assertTrue(actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> actual = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED);
        assertEquals(1, actual.size());
        assertEquals(entity1, actual.get(0));
    }

    @Test
    public void testFindAll() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findAll()).thenReturn(new PageImpl<>(Arrays.asList(entity1, entity2), new PageRequest(0, 10)));

        Page<UserTaskInstanceEntity> actual = repository.findAll(Pageable.unpaged());
        assertEquals(2, actual.getContent().size());
    }

}