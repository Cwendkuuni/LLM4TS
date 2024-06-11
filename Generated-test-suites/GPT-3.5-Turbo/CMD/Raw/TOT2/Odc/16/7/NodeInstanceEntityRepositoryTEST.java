import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE_A;

        List<NodeInstanceEntity> expectedEntities = new ArrayList<>();
        NodeInstanceEntity entity = new NodeInstanceEntity();
        expectedEntities.add(entity);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedEntities, actualEntities);
    }

    // Test for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedEntities = new ArrayList<>();
        NodeInstanceEntity entity = new NodeInstanceEntity();
        expectedEntities.add(entity);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedEntities, actualEntities);
    }

    // Test for findByIds method
    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> expectedEntities = new ArrayList<>();
        NodeInstanceEntity entity = new NodeInstanceEntity();
        expectedEntities.add(entity);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepositoryMock.findByIds(ids);

        assertEquals(expectedEntities, actualEntities);
    }

    // Test for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    // Test for deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(1);

        int result = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }
}