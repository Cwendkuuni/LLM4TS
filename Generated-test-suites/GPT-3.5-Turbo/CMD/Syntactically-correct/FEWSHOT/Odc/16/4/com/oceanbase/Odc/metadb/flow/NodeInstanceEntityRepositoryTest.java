package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository instance;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());

        FlowNodeType instanceType = FlowNodeType.INSTANCE_TYPE_1;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE_1;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeList);

        List<NodeInstanceEntity> result = instance.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(nodeList.size(), result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeList);

        List<NodeInstanceEntity> result = instance.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(nodeList.size(), result.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeList);

        List<NodeInstanceEntity> result = instance.findByIds(ids);

        assertNotNull(result);
        assertEquals(nodeList.size(), result.size());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int deleted = instance.deleteByFlowInstanceId(instanceId);

        assertEquals(1, deleted);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.INSTANCE_TYPE_1;

        int deleted = instance.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, deleted);
    }
}