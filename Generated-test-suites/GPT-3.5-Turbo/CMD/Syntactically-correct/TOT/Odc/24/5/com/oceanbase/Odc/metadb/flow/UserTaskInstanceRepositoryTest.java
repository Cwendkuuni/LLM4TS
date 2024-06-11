import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceService userTaskInstanceService;

    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        
        when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expected);
        
        List<UserTaskInstanceEntity> actual = userTaskInstanceService.findByStatus(status);
        
        verify(userTaskInstanceRepository).findByStatus(status);
        assertEquals(expected, actual);
    }

    // Add test cases for other methods

}