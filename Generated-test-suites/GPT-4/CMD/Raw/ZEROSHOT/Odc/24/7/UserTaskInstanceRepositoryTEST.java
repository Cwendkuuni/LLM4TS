
package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.Optional;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        userTaskInstanceRepository.findByStatus(FlowNodeStatus.INIT);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.INIT);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
    }

    @Test
    public void testUpdate() {
        userTaskInstanceRepository.update(userTaskInstanceEntity);
        verify(userTaskInstanceRepository, times(1)).update(userTaskInstanceEntity);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.INIT);
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.INIT);
    }

    //... similar tests for other methods
}
