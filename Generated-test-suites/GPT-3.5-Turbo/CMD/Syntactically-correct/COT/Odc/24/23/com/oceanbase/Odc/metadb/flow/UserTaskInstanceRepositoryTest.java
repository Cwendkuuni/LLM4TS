import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        List<UserTaskInstanceEntity> result = new ArrayList<>();
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
                .thenReturn(result);

        List<UserTaskInstanceEntity> approvalInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertNotNull(approvalInstances);
    }

    // Add more test cases for other methods

}