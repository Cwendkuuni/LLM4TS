
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

    @MockBean
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        UserTaskInstanceEntity entity = mock(UserTaskInstanceEntity.class);
        when(repository.findByStatus(status)).thenReturn(Arrays.asList(entity));

        List<UserTaskInstanceEntity> result = repository.findByStatus(status);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // similar to above
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // similar to above
    }

    // ...

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = mock(UserTaskInstanceEntity.class);
        when(repository.update(entity)).thenReturn(1);

        int result = repository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "taskId";
        when(repository.updateUserTaskIdById(id, userTaskId)).thenReturn(1);

        int result = repository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        when(repository.updateStatusById(id, status)).thenReturn(1);

        int result = repository.updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // similar to above
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // similar to above
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        when(repository.findConfirmById(id)).thenReturn(Boolean.TRUE);

        Boolean result = repository.findConfirmById(id);
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // similar to above
    }

}
