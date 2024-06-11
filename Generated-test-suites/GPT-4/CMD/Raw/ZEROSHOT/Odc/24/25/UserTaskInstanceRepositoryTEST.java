
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest{

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity userTaskInstanceEntity;

    FlowNodeStatus flowNodeStatus;
    FlowNodeType flowNodeType;

    @Before
    public void setup() {
        flowNodeStatus = FlowNodeStatus.CREATED;
        flowNodeType = FlowNodeType.USER_TASK;
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> list = Arrays.asList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByStatus(flowNodeStatus)).thenReturn(list);
        userTaskInstanceRepository.findByStatus(flowNodeStatus);
        verify(userTaskInstanceRepository).findByStatus(flowNodeStatus);
    }

    // You can extend the tests for all the methods in the UserTaskInstanceRepository in a similar manner. 

    // For methods that returns an int which signifies the number of rows affected, you can use the below approach

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        when(userTaskInstanceRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
    }

    // Similarly, you may write remaining tests for all methods

}
