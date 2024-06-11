import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Test
    public void testFindByStatus() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        List<UserTaskInstanceEntity> result = Arrays.asList(new UserTaskInstanceEntity());
        
        Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(result);
        
        List<UserTaskInstanceEntity> found = userTaskInstanceRepository.findByStatus(status);
        
        assertNotNull(found);
        assertEquals(result.size(), found.size());
    }
    
    // Add more test cases for other methods

}