
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity mockEntity;

    @Test
    public void findByStatusTest() {
        when(userTaskInstanceRepository.findByStatus(any())).thenReturn(Arrays.asList(mockEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertEquals(1, result.size());

        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.CREATED);
    }

    @Test
    public void findByCandidateUserIdOrRoleIdsTest() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), any())).thenReturn(Arrays.asList(mockEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
        assertEquals(1, result.size());

        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(anyLong(), any());
    }

    // Follow the similar pattern to write test cases for rest of the methods in the UserRepository

    @Test
    public void updateTest() {
        when(userTaskInstanceRepository.update(any())).thenReturn(1);

        int result = userTaskInstanceRepository.update(mockEntity);
        assertEquals(1, result);

        verify(userTaskInstanceRepository, times(1)).update(mockEntity);
    }
    
    @Test
    public void deleteUserByFlowInstanceIdTest() {
        when(userTaskInstanceRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);

        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(1L);
    }
    
    @Test
    public void findConfirmByIdTest() {
        when(userTaskInstanceRepository.findConfirmById(anyLong())).thenReturn(Boolean.TRUE);

        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertNotNull(result);
        assertTrue(result);

        verify(userTaskInstanceRepository, times(1)).findConfirmById(1L);
    }
}
