package com.oceanbase.odc.metadb.flow;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Test case to check finding by instanceId and instanceType and flowableElementType
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE1);
        assertNotNull(nodeInstanceEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Test case to check finding by flowInstanceId
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(nodeInstanceEntities);
    }

    @Test
    public void testFindByIds() {
        // Test case to check finding by ids
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(nodeInstanceEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Test case to check deleting by flowInstanceId
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Test case to check deleting by instanceId and instanceType
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1);
        assertEquals(0, result);
    }
}