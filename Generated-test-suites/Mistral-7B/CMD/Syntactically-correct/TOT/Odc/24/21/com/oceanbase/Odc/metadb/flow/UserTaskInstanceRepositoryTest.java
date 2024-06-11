package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> jpaRepositoryMock;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity2.setStatus(FlowNodeStatus.REJECTED);
        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        assertEquals(1, result.size());
        verify(jpaRepositoryMock).findByStatus(eq(FlowNodeStatus.APPROVED));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setCandidateUserId(1L);
        entity2.setCandidateRoleId(2L);
        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Collections.singletonList(1L));

        // Then
        assertEquals(1, result.size());
        verify(jpaRepositoryMock).findByCandidateUserIdOrRoleIds(eq(1L), eq(Collections.singletonList(1L)));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setResourceRoleIdentifier("role1");
        entity2.setResourceRoleIdentifier("role2");
        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Collections.singletonList("role1"));

        // Then
        assertEquals(1, result.size());
        verify(jpaRepositoryMock).findByResourceRoleIdentifierIn(eq(Collections.singletonList("role1")));
    }

    @Test
    public void testFindByRoleId() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setRoleId(1L);
        entity2.setRoleId(2L);
        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        // Then
        assertEquals(1, result.size());
        verify(jpaRepositoryMock).findByRoleId(eq(1L));
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setCandidateUserId(1L);
        entity2.setCandidateUserId(2L);
        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        // Then
        assertEquals(1, result.size());
        verify(jpaRepositoryMock).findByCandidateUserId(eq(1L));
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        String userId = "userId";
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        String operatorId = "operatorId";
        String comment = "comment";
        int expireIntervalSeconds = 10;
        String externalFlowInstanceId = "externalFlowInstanceId";
        Long id = 1L;

        when(jpaRepositoryMock.findById(id)).thenReturn(Optional.of(entity));

        // When
        int result = userTaskInstanceRepository.update(entity);

        // Then
        fail("update method should not be called directly");
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        String userTaskId = "userTaskId";
        Long id = 1L;

        doNothing().when(jpaRepositoryMock).updateUserTaskIdById(id, userTaskId);

        // When
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, userTaskId);

        // Then
        assertEquals(1, result);
        verify(jpaRepositoryMock).updateUserTaskIdById(eq(1L), eq(userTaskId));
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        Long id = 1L;

        doNothing().when(jpaRepositoryMock).updateStatusById(id, status);

        // When
        int result = userTaskInstanceRepository.updateStatusById(1L, status);

        // Then
        assertEquals(1, result);
        verify(jpaRepositoryMock).updateStatusById(eq(1L), eq(status));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;

        doNothing().when(jpaRepositoryMock).deleteByFlowInstanceId(instanceId);

        // When
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(1, result);
        verify(jpaRepositoryMock).deleteByFlowInstanceId(eq(instanceId));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity));

        // When
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        // Then
        assertTrue(result.isPresent());
        verify(jpaRepositoryMock).findByInstanceTypeAndActivityId(eq(instanceType), eq(activityId), eq(flowInstanceId));
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "name";
        Long flowInstanceId = 1L;

        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity));

        // When
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        // Then
        assertTrue(result.isPresent());
        verify(jpaRepositoryMock).findByInstanceTypeAndName(eq(instanceType), eq(name), eq(flowInstanceId));
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;
        boolean confirm = true;

        when(jpaRepositoryMock.findAll()).thenReturn(List.of(new UserTaskInstanceEntity()));
        when(jpaRepositoryMock.findByConfirmById(id)).thenReturn(confirm);

        // When
        boolean result = userTaskInstanceRepository.findConfirmById(id);

        // Then
        assertEquals(confirm, result);
        verify(jpaRepositoryMock).findByConfirmById(eq(id));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setFlowInstanceId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity2.setFlowInstanceId(1L);
        entity2.setStatus(FlowNodeStatus.REJECTED);
        when(jpaRepositoryMock.findAll()).thenReturn(List.of(entity1, entity2));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                List.of(1L), FlowNodeStatus.APPROVED);

        // Then
        assertEquals(1, result.size());
        verify(jpaRepositoryMock).findByFlowInstanceIdInAndStatus(eq(List.of(1L)), eq(FlowNodeStatus.APPROVED));
    }
}