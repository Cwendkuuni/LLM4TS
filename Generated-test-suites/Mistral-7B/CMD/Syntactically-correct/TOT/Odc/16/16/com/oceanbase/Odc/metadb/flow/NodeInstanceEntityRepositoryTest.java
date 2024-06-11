package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    private NodeInstanceEntityRepository testRepository;

    private NodeInstanceEntity nodeInstance;
    private NodeInstanceEntity nodeInstance2;
    private NodeInstanceEntity nodeInstance3;
    private FlowInstance flowInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstance = new NodeInstanceEntity();
        nodeInstance.setId(1L);
        nodeInstance.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstance.setFlowInstanceId(1L);

        nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setId(2L);
        nodeInstance2.setInstanceType(FlowNodeType.PROCESS);
        nodeInstance2.setFlowInstanceId(1L);

        nodeInstance3 = new NodeInstanceEntity();
        nodeInstance3.setId(3L);
        nodeInstance3.setInstanceType(FlowNodeType.END_EVENT);
        nodeInstance3.setFlowInstanceId(1L);

        flowInstance = new FlowInstance();
        flowInstance.setId(1L);

    }

    @After
    public void tearDown() {
        // Clear mocks
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstance, nodeInstance2);
        NodeInstanceEntity example = NodeInstanceEntity.builder()
                .flowInstanceId(flowInstance.getId())
                .instanceType(FlowNodeType.PROCESS)
                .flowableElementType(FlowableElementType.TASK)
                .build();

        Example<NodeInstanceEntity> exampleNodeInstance = Example.of(example, ExampleMatcher.matchingAny());
        List<NodeInstanceEntity> result = testRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.PROCESS, FlowableElementType.TASK);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstance, nodeInstance2, nodeInstance3);
        List<NodeInstanceEntity> result = testRepository.findByFlowInstanceId(flowInstance.getId());

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstance2, nodeInstance3);
        Collection<Long> ids = Arrays.asList(2L, 3L);

        List<NodeInstanceEntity> result = testRepository.findByIds(ids);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expectedResult = 3;

        int result = testRepository.deleteByFlowInstanceId(flowInstance.getId());

        assertEquals(expectedResult, result);

        List<NodeInstanceEntity> list = testRepository.findByFlowInstanceId(flowInstance.getId());
        assertNull(list);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int expectedResult = 2;

        int result = testRepository.deleteByInstanceIdAndInstanceType(nodeInstance2.getId(),
                nodeInstance2.getInstanceType());

        assertEquals(expectedResult, result);

        List<NodeInstanceEntity> list = testRepository.findByInstanceIdAndInstanceType(nodeInstance2.getId(),
                nodeInstance2.getInstanceType());
        assertNull(list);
    }
}