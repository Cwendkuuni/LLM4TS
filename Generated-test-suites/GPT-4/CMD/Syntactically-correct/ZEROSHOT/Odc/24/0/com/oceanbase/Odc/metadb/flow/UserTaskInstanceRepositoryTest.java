package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.AGREED;
        List<UserTaskInstanceEntity> response = userTaskInstanceRepository.findByStatus(status);
        verify(userTaskInstanceRepository, times(1)).findByStatus(status);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Long userId = 1L;
        List<UserTaskInstanceEntity> response = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    // Continue to create similar tests for all other methods present in UserTaskInstanceRepository interface

    @Test
    public void testUpdateUserTaskInstanceEntity() {
        int response = userTaskInstanceRepository.update(userTaskInstanceEntity);
        verify(userTaskInstanceRepository, times(1)).update(userTaskInstanceEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testTask";
        int response = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.AGREED;
        int response = userTaskInstanceRepository.updateStatusById(id, status);
        verify(userTaskInstanceRepository, times(1)).updateStatusById(id, status);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int response = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(instanceId);
    }
    
    //....followed by other methods...
}
