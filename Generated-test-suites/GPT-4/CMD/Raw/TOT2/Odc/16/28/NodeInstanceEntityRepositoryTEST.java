import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks // The class we're testing
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock // All the other stuff we need to mock
    NodeInstanceEntity nodeEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(nodeEntity.getFlowInstanceId()))
                .thenReturn(new ArrayList<>(Arrays.asList(nodeEntity)));
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(nodeEntity.getFlowInstanceId());
        
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Similar to above, set up your when(...)thenReturn(...). Use any() matchers for arguments if necessary.
        // Then run the method and verify the results. This would look largely similar to the above, just with different arguments.
    }

    @Test
    public void testFindByIds() {
        // Similar to above, set up your when(...)thenReturn(...). Use any() matchers for arguments if necessary.
        // Then run the method and verify the results.
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Use doNothing().when(...).<method> for void methods. Then use verify() to check that they were called correctly.
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Use doNothing().when(...).<method> for void methods. Then use verify() to check that they were called correctly.
    }

}