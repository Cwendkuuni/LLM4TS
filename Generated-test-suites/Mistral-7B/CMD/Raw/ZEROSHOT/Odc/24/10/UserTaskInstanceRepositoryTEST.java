package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.oceanbase.odc.metadb.config.MetadbTestConfiguration;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = MetadbTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId("operatorId");
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(100L);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setUserId(1L);
        entity1.setRoleId(1L);
        userTaskInstanceEntities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setUserId(1L);
        entity2.setRoleId(2L);
        userTaskInstanceEntities.add(entity2);

        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();
        entity3.setId(3L);
        entity3.setUserId(2L);
        entity3.setRoleId(2L);
        userTaskInstanceEntities.add(entity3);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result1 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
        assertEquals(2, result1.size());
        assertTrue(result1.contains(entity1));
        assertTrue(result1.contains(entity2));

        List<UserTaskInstanceEntity> result2 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Arrays.asList(1L));
        assertEquals(2, result2.size());
        assertTrue(result2.contains(entity1));
        assertTrue(result2.contains(entity2));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setResourceRoleIdentifier("roleIdentifier1");
        userTaskInstanceEntities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setResourceRoleIdentifier("roleIdentifier2");
        userTaskInstanceEntities.add(entity2);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier1"));
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setRoleId(1L);
        userTaskInstanceEntities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setRoleId(1L);
        userTaskInstanceEntities.add(entity2);

        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();
        entity3.setId(3L);
        entity3.setRoleId(2L);
        userTaskInstanceEntities.add(entity3);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(entity1, entity2)));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setUserId(1L);
        userTaskInstanceEntities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setUserId(2L);
        userTaskInstanceEntities.add(entity2);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    public void testUpdate() {
        Mockito.doAnswer(invocation -> {
            UserTaskInstanceEntity entity = (UserTaskInstanceEntity) invocation.getArgument(0);
            entity.setUserId("updatedUserId");
            entity.setStatus(FlowNodeStatus.IN_PROGRESS);
            entity.setApproved(false);
            entity.setOperatorId("updatedOperatorId");
            entity.setComment("updatedComment");
            entity.setExpireIntervalSeconds(200L);
            entity.setExternalFlowInstanceId("updatedExternalFlowInstanceId");
            return 1;
        }).when(userTaskInstanceRepository).update(Mockito.any(UserTaskInstanceEntity.class));

        userTaskInstanceRepository.update(userTaskInstanceEntity);
        assertEquals("updatedUserId", userTaskInstanceEntity.getUserId());
        assertEquals(FlowNodeStatus.IN_PROGRESS, userTaskInstanceEntity.getStatus());
        assertFalse(userTaskInstanceEntity.isApproved());
        assertEquals("updatedOperatorId", userTaskInstanceEntity.getOperatorId());
        assertEquals("updatedComment", userTaskInstanceEntity.getComment());
        assertEquals(200L, userTaskInstanceEntity.getExpireIntervalSeconds());
        assertNull(userTaskInstanceEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Mockito.doAnswer(invocation -> {
            Long id = (Long) invocation.getArgument(0);
            String userTaskId = (String) invocation.getArgument(1);
            UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(id).get();
            entity.setUserTaskId(userTaskId);
            return 1;
        }).when(userTaskInstanceRepository).updateUserTaskIdById(Mockito.anyLong(), Mockito.anyString());

        userTaskInstanceRepository.updateUserTaskIdById(1L, "updatedUserTaskId");
        UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(1L).get();
        assertEquals("updatedUserTaskId", entity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        Mockito.doAnswer(invocation -> {
            Long id = (Long) invocation.getArgument(0);
            FlowNodeStatus status = (FlowNodeStatus) invocation.getArgument(1);
            UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(id).get();
            entity.setStatus(status);
            return 1;
        }).when(userTaskInstanceRepository).updateStatusById(Mockito.anyLong(), Mockito.any(FlowNodeStatus.class));

        userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.IN_PROGRESS);
        UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(1L).get();
        assertEquals(FlowNodeStatus.IN_PROGRESS, entity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.doAnswer(invocation -> {
            Long instanceId = (Long) invocation.getArgument(0);
            List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
            List<UserTaskInstanceEntity> filteredEntities = new ArrayList<>();
            for (UserTaskInstanceEntity entity : entities) {
                if (entity.getFlowInstanceId() != instanceId) {
                    filteredEntities.add(entity);
                }
            }
            userTaskInstanceRepository.saveAll(filteredEntities);
            return 1;
        }).when(userTaskInstanceRepository).deleteByFlowInstanceId(Mockito.anyLong());

        userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
        List<UserTaskInstanceEntity> expectedEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(2L);
        entity1.setFlowInstanceId(2L);
        expectedEntities.add(entity1);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(3L);
        entity2.setFlowInstanceId(3L);
        expectedEntities.add(entity2);
        assertTrue(entities.isEmpty());
        assertEquals(expectedEntities, userTaskInstanceRepository.findAll());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setInstanceType(FlowNodeType.TASK);
        entity.setActivityId("activityId");
        entity.setFlowInstanceId(1L);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activityId", 1L))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK,
                "activityId", 1L);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setInstanceType(FlowNodeType.TASK);
        entity.setName("name");
        entity.setFlowInstanceId(1L);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setWaitForConfirm(true);

        Mockito.when(userTaskInstanceRepository.findByConfirmById(1L)).thenReturn(true);

        assertTrue(userTaskInstanceRepository.findConfirmById(1L));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setStatus(FlowNodeStatus.REJECTED);
        entities.add(entity2);

        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();
        entity3.setId(3L);
        entity3.setStatus(FlowNodeStatus.APPROVED);
        entities.add(entity3);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(entity1, entity3)));
    }
}