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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.test.builder.UserTaskInstanceEntityBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstance;

    @Before
    public void setup() {
        userTaskInstance = new UserTaskInstanceEntityBuilder().build();
        userTaskInstanceRepository.save(userTaskInstance);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(userTaskInstance, list.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Collections.singleton(1L));
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(userTaskInstance, list.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("testRole"));
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(userTaskInstance, list.get(0));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("testRole"));
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(userTaskInstance, list.get(0));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(userTaskInstance, list.get(0));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(userTaskInstance, list.get(0));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity updatedUserTaskInstance = new UserTaskInstanceEntityBuilder().build();
        int affectedRows = userTaskInstanceRepository.update(updatedUserTaskInstance);
        assertEquals(1, affectedRows);
        assertNotNull(userTaskInstanceRepository.findById(userTaskInstance.getId()).get());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        String newUserTaskId = "newUserTaskId";
        int affectedRows = userTaskInstanceRepository.updateUserTaskIdById(userTaskInstance.getId(), newUserTaskId);
        assertEquals(1, affectedRows);
        assertEquals(newUserTaskId,
                userTaskInstanceRepository.findById(userTaskInstance.getId()).get().getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        FlowNodeStatus newStatus = FlowNodeStatus.REJECTED;
        int affectedRows = userTaskInstanceRepository.updateStatusById(userTaskInstance.getId(), newStatus);
        assertEquals(1, affectedRows);
        assertEquals(newStatus,
                userTaskInstanceRepository.findById(userTaskInstance.getId()).get().getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int affectedRows = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, affectedRows);
        assertNull(userTaskInstanceRepository.findById(userTaskInstance.getId()));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> optional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL,
                "activityId", 1L);
        assertFalse(optional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> optional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL,
                "name", 1L);
        assertFalse(optional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        assertFalse(userTaskInstanceRepository.findConfirmById(userTaskInstance.getId()));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> list = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}