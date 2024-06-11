import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE_A;

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        expectedList.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepositoryMock
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 2L;

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        expectedList.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepositoryMock.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        expectedList.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepositoryMock.findByIds(ids)).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(flowInstanceId)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertTrue(result == 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        Mockito.when(nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertTrue(result == 1);
    }
}