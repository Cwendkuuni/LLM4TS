
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTests {
    @Mock
    private NodeInstanceEntityRepository repository;
    @Mock
    private EntityManager em;
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setFlowInstanceId(2L);
        nodeInstanceEntity.setInstanceId(3L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.SEQUENCE_FLOW);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(3L, FlowNodeType.PROCESS, FlowableElementType.SEQUENCE_FLOW)).thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(3L, FlowNodeType.PROCESS, FlowableElementType.SEQUENCE_FLOW);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(FlowNodeType.PROCESS, result.get(0).getInstanceType());
        assertEquals(FlowableElementType.SEQUENCE_FLOW, result.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Mockito.when(repository.findByFlowInstanceId(2L)).thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(2L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getFlowInstanceId().longValue());
    }

    @Test
    public void testFindByIds() {
        List ids = Arrays.asList(1L, 2L, 3L);
        Mockito.when(repository.findByIds(ids)).thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(repository.deleteByFlowInstanceId(2L)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(2L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(repository.deleteByInstanceIdAndInstanceType(3L, FlowNodeType.PROCESS)).thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(3L, FlowNodeType.PROCESS);

        assertEquals(1, result);
    }
}
