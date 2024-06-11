package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    // Test findByStatus method
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVING);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    // Test findByCandidateUserIdOrRoleIds method with valid arguments
    @Test
    public void testFindByCandidateUserIdOrRoleIds_ValidArgs() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));
        assertNotNull(userTaskInstances);
    }

    // Test findByCandidateUserIdOrRoleIds method with empty roleIds list
    @Test
    public void testFindByCandidateUserIdOrRoleIds_EmptyRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                new ArrayList<>());
        assertNull(userTaskInstances);
    }

    // Test findByResourceRoleIdentifier method with valid arguments
    @Test
    public void testFindByResourceRoleIdentifier_ValidArgs() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("role1", "role2"));
        assertNotNull(userTaskInstances);
    }

    // Test findByRoleId method with valid arguments
    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    // Test update method with valid arguments
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = userTaskInstanceRepository.findByCandidateUserId(1L).get();
        Long id = entity.getId();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        updatedEntity.setId(id);
        updatedEntity.setUserTaskId("new_userTaskId");
        updatedEntity.setStatus(FlowNodeStatus.APPROVED);

        int updatedRows = userTaskInstanceRepository.update(updatedEntity);
        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(id).get();

        assertEquals("new_userTaskId", updatedUserTaskInstance.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, updatedUserTaskInstance.getStatus());
    }

    // Test deleteByFlowInstanceId method with valid arguments
    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(0, userTaskInstances.size());
    }

    // Test findByInstanceTypeAndActivityId method with valid arguments
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.PROCESS, "activityId", 1L);
        UserTaskInstanceEntity userTaskInstance = userTaskInstanceOptional.get();
        assertNotNull(userTaskInstance);
    }

    // Test findByInstanceTypeAndName method with valid arguments
    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository
                .findByInstanceTypeAndName(FlowNodeType.PROCESS, "name", 1L);
        UserTaskInstanceEntity userTaskInstance = userTaskInstanceOptional.get();
        assertNotNull(userTaskInstance);
    }

    // Test findConfirmById method with valid arguments
    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertEquals(true, confirm);
    }

    // Test findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid arguments
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    // Test updateUserTaskIdById method with valid arguments
    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "new_userTaskId";
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(id).get();

        assertEquals("new_userTaskId", userTaskInstance.getUserTaskId());
    }

    // Test updateStatusById method with valid arguments
    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        int updatedRows = userTaskInstanceRepository.updateStatusById(id, status);
        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(id).get();

        assertEquals(status, userTaskInstance.getStatus());
    }
}