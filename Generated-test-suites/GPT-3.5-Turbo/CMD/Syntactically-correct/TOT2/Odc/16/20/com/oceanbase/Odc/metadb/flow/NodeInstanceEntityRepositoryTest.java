import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

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
        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE1;

        Mockito.when(nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(Arrays.asList(new NodeInstanceEntity()));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceEntityRepositoryMock.findByFlowInstanceId(flowInstanceId))
                .thenReturn(Arrays.asList(new NodeInstanceEntity()));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);

        Mockito.when(nodeInstanceEntityRepositoryMock.findByIds(ids)).thenReturn(Arrays.asList(new NodeInstanceEntity()));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepositoryMock.findByIds(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", nodeInstanceEntityRepositoryMock);

        Mockito.when(nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;

        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", nodeInstanceEntityRepositoryMock);

        Mockito.when(nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }

}