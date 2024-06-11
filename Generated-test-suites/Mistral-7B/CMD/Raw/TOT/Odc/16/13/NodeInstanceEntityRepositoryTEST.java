package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.model.FlowableElementTypeEnum;
import com.oceanbase.odc.metadb.flow.entity.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.NodeInstanceEntityRepository;

/**
 * Test class for {@link NodeInstanceEntityRepository}
 *
 * @author yh263208
 * @date 2022-02-09 14:18
 * @since ODC_release_3.3.0
 */
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository testedRepository;

    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private NodeInstanceEntity nodeInstanceEntity3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeTypeEnum.START);
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementTypeEnum.ACTIVITY);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setInstanceId(1L);
        nodeInstanceEntity2.setInstanceType(FlowNodeTypeEnum.END);
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementTypeEnum.ACTIVITY);

        nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setId(3L);
        nodeInstanceEntity3.setInstanceId(2L);
        nodeInstanceEntity3.setInstanceType(FlowNodeTypeEnum.INTERMEDIATE);
        nodeInstanceEntity3.setFlowInstanceId(1L);
        nodeInstanceEntity3.setFlowableElementType(FlowableElementTypeEnum.DECISION);

        when(nodeInstanceEntityRepository.findById(anyLong())).thenAnswer(invocationOnMock -> nodeInstanceEntity1);
        when(nodeInstanceEntityRepository.findAll()).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenCallRealMethod();
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(Collections.singletonList(nodeInstanceEntity1));
        when(nodeInstanceEntityRepository.findByIds(anyCollection()))
                .thenReturn(new ArrayList<>(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3)));
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(3);
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)))
                .thenReturn(1);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeTypeEnum.START;
        FlowableElementType flowableElementType = FlowableElementTypeEnum.ACTIVITY;
        Long instanceId = 1L;

        List<NodeInstanceEntity> result = testedRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(instanceId, result.get(0).getInstanceId());
        assertEquals(instanceType, result.get(0).getInstanceType());
        assertEquals(flowableElementType, result.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> result = testedRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(1, result.size());
        assertEquals(flowInstanceId, result.get(0).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> result = testedRepository.findByIds(ids);

        assertEquals(3, result.size());
        assertTrue(ids.containsAll(Arrays.asList(result.get(0).getId(), result.get(1).getId(), result.get(2).getId())));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = testedRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(3, result);
        assertNull(testedRepository.findByFlowInstanceId(instanceId).get(0));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeEnum.INTERMEDIATE;

        int result = testedRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
        assertNull(testedRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, FlowableElementTypeEnum.DECISION));
    }
}