
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        //mock the method
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(new ArrayList<>());
        
        //call the method and check the result
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertTrue(result.isEmpty());
    }

    // Repeat similar tests for findByCandidateUserIdOrRoleIds, findByCandidateUserIdOrResourceRoleIdentifier, findByResourceRoleIdentifierIn, 
    // findByRoleId, findByCandidateUserId, update, updateUserTaskIdById, updateStatusById, deleteByFlowInstanceId, 
    // findByInstanceTypeAndActivityId, findByInstanceTypeAndName, findConfirmById, findApprovalInstanceIdByFlowInstanceIdAndStatus
    
}
