package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

import static org.mockito.Mockito.*;

@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceEntity entity;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.READY;
        userTaskInstanceRepository.findByStatus(status);
        verify(userTaskInstanceRepository, times(1)).findByStatus(status);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = List.of(1L);
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    //...Add more test methods here for remaining methods in UserTaskInstanceRepository...

    @Test
    public void testUpdate() {
        int res = userTaskInstanceRepository.update(entity);
        verify(userTaskInstanceRepository, times(1)).update(entity);
        assertEquals(1, res);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "taskId123";
        int res = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(id, userTaskId);
        assertEquals(1, res);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int res = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(instanceId);
        assertEquals(1, res);
    }

    //...Continue adding test cases for all other methods...//
}
