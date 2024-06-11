import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

class NodeInstanceEntityRepositoryTest {

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType));
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expected);

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actual.size() > 0);
        assertNotNull(actual.get(0).getInstanceId());
        assertNotNull(actual.get(0).getInstanceType());
        assertNotNull(actual.get(0).getFlowableElementType());
        assertFalse(actual.contains(null));

        Mockito.verify(repository, Mockito.times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullParameters() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Long instanceId = null;
        FlowableElementType flowableElementType = null;
        FlowNodeType instanceType = null;

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyList() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NonExistingInstanceId() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Long instanceId = -1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NonExistingFlowableElementType() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Long instanceId = 1L;
        FlowableElementType flowableElementType = null;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NonExistingInstanceType() {
        NodeInstanceEntityRepository repository = Mockito.mock(NodeInstanceEntityRepository.class);
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = null;

        List<NodeInstanceEntity> actual = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actual.isEmpty());
    }
}