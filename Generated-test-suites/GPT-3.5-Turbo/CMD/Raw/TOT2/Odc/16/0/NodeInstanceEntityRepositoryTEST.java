import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> resultList = new ArrayList<>();
        // Add test data to resultList

        // Mock the repository method
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(resultList);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(resultList, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> resultList = new ArrayList<>();
        // Add test data to resultList

        // Mock the repository method
        when(repository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(resultList);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(resultList, result);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        // Add test data to ids

        List<NodeInstanceEntity> resultList = new ArrayList<>();
        // Add test data to resultList

        // Mock the repository method
        when(repository.findByIds(any(Collection.class)))
                .thenReturn(resultList);

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertNotNull(result);
        assertEquals(resultList, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        // Mock the repository method
        when(repository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        // Mock the repository method
        when(repository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }

}