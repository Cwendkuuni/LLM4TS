package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test findByStatus method
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntities.add(entity1);
        UserTaskInstanceEntity entity2 = Mockito.spy(new UserTaskInstanceEntity());
        entity2.setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceEntities.add(entity2);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());
    }

    // Test findByCandidateUserIdOrRoleIds method with valid parameters
    @Test
    public void testFindByCandidateUserIdOrRoleIds_validParams() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setUserId(userId);
        entity1.setRoleId(1L);
        userTaskInstanceEntities.add(entity1);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(userId, result.get(0).getUserId());
        assertTrue(roleIds.contains(result.get(0).getRoleId()));
    }

    // Test findByCandidateUserIdOrResourceRoleIdentifier method with valid parameters
    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier_validParams() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");

        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setUserId(userId);
        entity1.setResourceRoleIdentifier("role1");
        userTaskInstanceEntities.add(entity1);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(userId, result.get(0).getUserId());
        assertTrue(result.get(0).getResourceRoleIdentifier().equals("role1"));
    }

    // Test findByResourceRoleIdentifierIn method with valid parameters
    @Test
    public void testFindByResourceRoleIdentifierIn_validParams() {
        Collection<String> resourceRoleIdentifier = new ArrayList<>();
        resourceRoleIdentifier.add("role1");

        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setResourceRoleIdentifier("role1");
        userTaskInstanceEntities.add(entity1);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertTrue(result.get(0).getResourceRoleIdentifier().equals("role1"));
    }

    // Test findByRoleId method with valid parameter
    @Test
    public void testFindByRoleId_validParam() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setRoleId(roleId);
        userTaskInstanceEntities.add(entity1);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(roleId, result.get(0).getRoleId());
    }

    // Test findByCandidateUserId method with valid parameter
    @Test
    public void testFindByCandidateUserId_validParam() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setUserId(userId);
        userTaskInstanceEntities.add(entity1);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(userId, result.get(0).getUserId());
    }

    // Test update method with valid parameter
    @Test
    public void testUpdate_validParam() {
        Long id = 1L;
        UserTaskInstanceEntity entity = Mockito.spy(new UserTaskInstanceEntity());
        UserTaskInstanceEntity entityToUpdate = Mockito.spy(new UserTaskInstanceEntity());

        Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(entity));

        entityToUpdate.setId(id);
        entityToUpdate.setUserTaskId("newUserTaskId");

        int result = userTaskInstanceRepository.update(entityToUpdate);

        assertEquals(1, result);
        Mockito.verify(entity).setUserTaskId("newUserTaskId");
    }

    // Test updateUserTaskIdById method with valid parameter
    @Test
    public void testUpdateUserTaskIdById_validParam() {
        Long id = 1L;
        String userTaskId = "newUserTaskId";

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, result);
    }

    // Test updateStatusById method with valid parameter
    @Test
    public void testUpdateStatusById_validParam() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int result = userTaskInstanceRepository.updateStatusById(id, status);

        assertEquals(1, result);
        Mockito.verify(userTaskInstanceRepository).updateStatusById(id, status);
    }

    // Test deleteByFlowInstanceId method with valid parameter
    @Test
    public void testDeleteByFlowInstanceId_validParam() {
        Long instanceId = 1L;

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    // Test findByInstanceTypeAndActivityId method with valid parameters
    @Test
    public void testFindByInstanceTypeAndActivityId_validParams() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        String activityId = "activityId";

        UserTaskInstanceEntity entity = Mockito.spy(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, instanceId))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, instanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(instanceId, result.get().getId());
    }

    // Test findByInstanceTypeAndName method with valid parameters
    @Test
    public void testFindByInstanceTypeAndName_validParams() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        String name = "name";

        UserTaskInstanceEntity entity = Mockito.spy(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, instanceId))
                .thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                instanceId);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(instanceId, result.get().getId());
    }

    // Test findConfirmById method with valid parameter
    @Test
    public void testFindConfirmById_validParam() {
        Long id = 1L;
        Boolean confirm = true;

        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        assertEquals(confirm, result);
    }

    // Test findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid parameters
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus_validParams() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setFlowInstanceId(flowInstanceIds.get(0));
        entity1.setStatus(status);
        userTaskInstanceEntities.add(entity1);

        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(flowInstanceIds.get(0), result.get(0).getFlowInstanceId());
        assertEquals(status, result.get(0).getStatus());
    }
}