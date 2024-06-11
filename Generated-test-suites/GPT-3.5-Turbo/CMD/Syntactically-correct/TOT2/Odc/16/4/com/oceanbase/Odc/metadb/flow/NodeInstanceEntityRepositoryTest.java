import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.NODE;

        List<NodeInstanceEntity> mockedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.NODE);
        mockedResult.add(nodeInstanceEntity1);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(mockedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryImpl.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getId() == 1L);
    }

    // Additional test cases can be added for other methods

}