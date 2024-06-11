import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    NodeInstanceEntityRepository repo;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> mockList = new ArrayList<>();
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        mockList.add(entity1);
        mockList.add(entity2);

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT1;

        Mockito.when(repo.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(mockList);

        List<NodeInstanceEntity> result = repo.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> mockList = new ArrayList<>();
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        mockList.add(entity1);
        mockList.add(entity2);

        Long flowInstanceId = 1L;

        Mockito.when(repo.findByFlowInstanceId(flowInstanceId)).thenReturn(mockList);

        List<NodeInstanceEntity> result = repo.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> mockList = new ArrayList<>();
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        mockList.add(entity1);
        mockList.add(entity2);

        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        Mockito.when(repo.findByIds(ids)).thenReturn(mockList);

        List<NodeInstanceEntity> result = repo.findByIds(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(repo.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = repo.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;

        Mockito.when(repo.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = repo.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }

}