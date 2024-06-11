package com.oceanbase.odc.service.flow.repository; // change the package name according to your project structure

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.entity.NodeInstanceEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstance1;
    private NodeInstanceEntity nodeInstance2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        nodeInstance1 = new NodeInstanceEntity();
        nodeInstance1.setInstanceId(1L);
        nodeInstance1.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstance1.setFlowInstanceId(1L);
        nodeInstance1.setFlowableElementType(FlowableElementType.TASK);

        nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setInstanceId(2L);
        nodeInstance2.setInstanceType(FlowNodeType.INTERMEDIATE_EVENT);
        nodeInstance2.setFlowInstanceId(1L);
        nodeInstance2.setFlowableElementType(FlowableElementType.GATEWAY);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstance1);
        nodeInstanceEntityRepository.save(nodeInstance1);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance1.getInstanceId(), FlowNodeType.START_EVENT, FlowableElementType.TASK);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_emptyList() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance1.getInstanceId(), FlowNodeType.START_EVENT, FlowableElementType.GATEWAY);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_nullResult() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, FlowNodeType.START_EVENT, FlowableElementType.TASK);

        assertNull(result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        nodeInstanceEntityRepository.save(nodeInstance1);
        nodeInstanceEntityRepository.save(nodeInstance2);

        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstance1, nodeInstance2);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId_emptyList() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByIds() {
        nodeInstanceEntityRepository.save(nodeInstance1);
        nodeInstanceEntityRepository.save(nodeInstance2);

        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstance1, nodeInstance2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds_emptyList() {
        List<Long> ids = Collections.emptyList();
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        nodeInstanceEntityRepository.save(nodeInstance1);

        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, deletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        nodeInstanceEntityRepository.save(nodeInstance1);

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        assertEquals(1, deletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_noMatchingResult() {
        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.START_EVENT);

        assertEquals(0, deletedCount);
    }
}