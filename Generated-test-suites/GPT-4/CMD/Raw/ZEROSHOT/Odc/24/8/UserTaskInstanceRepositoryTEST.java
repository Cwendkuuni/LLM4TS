
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceEntityRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        when(userTaskInstanceRepository.findByStatus(any())).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByStatus(FlowNodeStatus.valueOf("Active"));
        assertEquals(entities, result);
    }

    @Test
    public void testFindByIdCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), any())).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());
        assertEquals(entities, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), any())).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());
        assertEquals(entities, result);
    }

    // Do similar for remaining methods...

    @Test
    public void testUpdate() {
        when(userTaskInstanceRepository.update(any())).thenReturn(1);

        int result = userTaskInstanceService.update(new UserTaskInstanceEntity());
        assertEquals(1, result);
    }

    @Test
    public void testFindConfirmById() {
        when(userTaskInstanceRepository.findConfirmById(anyLong())).thenReturn(true);

        Boolean result = userTaskInstanceService.findConfirmById(1L);
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(any(), any())).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceService.findApprovalInstanceIdByFlowInstanceIdAndStatus(new ArrayList<>(), FlowNodeStatus.valueOf("Active"));
        assertEquals(entities, result);
    }
}
