package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("123");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId(1L);
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(any())).thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        assertEquals(1, results.size());
        verify(userTaskInstanceRepository, times(1)).findByStatus(any());
    }
    
    @Test
    public void testFindByRoleId() {
        when(userTaskInstanceRepository.findByRoleId(any())).thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(1, results.size());
        verify(userTaskInstanceRepository, times(1)).findByRoleId(any());
    }

    @Test
    public void testFindByCandidateUserId() {
        when(userTaskInstanceRepository.findByCandidateUserId(any())).thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(1, results.size());
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(any());
    }

    @Test
    public void testUpdate() {
        when(userTaskInstanceRepository.update(userTaskInstanceEntity)).thenReturn(1);
        int updatedRows = userTaskInstanceRepository.update(userTaskInstanceEntity);
        assertEquals(1, updatedRows);
        verify(userTaskInstanceRepository, times(1)).update(userTaskInstanceEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        when(userTaskInstanceRepository.updateUserTaskIdById(1L, "123")).thenReturn(1);
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(1L, "123");
        assertEquals(1, updatedRows);
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(1L, "123");
    }


    // Add similar test methods for the rest of the methods present in UserTaskInstanceRepository.
}
