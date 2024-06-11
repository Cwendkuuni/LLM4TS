package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, 1L, instanceType, flowableElementType));

        Example<NodeInstanceEntity> example = Example.of(new NodeInstanceEntity(instanceId, null, instanceType, flowableElementType),
                ExampleMatcher.matchingAllProperties());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(example);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, flowInstanceId, null, null),
                new NodeInstanceEntity(2L, flowInstanceId, null, null));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = Arrays.asList(id1, id2);

        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(id1, null, null, null),
                new NodeInstanceEntity(id2, null, null, null));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumberOfDeletedEntities = 2;

        nodeInstanceEntityRepository.save(new NodeInstanceEntity(1L, instanceId, null, null));
        nodeInstanceEntityRepository.save(new NodeInstanceEntity(2L, instanceId, null, null));

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedNumberOfDeletedEntities, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfDeletedEntities = 1;

        nodeInstanceEntityRepository.save(new NodeInstanceEntity(1L, instanceId, instanceType, null));

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedNumberOfDeletedEntities, result);
    }
}