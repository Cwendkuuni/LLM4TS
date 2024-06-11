import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_A;

        List<NodeInstanceEntity> expectedResults = Arrays.asList(
            new NodeInstanceEntity(1L, 1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A),
            new NodeInstanceEntity(2L, 1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A)
        );

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, instanceType, flowableElementType))
                .thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, instanceType, flowableElementType);

        assertEquals(expectedResults.size(), actualResults.size());
        assertEquals(expectedResults, actualResults);
    }

    // Test for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedResults = Arrays.asList(
            new NodeInstanceEntity(1L, 1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A),
            new NodeInstanceEntity(2L, 1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_B)
        );

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = repository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedResults.size(), actualResults.size());
        assertEquals(expectedResults, actualResults);
    }

    // Test for findByIds method
    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> expectedResults = Arrays.asList(
            new NodeInstanceEntity(1L, 1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A),
            new NodeInstanceEntity(2L, 1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_B),
            new NodeInstanceEntity(3L, 1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_C)
        );

        when(repository.findByIds(ids)).thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = repository.findByIds(ids);

        assertEquals(expectedResults.size(), actualResults.size());
        assertEquals(expectedResults, actualResults);
    }

    // Test for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int rowsAffected = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, rowsAffected);
    }

    // Test for deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        when(repository.deleteByInstanceIdAndInstanceType(1L, instanceType)).thenReturn(1);

        int rowsAffected = repository.deleteByInstanceIdAndInstanceType(1L, instanceType);

        assertEquals(1, rowsAffected);
    }
}