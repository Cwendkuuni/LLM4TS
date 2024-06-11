package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.SpecificationSupport;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntityId;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private SpecificationSupport specificationSupport;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        specificationSupport = new SpecificationSupport();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(instanceId, instanceType, flowableElementType), "name1"));
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START, FlowableElementType.PROCESS), "name1"),
                new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END, FlowableElementType.PROCESS), "name2"));
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<NodeInstanceEntity> page = new PageImpl<>(expected);
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(page);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START, FlowableElementType.PROCESS), "name1"),
                new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END, FlowableElementType.PROCESS), "name2"));
        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 2;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expected = 1;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test
    public void testFindingEmptyPage() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<NodeInstanceEntity> expected = new PageImpl<>(Collections.emptyList());
        Mockito.when(nodeInstanceEntityRepository.findAll(pageable)).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findAll(pageable).getContent();

        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    public void testFindingEmptyList() {
        List<NodeInstanceEntity> expected = Collections.emptyList();
        Mockito.when(nodeInstanceEntityRepository.findAll()).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findAll();

        assertNotNull(actual);
        assertEquals(0, actual.size());
    }
}