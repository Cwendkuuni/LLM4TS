import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
// import other mockito classes

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository repository;
    
    // Mock all other objects used in the repository
    NodeInstanceEntity mockEntity;
        
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockEntity = mock(NodeInstanceEntity.class); // You should create a real object or a more suitable mock object.
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(mockEntity);

        Long instanceId = 1L;
        FlowNodeType instanceType = new FlowNodeType(); //Initialize correctly
        FlowableElementType flowableElementType = new FlowableElementType(); //Initialize correctly

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(eq(instanceId), eq(instanceType), eq(flowableElementType)))
            .thenReturn(expectedEntities);
    }
    
    
    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        Long instanceId = 1L;
        FlowNodeType instanceType = new FlowNodeType(); //Initialize correctly
        FlowableElementType flowableElementType = new FlowableElementType(); //Initialize correctly

        List<NodeInstanceEntity> actualEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(mockEntity, actualEntities.get(0)); // Assert according to your needs
    }

    // Other test methods...
}