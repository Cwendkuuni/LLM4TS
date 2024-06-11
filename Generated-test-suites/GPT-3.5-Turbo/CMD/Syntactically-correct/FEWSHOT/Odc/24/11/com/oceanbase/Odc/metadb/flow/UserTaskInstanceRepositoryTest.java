package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Mock
    UserTaskInstanceEntity mockEntity;

    @InjectMocks
    UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.ACTIVE);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L, 3L));
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role1"));
        assertNotNull(result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(Collections.singletonList("role1"));
        assertNotNull(result);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        mockEntity.setUserTaskId("test");
        mockEntity.setStatus(FlowNodeStatus.COMPLETED);
        int result = repository.update(mockEntity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = repository.updateUserTaskIdById(1L, "test");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = repository.updateStatusById(1L, FlowNodeStatus.PENDING);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = repository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity result = repository.findByInstanceTypeAndActivityId(FlowNodeType.SINGLE, "activity1", 1L).orElse(null);
        assertNotNull(result);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity result = repository.findByInstanceTypeAndName(FlowNodeType.SINGLE, "name1", 1L).orElse(null);
        assertNotNull(result);
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = repository.findConfirmById(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.ACTIVE);
        assertNotNull(result);
    }
}