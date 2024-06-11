import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> resultList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        resultList.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(resultList);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepositoryImpl.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualResult);
        assertEquals(resultList.size(), actualResult.size());
        assertEquals(resultList.get(0), actualResult.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> resultList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        resultList.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(resultList);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepositoryImpl.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualResult);
        assertEquals(resultList.size(), actualResult.size());
        assertEquals(resultList.get(0), actualResult.get(0));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);

        List<NodeInstanceEntity> resultList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        resultList.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(resultList);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepositoryImpl.findByIds(ids);

        assertNotNull(actualResult);
        assertEquals(resultList.size(), actualResult.size());
        assertEquals(resultList.get(0), actualResult.get(0));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int deleted = nodeInstanceEntityRepositoryImpl.deleteByFlowInstanceId(instanceId);

        assertEquals(1, deleted);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int deleted = nodeInstanceEntityRepositoryImpl.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, deleted);
    }
}