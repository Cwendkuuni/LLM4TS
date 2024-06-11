package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId(1L);
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(100);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity task1 = new UserTaskInstanceEntity();
        task1.setId(1L);
        task1.setUserId(1L);
        userTaskInstanceEntities.add(task1);
        UserTaskInstanceEntity task2 = new UserTaskInstanceEntity();
        task2.setId(2L);
        task2.setRoleId(1L);
        userTaskInstanceEntities.add(task2);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task1, result.get(0));

        result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task2, result.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByCandidateUserIdOrResourceRoleIdentifierWithNullUserId() {
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(Collections.emptyList());
        userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, Arrays.asList("roleIdentifier"));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier"));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testUpdate() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        updatedEntity.setId(1L);
        updatedEntity.setUserTaskId("updatedUserTaskId");
        updatedEntity.setStatus(FlowNodeStatus.REJECTED);
        updatedEntity.setApproved(false);
        updatedEntity.setOperatorId(2L);
        updatedEntity.setComment("updatedComment");
        updatedEntity.setExpireIntervalSeconds(200);
        updatedEntity.setExternalFlowInstanceId("updatedExternalFlowInstanceId");

        int result = userTaskInstanceRepository.update(updatedEntity);
        assertEquals(1, result);

        UserTaskInstanceEntity entityFromDB = Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity)).get();
        assertEquals("updatedUserTaskId", entityFromDB.getUserTaskId());
        assertEquals(FlowNodeStatus.REJECTED, entityFromDB.getStatus());
        assertEquals(false, entityFromDB.isApproved());
        assertEquals(2L, entityFromDB.getOperatorId());
        assertEquals("updatedComment", entityFromDB.getComment());
        assertEquals(200, entityFromDB.getExpireIntervalSeconds());
        assertEquals("updatedExternalFlowInstanceId", entityFromDB.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "updatedUserTaskId");
        assertEquals(1, result);

        UserTaskInstanceEntity entityFromDB = Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity)).get();
        assertEquals("updatedUserTaskId", entityFromDB.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        assertEquals(1, result);

        UserTaskInstanceEntity entityFromDB = Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity)).get();
        assertEquals(FlowNodeStatus.REJECTED, entityFromDB.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(Arrays.asList(userTaskInstanceEntity));

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);

        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).delete(userTaskInstanceEntity);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setFlowInstanceId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId(1L);
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(100);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activityId", 1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setFlowInstanceId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId(1L);
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(100);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertFalse(result);

        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(new UserTaskInstanceEntity()));
        result = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.APPROVED);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }
}