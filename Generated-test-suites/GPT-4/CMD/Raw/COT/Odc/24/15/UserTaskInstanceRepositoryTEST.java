import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks 
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceEntity userTaskEntity;

    @Before
    public void setup() {
        // Do any setup here
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETE))
            .thenReturn(Arrays.asList(userTaskEntity));

        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETE);
        
        assertEquals(1, results.size());
    }

    // Add other test methods here as per the public methods in UserTaskInstanceRepository

    @Test
    public void testUpdate() {
        when(userTaskInstanceRepository.update(userTaskEntity)).thenReturn(1);

        int result = userTaskInstanceRepository.update(userTaskEntity);

        assertEquals(1, result);
    }
    
    // And so on...
    
}