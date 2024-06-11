package com.oceanbase.odc.metadb.flow.test;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(classes = NodeInstanceEntityRepositoryTest.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedResult = Collections.singletonList(nodeInstanceEntity);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        expectedResult.add(nodeInstanceEntity);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> expectedResult = Collections.singletonList(nodeInstanceEntity);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 1;

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 1;

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, result);
    }
}