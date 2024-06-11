
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepositoryImpl;

    @Test
    public void testFindByStatus() {
        // Mock data
        List<UserTaskInstanceEntity> instanceList = new ArrayList<>();
        UserTaskInstanceEntity instance1 = new UserTaskInstanceEntity();
        instance1.setStatus(FlowNodeStatus.PENDING);
        instanceList.add(instance1);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(instanceList);

        // Test
        List<UserTaskInstanceEntity> resultList = userTaskInstanceRepositoryImpl.findByStatus(FlowNodeStatus.PENDING);
        assertEquals(1, resultList.size());
        assertEquals(FlowNodeStatus.PENDING, resultList.get(0).getStatus());
    }

    // Additional test cases can be added for other public methods

}
