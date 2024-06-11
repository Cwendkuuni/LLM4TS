package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.NodeInstance;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity;
    private NodeInstanceEntity nodeInstanceEntity2;
    private NodeInstanceEntity nodeInstanceEntity3;
    private NodeInstanceEntity nodeInstanceEntity4;
    private NodeInstanceEntity nodeInstanceEntity5;
    private FlowInstance flowInstance;

    @Before
    public void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.Activity);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.GATEWAY);
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.Gateway);

        nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setId(3L);
        nodeInstanceEntity3.setInstanceId(3L);
        nodeInstanceEntity3.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity3.setFlowInstanceId(2L);
        nodeInstanceEntity3.setFlowableElementType(FlowableElementType.Activity);

        nodeInstanceEntity4 = new NodeInstanceEntity();
        nodeInstanceEntity4.setId(4L);
        nodeInstanceEntity4.setInstanceId(4L);
        nodeInstanceEntity4.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity4.setFlowInstanceId(2L);
        nodeInstanceEntity4.setFlowableElementType(FlowableElementType.Activity);

        nodeInstanceEntity5 = new NodeInstanceEntity();
        nodeInstanceEntity5.setId(5L);
        nodeInstanceEntity5.setInstanceId(5L);
        nodeInstanceEntity5.setInstanceType(FlowNodeType.GATEWAY);
        nodeInstanceEntity5.setFlowInstanceId(2L);
        nodeInstanceEntity5.setFlowableElementType(FlowableElementType.Gateway);

        flowInstance = new FlowInstance();
        flowInstance.setId(1L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK,
                FlowableElementType.Activity)).thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> foundList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.TASK, FlowableElementType.Activity);
        assertNotNull(foundList);
        assertEquals(1, foundList.size());
        assertNotNull(foundList.get(0));
        assertSame(nodeInstanceEntity, foundList.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        nodeInstanceEntities.add(nodeInstanceEntity2);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> foundList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(foundList);
        assertEquals(2, foundList.size());
        assertNotNull(foundList.get(0));
        assertSame(nodeInstanceEntity, foundList.get(0));
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        nodeInstanceEntities.add(nodeInstanceEntity2);
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L))).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> foundList = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        assertNotNull(foundList);
        assertEquals(2, foundList.size());
        assertNotNull(foundList.get(0));
        assertSame(nodeInstanceEntity, foundList.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.countByFlowInstanceId(1L)).thenReturn(3);

        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, deletedCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.TASK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.countByInstanceIdAndInstanceType(1L, FlowNodeType.TASK)).thenReturn(2);

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
        assertEquals(2, deletedCount);
    }
}