package com.oceanbase.odc.metadb.flow;

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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        UserTaskInstanceEntityBuilder userTaskInstanceBuilder = new UserTaskInstanceEntityBuilder();
        this.userTaskInstance = userTaskInstanceBuilder.build();
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(this.userTaskInstance);
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setUserId(1L);
        userTaskInstanceEntities.add(userTaskInstance1);

        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setUserId(2L);
        userTaskInstance2.setRoleId(1L);
        userTaskInstanceEntities.add(userTaskInstance2);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));
        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertEquals(1L, result.get(0).getUserId().longValue());
        assertEquals(1L, result.get(1).getRoleId().longValue());
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setResourceRoleIdentifier("role1");
        userTaskInstanceEntities.add(userTaskInstance1);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("role1", "role2"));
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals("role1", result.get(0).getResourceRoleIdentifier());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(this.userTaskInstance);
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(1L, result.get(0).getRoleId().longValue());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(this.userTaskInstance);
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(1L, result.get(0).getUserId().longValue());
    }

    @Test
    public void testUpdate() {
        long id = 1L;
        UserTaskInstanceEntity newUserTaskInstance = new UserTaskInstanceEntity();
        newUserTaskInstance.setUserTaskId("newUserTaskId");
        newUserTaskInstance.setStatus(FlowNodeStatus.APPROVED);
        newUserTaskInstance.setApproved(true);
        newUserTaskInstance.setOperatorId(1L);
        newUserTaskInstance.setComment("newComment");
        newUserTaskInstance.setExpireIntervalSeconds(1000L);
        newUserTaskInstance.setExternalFlowInstanceId("newExternalFlowInstanceId");

        int result = userTaskInstanceRepository.update(newUserTaskInstance);
        assertEquals(1, result);

        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(id).get();
        assertEquals("newUserTaskId", updatedUserTaskInstance.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, updatedUserTaskInstance.getStatus());
        assertEquals(true, updatedUserTaskInstance.isApproved());
        assertEquals(1L, updatedUserTaskInstance.getOperatorId().longValue());
        assertEquals("newComment", updatedUserTaskInstance.getComment());
        assertEquals(1000L, updatedUserTaskInstance.getExpireIntervalSeconds().longValue());
        assertEquals("newExternalFlowInstanceId", updatedUserTaskInstance.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        long id = 1L;
        String newUserTaskId = "newUserTaskId";

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, newUserTaskId);
        assertEquals(1, result);

        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(id).get();
        assertEquals(newUserTaskId, updatedUserTaskInstance.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        long id = 1L;
        FlowNodeStatus newStatus = FlowNodeStatus.REJECTED;

        int result = userTaskInstanceRepository.updateStatusById(id, newStatus);
        assertEquals(1, result);

        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(id).get();
        assertEquals(newStatus.name(), updatedUserTaskInstance.getStatus().name());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        long flowInstanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(1, result);

        assertNull(userTaskInstanceRepository.findByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstance));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL, "activityId", 1L);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstance, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L))
                .thenReturn(Optional.of(userTaskInstance));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.APPROVAL, "name", 1L);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstance, result.get());
    }

    @Test
    public void testFindConfirmById() {
        long id = 1L;
        boolean confirm = true;

        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(this.userTaskInstance);
        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(1L, result.get(0).getId().longValue());
    }
}