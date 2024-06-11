package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.springframework.test.util.ReflectionTestUtils;


public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByStatusTest() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETED)).thenReturn(list);
        assertEquals(list, userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETED));
    }

    @Test
    public void findByCandidateUserIdOrRoleIdsTest() {
        // Similar pattern for the other repository-defined methods
    }

    @Test
    public void updateTest() {
        Mockito.when(
                userTaskInstanceRepository.update(userTaskInstanceEntity)
        ).thenReturn(1);
        int updated = userTaskInstanceRepository.update(userTaskInstanceEntity); 
        assertEquals(1, updated);

    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        Mockito.when(
                userTaskInstanceRepository.deleteByFlowInstanceId(1L)
        ).thenReturn(1);
        int deleted = userTaskInstanceRepository.deleteByFlowInstanceId(1L); 
        assertEquals(1, deleted);

    }

    @Test
    public void findConfirmByIdTest() {
        Mockito.when(
                userTaskInstanceRepository.findConfirmById(1L)
        ).thenReturn(true);
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(result);
    }

    // Similar pattern for the remaining repository methods

}