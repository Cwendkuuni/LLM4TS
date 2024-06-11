package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

/**
 * Test class for UserTaskInstanceRepository
 *
 * @author yh263208
 * @date 2023-03-15 10:23
 * @since ODC_release_3.3.0
 */
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository testUserTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByStatus(FlowNodeStatus)}
     */
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity2.setId(2L);
        entity2.setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = testUserTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrRoleIds(Long, Collection)}
     */
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setUserId(1L);
        entity1.setRoleId(1L);
        entity2.setId(2L);
        entity2.setUserId(1L);
        entity2.setRoleId(2L);
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>()))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = testUserTaskInstanceRepository
                .findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrResourceRoleIdentifier(Long, Collection)}
     */
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setUserId(1L);
        entity1.setResourceRoleIdentifier("role1");
        entity2.setId(2L);
        entity2.setUserId(1L);
        entity2.setResourceRoleIdentifier("role2");
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>()))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = testUserTaskInstanceRepository
                .findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByResourceRoleIdentifierIn(Collection)}
     */
    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setResourceRoleIdentifier("role1");
        entity2.setId(2L);
        entity2.setResourceRoleIdentifier("role1");
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(new ArrayList<>()))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = testUserTaskInstanceRepository
                .findByResourceRoleIdentifierIn(new ArrayList<>());

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByRoleId(Long)}
     */
    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setRoleId(1L);
        entity2.setId(2L);
        entity2.setRoleId(1L);
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = testUserTaskInstanceRepository.findByRoleId(1L);

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByCandidateUserId(Long)}
     */
    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setUserId(1L);
        entity2.setId(2L);
        entity2.setUserId(1L);
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = testUserTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#update(UserTaskInstanceEntity)}
     */
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("userTaskId");
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setApproved(false);
        entity.setOperatorId("operatorId");
        entity.setComment("comment");
        entity.setExpireIntervalSeconds(10L);
        entity.setExternalFlowInstanceId(1L);

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(entity));

        testUserTaskInstanceRepository.update(entity);

        UserTaskInstanceEntity updatedEntity = entity;
        updatedEntity.setUserTaskId("updatedUserTaskId");
        updatedEntity.setStatus(FlowNodeStatus.APPROVED);
        updatedEntity.setApproved(true);
        updatedEntity.setOperatorId("updatedOperatorId");
        updatedEntity.setComment("updatedComment");
        updatedEntity.setExpireIntervalSeconds(20L);
        updatedEntity.setExternalFlowInstanceId(2L);

        UserTaskInstanceEntity expectedEntity = new UserTaskInstanceEntity();
        expectedEntity.setId(1L);
        expectedEntity.setUserTaskId("updatedUserTaskId");
        expectedEntity.setStatus(FlowNodeStatus.APPROVED);
        expectedEntity.setApproved(true);
        expectedEntity.setOperatorId("updatedOperatorId");
        expectedEntity.setComment("updatedComment");
        expectedEntity.setExpireIntervalSeconds(20L);
        expectedEntity.setExternalFlowInstanceId(2L);

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(expectedEntity));

        testUserTaskInstanceRepository.update(entity);

        assertEquals(expectedEntity.getId(), entity.getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#updateUserTaskIdById(Long, String)}
     */
    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("userTaskId");

        testUserTaskInstanceRepository.updateUserTaskIdById(1L, "updatedUserTaskId");

        UserTaskInstanceEntity expectedEntity = new UserTaskInstanceEntity();
        expectedEntity.setId(1L);
        expectedEntity.setUserTaskId("updatedUserTaskId");

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(entity));

        testUserTaskInstanceRepository.updateUserTaskIdById(1L, "updatedUserTaskId");

        assertEquals(expectedEntity.getUserTaskId(), entity.getUserTaskId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#updateStatusById(Long, FlowNodeStatus)}
     */
    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);

        testUserTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        UserTaskInstanceEntity expectedEntity = new UserTaskInstanceEntity();
        expectedEntity.setId(1L);
        expectedEntity.setStatus(FlowNodeStatus.APPROVED);

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(entity));

        testUserTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        assertEquals(expectedEntity.getStatus(), entity.getStatus());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#deleteByFlowInstanceId(Long)}
     */
    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);

        testUserTaskInstanceRepository.deleteByFlowInstanceId(1L);

        verify(userTaskInstanceRepository, times(1)).delete(entity);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndActivityId(FlowNodeType, String, Long)}
     */
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setInstanceType(FlowNodeType.INITIATOR);
        entity.setActivityId("activityId");

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.INITIATOR, "activityId", 1L))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = testUserTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.INITIATOR, "activityId", 1L);

        assertEquals(entity.getId(), result.get().getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndName(FlowNodeType, String, Long)}
     */
    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setInstanceType(FlowNodeType.INITIATOR);
        entity.setName("name");

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.INITIATOR, "name", 1L))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = testUserTaskInstanceRepository
                .findByInstanceTypeAndName(FlowNodeType.INITIATOR, "name", 1L);

        assertEquals(entity.getId(), result.get().getId());
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findConfirmById(Long)}
     */
    @Test
    public void testFindConfirmById() {
        Boolean confirm = true;
        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(confirm);

        Boolean result = testUserTaskInstanceRepository.findConfirmById(1L);

        assertEquals(confirm, result);
    }

    /**
     * Test method for {@link UserTaskInstanceRepository#findApprovalInstanceIdByFlowInstanceIdAndStatus(Collection, FlowNodeStatus)}
     */
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setFlowInstanceId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entity2.setId(2L);
        entity2.setFlowInstanceId(1L);
        entity2.setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceEntities.add(entity1);
        userTaskInstanceEntities.add(entity2);

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(new ArrayList<>(), FlowNodeStatus.APPROVED))
                .thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = testUserTaskInstanceRepository
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(new ArrayList<>(), FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
    }
}