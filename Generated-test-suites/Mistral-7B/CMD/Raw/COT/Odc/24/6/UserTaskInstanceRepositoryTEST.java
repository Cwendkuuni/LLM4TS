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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test findByStatus
    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstanceEntities);
        assertEquals(0, userTaskInstanceEntities.size());

        // Add test data
        UserTaskInstanceEntity testData1 = new UserTaskInstanceEntity();
        testData1.setStatus(FlowNodeStatus.APPROVED);
        UserTaskInstanceEntity testData2 = new UserTaskInstanceEntity();
        testData2.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstanceRepository.saveAll(Arrays.asList(testData1, testData2));

        userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstanceEntities);
        assertEquals(1, userTaskInstanceEntities.size());
        assertEquals(testData1.getId(), userTaskInstanceEntities.get(0).getId());

        userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED);
        assertNotNull(userTaskInstanceEntities);
        assertEquals(1, userTaskInstanceEntities.size());
        assertEquals(testData2.getId(), userTaskInstanceEntities.get(0).getId());
    }

    // Test findByCandidateUserIdOrRoleIds
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity testData1 = new UserTaskInstanceEntity();
        testData1.setUserId(1L);
        UserTaskInstanceEntity testData2 = new UserTaskInstanceEntity();
        testData2.setUserId(1L);

        UserTaskInstanceEntity testData3 = new UserTaskInstanceEntity();
        testData3.setRoleId(1L);
        UserTaskInstanceEntity testData4 = new UserTaskInstanceEntity();
        testData4.setRoleId(1L);

        UserTaskInstanceEntity testData5 = new UserTaskInstanceEntity();
        testData5.setUserId(2L);
        UserTaskInstanceEntity testData6 = new UserTaskInstanceEntity();
        testData6.setRoleId(2L);

        userTaskInstanceRepository.saveAll(Arrays.asList(testData1, testData2, testData3, testData4, testData5, testData6));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(testData1.getId(), result.get(0).getId());
        assertEquals(testData2.getId(), result.get(1).getId());
        assertEquals(testData3.getId(), result.get(2).getId());

        result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(2L, Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testData5.getId(), result.get(0).getId());
        assertEquals(testData6.getId(), result.get(1).getId());
    }

    // Test findByResourceRoleIdentifier
    @Test
    public void testFindByResourceRoleIdentifier() {
        UserTaskInstanceEntity testData1 = new UserTaskInstanceEntity();
        testData1.setResourceRoleIdentifier("Role1");
        UserTaskInstanceEntity testData2 = new UserTaskInstanceEntity();
        testData2.setResourceRoleIdentifier("Role2");

        userTaskInstanceRepository.saveAll(Arrays.asList(testData1, testData2));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("Role1", "Role2"));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testData1.getId(), result.get(0).getId());
        assertEquals(testData2.getId(), result.get(1).getId());
    }

    // Test findByRoleId
    @Test
    public void testFindByRoleId() {
        UserTaskInstanceEntity testData1 = new UserTaskInstanceEntity();
        testData1.setRoleId(1L);
        UserTaskInstanceEntity testData2 = new UserTaskInstanceEntity();
        testData2.setRoleId(1L);

        UserTaskInstanceEntity testData3 = new UserTaskInstanceEntity();
        testData3.setRoleId(2L);

        userTaskInstanceRepository.saveAll(Arrays.asList(testData1, testData2, testData3));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testData1.getId(), result.get(0).getId());
        assertEquals(testData2.getId(), result.get(1).getId());
    }

    // Test findByCandidateUserId
    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceEntity testData1 = new UserTaskInstanceEntity();
        testData1.setUserId(1L);
        UserTaskInstanceEntity testData2 = new UserTaskInstanceEntity();
        testData2.setUserId(1L);

        userTaskInstanceRepository.saveAll(Arrays.asList(testData1, testData2));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testData1.getId(), result.get(0).getId());
        assertEquals(testData2.getId(), result.get(1).getId());
    }

    // Test update
    @Test
    public void testUpdate() {
        UserTaskInstanceEntity testData = new UserTaskInstanceEntity();
        testData.setId(1L);

        UserTaskInstanceEntity updatedTestData = new UserTaskInstanceEntity();
        updatedTestData.setId(1L);
        updatedTestData.setUserTaskId("UserTaskId1");
        updatedTestData.setStatus(FlowNodeStatus.APPROVED);
        updatedTestData.setApproved(true);
        updatedTestData.setOperatorId(1L);
        updatedTestData.setComment("Comment1");
        updatedTestData.setExpireIntervalSeconds(60);
        updatedTestData.setExternalFlowInstanceId("ExternalInstanceId1");

        int rowsAffected = userTaskInstanceRepository.update(updatedTestData);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity result = userTaskInstanceRepository.findById(1L).get();
        assertEquals("UserTaskId1", result.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, result.getStatus());
        assertEquals(true, result.isApproved());
        assertEquals(1L, result.getOperatorId().longValue());
        assertEquals("Comment1", result.getComment());
        assertEquals(60, result.getExpireIntervalSeconds().intValue());
        assertEquals("ExternalInstanceId1", result.getExternalFlowInstanceId());
    }

    // Test updateUserTaskIdById
    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity testData = new UserTaskInstanceEntity();
        testData.setId(1L);
        testData.setUserTaskId("UserTaskId1");

        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "UserTaskId2");
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity result = userTaskInstanceRepository.findById(1L).get();
        assertEquals("UserTaskId2", result.getUserTaskId());
    }

    // Test updateStatusById
    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity testData = new UserTaskInstanceEntity();
        testData.setId(1L);
        testData.setStatus(FlowNodeStatus.APPROVED);

        int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity result = userTaskInstanceRepository.findById(1L).get();
        assertEquals(FlowNodeStatus.REJECTED, result.getStatus());
    }

    // Test deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity testData1 = new UserTaskInstanceEntity();
        testData1.setFlowInstanceId(1L);

        UserTaskInstanceEntity testData2 = new UserTaskInstanceEntity();
        testData2.setFlowInstanceId(1L);

        userTaskInstanceRepository.saveAll(Arrays.asList(testData1, testData2));

        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, rowsAffected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByFlowInstanceId(1L);
        assertNull(result);
    }

    // Test findByInstanceTypeAndActivityId
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity testData = new UserTaskInstanceEntity();
        testData.setId(1L);
        testData.setFlowInstanceId(1L);
        testData.setInstanceType(FlowNodeType.APPROVAL);
        testData.setActivityId("ActivityId1");

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "ActivityId1", 1L);
        assertNotNull(result);
        assertEquals(testData.getId(), result.get().getId());
    }

    // Test findByInstanceTypeAndName
    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity testData = new UserTaskInstanceEntity();
        testData.setId(1L);
        testData.setFlowInstanceId(1L);
        testData.setInstanceType(FlowNodeType.APPROVAL);
        testData.setName("Name1");

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "Name1", 1L);
        assertNotNull(result);
        assertEquals(testData.getId(), result.get().getId());
    }

    // Test findConfirmById
    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity testData = new UserTaskInstanceEntity();
        testData.setId(1L);
        testData.setConfirm(true);

        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertNotNull(result);
        assertEquals(true, result);
    }

    // Test findApprovalInstanceIdByFlowInstanceIdAndStatus
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceEntity testData1 = new UserTaskInstanceEntity();
        testData1.setFlowInstanceId(1L);
        testData1.setStatus(FlowNodeStatus.APPROVED);

        UserTaskInstanceEntity testData2 = new UserTaskInstanceEntity();
        testData2.setFlowInstanceId(1L);
        testData2.setStatus(FlowNodeStatus.REJECTED);

        userTaskInstanceRepository.saveAll(Arrays.asList(testData1, testData2));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.APPROVED);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testData1.getId(), result.get(0).getId());

        result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), FlowNodeStatus.REJECTED);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}