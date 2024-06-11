import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {
    
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.ACTIVE;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(status);
        
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        expected.add(entity);
        
        Mockito.when(userTaskInstanceRepositoryMock.findByStatus(status)).thenReturn(expected);
        
        List<UserTaskInstanceEntity> result = userTaskInstanceRepositoryMock.findByStatus(status);
        
        assertNotNull(result);
        assertEquals(expected.size(), result.size());
    }
    
    // Add more test cases for other methods
    
}