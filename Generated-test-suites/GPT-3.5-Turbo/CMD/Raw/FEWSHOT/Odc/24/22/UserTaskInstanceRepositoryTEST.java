// Assuming there is a UserTaskInstanceEntity class for UserTaskInstanceRepository class

package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.ACTIVE);
        assertEquals(1, userTaskInstanceEntities.size());
        assertEquals(FlowNodeStatus.ACTIVE, userTaskInstanceEntities.get(0).getStatus());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());
        assertNotNull(userTaskInstanceEntities);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());
        assertNotNull(userTaskInstanceEntities);
    }

    // Add test cases for remaining methods in UserTaskInstanceRepository class
    
    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.ACTIVITY, "activity123", 1L);
        assertTrue(userTaskInstanceEntity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TASK, "task123", 1L);
        assertTrue(userTaskInstanceEntity.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertNotNull(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(new ArrayList<>(), FlowNodeStatus.PENDING);
        assertNotNull(userTaskInstanceEntities);
    }
}