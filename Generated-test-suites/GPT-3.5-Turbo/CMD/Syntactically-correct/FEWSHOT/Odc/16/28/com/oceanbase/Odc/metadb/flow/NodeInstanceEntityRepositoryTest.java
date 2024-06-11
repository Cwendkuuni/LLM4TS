import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    private NodeInstanceEntityRepository repositoryMock;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> entityList = Arrays.asList(entity1, entity2);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(entityList);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 2L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> entityList = Arrays.asList(entity1, entity2);

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(entityList);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> entityList = Arrays.asList(entity1, entity2);

        when(repository.findByIds(ids)).thenReturn(entityList);

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int affectedRows = 2;

        when(repositoryMock.deleteByFlowInstanceId(instanceId)).thenReturn(affectedRows);

        int result = repositoryMock.deleteByFlowInstanceId(instanceId);

        assertEquals(2, result);
        verify(repositoryMock).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.END_EVENT;

        int affectedRows = 1;

        when(repositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(affectedRows);

        int result = repositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
        verify(repositoryMock).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}