import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                      .thenReturn(expectedResult);
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        
        assertNotNull(result);
        assertEquals(expectedResult.size(), result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                      .thenReturn(expectedResult);
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(flowInstanceId);
        
        assertNotNull(result);
        assertEquals(expectedResult.size(), result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        
        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                      .thenReturn(expectedResult);
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByIds(ids);
        
        assertNotNull(result);
        assertEquals(expectedResult.size(), result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
                      .thenReturn(1);
        
        int result = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(instanceId);
        
        assertEquals(1, result);

    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                      .thenReturn(1);
        
        int result = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        
        assertEquals(1, result);

    }

}