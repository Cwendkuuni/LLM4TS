import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> expectedList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> expectedList = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        int expectedDeletedCount = 2;

        Mockito.when(nodeInstanceEntityRepository.deleteByflowInstanceId(flowInstanceId))
                .thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByflowInstanceId(flowInstanceId);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        int expectedDeletedCount = 2;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

}