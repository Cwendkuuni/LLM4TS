//JUnit 4 Test Cases


package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserTaskInstanceRepositoryTest {
    
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertNotNull(resultList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(resultList);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> resultList = 
            userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("admin", "test"));
        assertNotNull(resultList);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> resultList = 
        userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("admin", "test"));
        assertNotNull(resultList);
    }

    //...create similar tests for the other methods

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("testUserTaskId");
        //set other fields...
        int updatedCount = userTaskInstanceRepository.update(entity);
        assertTrue(updatedCount > 0);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedCount = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertTrue(deletedCount > 0);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "testActivityId", 1L);
        assertTrue(entity.isPresent());
    }

}
