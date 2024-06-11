package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

public class UserTaskInstanceRepositoryTest {
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private SimpleJpaRepository<UserTaskInstanceEntity, Long> simpleJpaRepository;

    @Before
    public void setup() {
        simpleJpaRepository = mock(SimpleJpaRepository.class);
        when(simpleJpaRepository.findAll()).thenReturn(new ArrayList<>());
        userTaskInstanceRepository = new UserTaskInstanceRepositoryImpl(simpleJpaRepository);
    }

    @After
    public void tearDown() {
        reset(simpleJpaRepository);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().withStatus(FlowNodeStatus.APPROVED).build(),
                UserTaskInstanceEntityBuilder.anEntity().withStatus(FlowNodeStatus.REJECTED).build());

        when(simpleJpaRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().withCandidateUserId(1L).build(),
                UserTaskInstanceEntityBuilder.anEntity().withCandidateUserId(2L).build());

        when(simpleJpaRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(1, result.size());
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().withCandidateUserId(1L).build(),
                UserTaskInstanceEntityBuilder.anEntity().withRoleId(1L).build(),
                UserTaskInstanceEntityBuilder.anEntity().withRoleId(2L).build());

        when(simpleJpaRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        assertEquals(2, result.size());
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().withResourceRoleIdentifier("role1").build(),
                UserTaskInstanceEntityBuilder.anEntity().withResourceRoleIdentifier("role2").build());

        when(simpleJpaRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));

        assertEquals(1, result.size());
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().withRoleId(1L).build(),
                UserTaskInstanceEntityBuilder.anEntity().withRoleId(2L).build());

        when(simpleJpaRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(1, result.size());
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = UserTaskInstanceEntityBuilder.anEntity().build();

        int numRowsAffected = userTaskInstanceRepository.update(entity);

        assertEquals(1, numRowsAffected);
        verify(simpleJpaRepository).save(entity);
    }

    @Test
    public void testUpdateByUserTaskId() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        int numRowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, numRowsAffected);
        verify(simpleJpaRepository).update("flow_instance_node_approval set user_task_id=? where id=?", userTaskId, id);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int numRowsAffected = userTaskInstanceRepository.updateStatusById(id, status);

        assertEquals(1, numRowsAffected);
        verify(simpleJpaRepository).update("update flow_instance_node_approval set status=? where id=?", status.name(), id);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int numRowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, numRowsAffected);
        verify(simpleJpaRepository).deleteAll(eq(UserTaskInstanceEntity.class), eq(instanceId));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = UserTaskInstanceEntityBuilder.anEntity().build();

        when(simpleJpaRepository.findAll()).thenReturn(Arrays.asList(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", 1L);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = UserTaskInstanceEntityBuilder.anEntity().build();

        when(simpleJpaRepository.findAll()).thenReturn(Arrays.asList(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        when(simpleJpaRepository.findAll()).thenReturn(Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().withConfirm(confirm).build()));

        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        assertEquals(confirm, result);
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().withFlowInstanceId(1L).withStatus(FlowNodeStatus.APPROVED).build(),
                UserTaskInstanceEntityBuilder.anEntity().withFlowInstanceId(1L).withStatus(FlowNodeStatus.REJECTED).build(),
                UserTaskInstanceEntityBuilder.anEntity().withFlowInstanceId(2L).withStatus(FlowNodeStatus.APPROVED).build());

        when(simpleJpaRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        assertEquals(entities.get(0), result.get(0));
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().build(),
                UserTaskInstanceEntityBuilder.anEntity().build());

        when(simpleJpaRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findAll();

        assertEquals(entities, result);
        verify(simpleJpaRepository).findAll();
    }

    @Test
    public void testFindAllByPageable() {
        List<UserTaskInstanceEntity> entities = Arrays.asList(
                UserTaskInstanceEntityBuilder.anEntity().build(),
                UserTaskInstanceEntityBuilder.anEntity().build());

        Pageable pageable = PageRequest.of(0, 10);
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, pageable, entities.size());

        when(simpleJpaRepository.findAll(pageable)).thenReturn(page);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findAll(pageable);

        assertEquals(entities, result);
        verify(simpleJpaRepository).findAll(pageable);
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity entity = UserTaskInstanceEntityBuilder.anEntity().build();

        UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);

        assertEquals(entity, savedEntity);
        verify(simpleJpaRepository).save(entity);
    }
}