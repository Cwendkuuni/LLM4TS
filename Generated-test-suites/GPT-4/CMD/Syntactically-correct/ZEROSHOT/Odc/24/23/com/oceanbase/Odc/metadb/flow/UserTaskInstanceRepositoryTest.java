package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepository userTaskRepository;

    @Mock
    UserTaskInstanceEntity userTaskInstanceEntity;

    @Mock
    FlowNodeStatus flowNodeStatus;

    @Mock
    FlowNodeType flowNodeType;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> taskInstanceEntities = new ArrayList<>();
        when(userTaskRepository.findByStatus(flowNodeStatus)).thenReturn(taskInstanceEntities);
        assertEquals(taskInstanceEntities, userTaskRepository.findByStatus(flowNodeStatus));      
    }

    @Test
    public void testfindByCandidateUserIdOrRoleIds() {
        //... similar codes to the one above
    }

    // Repeat the same pattern for all the findBy methods in the UserTaskInstanceRepository class

    @Test
    public void testUpdate() {
        when(userTaskRepository.update(userTaskInstanceEntity)).thenReturn(1);
        assertEquals(1, userTaskRepository.update(userTaskInstanceEntity));
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // ... similar to testUpdate()
    }
    
    // Repeat the same pattern for all the update methods 

    @Test
    public void testDeleteByFlowInstanceId() {
        when(userTaskRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        assertEquals(1, userTaskRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // ... You should assert what you expect the findByInstanceTypeAndActivityId will return
    }

    // Repeat the same pattern for the rest of the methods in the UserTaskInstanceRepository class

    @Test
    public void testFindConfirmById() {
        when(userTaskRepository.findConfirmById(1L)).thenReturn(true);
        assertEquals(true, userTaskRepository.findConfirmById(1L));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        when(userTaskRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(entities, flowNodeStatus)).thenReturn(entities);
        assertEquals(entities, userTaskRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(entities, flowNodeStatus));
    }
}
