package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {
 
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity testEntity;

    @Before
    public void setup() {
        testEntity = new UserTaskInstanceEntity();
        testEntity.setId(1L);
        testEntity.setFlowNodeId(1L);
        testEntity.setUserTaskId("TestUserTask");
        testEntity.setStatus(FlowNodeStatus.WAITING_APPROVAL);
        // set other properties as needed
        testEntity = userTaskInstanceRepository.save(testEntity);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.WAITING_APPROVAL;
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(status);
        assertNotNull(entities);
        // ... Add assertions to check entities
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(entities);
        // ... Add assertions to check entities
    }

    // Similarly, you can add tests cases for other methods of repository
    @Test
    public void testUpdateUserTaskIdById() {
        int updated = userTaskInstanceRepository.updateUserTaskIdById(testEntity.getId(), "NewUserTask");
        assertEquals(1, updated);
        Optional<UserTaskInstanceEntity> updatedEntity = userTaskInstanceRepository.findById(testEntity.getId());
        assertEquals("NewUserTask", updatedEntity.get().getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        FlowNodeStatus newStatus = FlowNodeStatus.APPROVED;
        int updated = userTaskInstanceRepository.updateStatusById(testEntity.getId(), newStatus);
        assertEquals(1, updated);
        Optional<UserTaskInstanceEntity> updatedEntity = userTaskInstanceRepository.findById(testEntity.getId());
        assertEquals(newStatus, updatedEntity.get().getStatus());
    }

    //... Similar tests for other methods
}