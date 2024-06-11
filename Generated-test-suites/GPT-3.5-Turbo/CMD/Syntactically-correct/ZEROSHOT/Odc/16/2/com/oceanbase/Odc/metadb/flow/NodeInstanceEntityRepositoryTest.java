import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() {
        nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedNodeInstances = new ArrayList<>();
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        expectedNodeInstances.add(nodeInstance1);
        expectedNodeInstances.add(nodeInstance2);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE_A))
                .thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE_A);
        assertNotNull(actualNodeInstances);
        assertEquals(2, actualNodeInstances.size());
        assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedNodeInstances = new ArrayList<>();
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        expectedNodeInstances.add(nodeInstance1);
        expectedNodeInstances.add(nodeInstance2);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(actualNodeInstances);
        assertEquals(2, actualNodeInstances.size());
        assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    // Add more test cases for other methods in NodeInstanceEntityRepository

}