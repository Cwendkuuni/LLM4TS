
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTaskInstanceRepositoryTest {

    @Autowired
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("test");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(result);
    }

    // Add more test cases based on the methods in UserTaskInstanceRepository class

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        //set entity attributes here
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
    }
    
    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus(){
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertNotNull(result);
     }
    
     @Test
     public void testFindConfirmById(){
        Long id = 1L;
        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertTrue(result);
     }
}
