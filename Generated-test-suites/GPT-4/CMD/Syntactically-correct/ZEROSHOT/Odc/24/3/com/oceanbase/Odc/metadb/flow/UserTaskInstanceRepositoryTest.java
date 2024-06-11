package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // More tests for the other methods following the same structure
    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Do setup and mock call here
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
       // Do setup and mock call here
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
       // Do setup and mock call here
    }

    @Test
    public void testFindByRoleId() {
        // Do setup and mock call here
    }

    @Test
    public void testfindByCandidateUserId() {
        // Do setup and mock call here
    }

    @Test
    public void testUpdate() {
        // Do setup and mock call here
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Do setup and mock call here
    }

    @Test
    public void testUpdateStatusById() {
        // Do setup and mock call here
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Do setup and mock call here
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Do setup and mock call here
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Do setup and mock call here
    }

    @Test
    public void testFindConfirmById() {
        // Do setup and mock call here
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Do setup and mock call here
    }
}
