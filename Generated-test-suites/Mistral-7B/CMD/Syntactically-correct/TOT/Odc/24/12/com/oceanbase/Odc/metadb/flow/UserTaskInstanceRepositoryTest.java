package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private static final Long TEST_FLOW_INSTANCE_ID = 1L;
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_ROLE_ID = 1L;
    private static final String TEST_RESOURCE_ROLE_IDENTIFIER = "testRole";

    @Before
    public void setup() {
        // Add test data here if needed
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAITING);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(TEST_USER_ID, Arrays.asList(TEST_ROLE_ID));
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(TEST_USER_ID, Arrays.asList(TEST_RESOURCE_ROLE_IDENTIFIER));
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(TEST_RESOURCE_ROLE_IDENTIFIER));
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(TEST_ROLE_ID);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(TEST_USER_ID);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        // Set other fields here

        int rowsAffected = userTaskInstanceRepository.update(entity);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";

        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(TEST_FLOW_INSTANCE_ID);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", TEST_FLOW_INSTANCE_ID);
        assertTrue(userTaskInstanceEntityOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", TEST_FLOW_INSTANCE_ID);
        assertTrue(userTaskInstanceEntityOptional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertNull(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.WAITING);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }
}