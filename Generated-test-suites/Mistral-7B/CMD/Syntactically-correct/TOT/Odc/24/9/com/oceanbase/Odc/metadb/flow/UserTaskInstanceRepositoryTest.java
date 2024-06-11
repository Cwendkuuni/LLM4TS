package com.oceanbase.odc.service.flow.test;

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

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.REJECTED);
        entities.add(entity2);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity1.setUserId(1L);
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.APPROVED);
        entity2.setRoleId(2L);
        entities.add(entity2);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)))
                .thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity1.setUserId(1L);
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.APPROVED);
        entity2.setResourceRoleIdentifier("role1");
        entities.add(entity2);

        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2")))
                .thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity1.setResourceRoleIdentifier("role1");
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.APPROVED);
        entity2.setResourceRoleIdentifier("role2");
        entities.add(entity2);

        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2")))
                .thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByResourceRoleIdentifierIn(
                Arrays.asList("role1", "role2"));

        assertEquals(2, result.size());
        verify(userTaskInstanceRepository).findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity1.setRoleId(1L);
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.APPROVED);
        entity2.setRoleId(2L);
        entities.add(entity2);

        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByRoleId(1L);

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByRoleId(1L);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity1.setUserId(1L);
        entities.add(entity1);

        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByCandidateUserId(1L);

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserId(1L);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        String comment = "Test comment";

        when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(entity));

        int result = userTaskInstanceRepositoryMock.update(entity);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).update(entity);

        entity.setUserTaskId("testUserTaskId");
        entity.setStatus(status);
        entity.setApproved(false);
        entity.setOperatorId(1L);
        entity.setComment(comment);
        entity.setExpireIntervalSeconds(1000L);
        entity.setExternalFlowInstanceId(1L);

        result = userTaskInstanceRepositoryMock.update(entity);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        long id = 1L;
        String userTaskId = "testUserTaskId";

        int result = userTaskInstanceRepositoryMock.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int result = userTaskInstanceRepositoryMock.updateStatusById(id, status);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateStatusById(id, status);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        long instanceId = 1L;
        int result = userTaskInstanceRepositoryMock.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        FlowNodeType instanceType = FlowNodeType.SEQUENTIAL;
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock
                .findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertTrue(result.isPresent());
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        FlowNodeType instanceType = FlowNodeType.SEQUENTIAL;
        String name = "testName";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock
                .findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertTrue(result.isPresent());
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test
    public void testFindConfirmById() {
        long id = 1L;
        Boolean confirm = true;

        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean result = userTaskInstanceRepositoryMock.findConfirmById(id);
        assertEquals(confirm, result);
        verify(userTaskInstanceRepository).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.REJECTED);
        entities.add(entity2);

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L),
                FlowNodeStatus.APPROVED)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singletonList(1L), FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singletonList(1L), FlowNodeStatus.APPROVED);
    }
}