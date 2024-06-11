package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private Query query;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE1;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(nodeInstanceEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(nodeInstanceEntities);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(nodeInstanceEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(0, result); // Assuming 0 as a success result for deletion
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, result); // Assuming 0 as a success result for deletion
    }
}