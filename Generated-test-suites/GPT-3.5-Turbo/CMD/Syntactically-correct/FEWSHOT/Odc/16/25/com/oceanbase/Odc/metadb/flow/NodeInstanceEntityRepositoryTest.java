package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @MockBean
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE1;

        Long instanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        // Set entity properties
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(nodeInstanceEntity);

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualEntities);
        assertEquals(1, actualEntities.size());
        assertEquals(nodeInstanceEntity, actualEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        // Set entity properties
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(nodeInstanceEntity);

        Mockito.when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualEntities);
        assertEquals(1, actualEntities.size());
        assertEquals(nodeInstanceEntity, actualEntities.get(0));
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        // Set entity properties
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(nodeInstanceEntity);

        Mockito.when(repository.findByIds(ids)).thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByIds(ids);

        assertNotNull(actualEntities);
        assertEquals(1, actualEntities.size());
        assertEquals(nodeInstanceEntity, actualEntities.get(0));
    }

}