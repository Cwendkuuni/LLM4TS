package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityTestData;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:test-application.properties")
public class NodeInstanceEntityRepositoryTest {

    @Qualifier("nodeInstanceEntityRepository")
    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityTestData nodeInstanceEntityTestData;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(nodeInstanceEntityTestData.getNodeInstanceEntity1(),
                nodeInstanceEntityTestData.getNodeInstanceEntity2());
        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(nodeInstanceEntityTestData.getNodeInstanceEntity1(),
                nodeInstanceEntityTestData.getNodeInstanceEntity2());
        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(nodeInstanceEntityTestData.getNodeInstanceEntity1(),
                nodeInstanceEntityTestData.getNodeInstanceEntity2());
        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumberOfDeletedEntities = 2;
        int actualNumberOfDeletedEntities = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfDeletedEntities = 2;
        int actualNumberOfDeletedEntities = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    @Test
    public void testFindEmptyListByFlowInstanceId() {
        Long flowInstanceId = 3L;
        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(actualNodeInstances);
        assertTrue(actualNodeInstances.isEmpty());
    }

    @Test
    public void testDeleteEmptyListByFlowInstanceId() {
        Long instanceId = 3L;
        int expectedNumberOfDeletedEntities = 0;
        int actualNumberOfDeletedEntities = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        Long instanceId = 1L;
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, null);
    }

}