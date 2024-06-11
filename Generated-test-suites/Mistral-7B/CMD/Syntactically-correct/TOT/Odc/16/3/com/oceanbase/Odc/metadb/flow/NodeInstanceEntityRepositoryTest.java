package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @MockBean
    private NodeInstanceEntityRepository repository;

    private NodeInstanceEntity node1, node2, node3;

    @Before
    public void setup() {
        node1 = new NodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.TASK, null, 1L);
        node2 = new NodeInstanceEntity(2L, FlowNodeType.INTERMEDIATE, FlowableElementType.GATEWAY, null, 2L);
        node3 = new NodeInstanceEntity(3L, FlowNodeType.END, FlowableElementType.TASK, null, 3L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.TASK))
                .thenReturn(Arrays.asList(node1));

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START, FlowableElementType.TASK);

        assertEquals(1, result.size());
        assertEquals(node1, result.get(0));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeEmptyResult() {
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.GATEWAY))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START, FlowableElementType.GATEWAY);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Mockito.when(repository.findByFlowInstanceId(1L))
                .thenReturn(Arrays.asList(node1, node3));

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);

        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(1));
    }

    @Test
    public void testFindByIds() {
        Mockito.when(repository.findByIds(Arrays.asList(1L, 3L)))
                .thenReturn(Arrays.asList(node1, node3));

        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(1L, 3L));

        assertEquals(2, result.size());
        assertEquals(node1, result.get(0));
        assertEquals(node3, result.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.doNothing().when(repository).deleteByFlowInstanceId(1L);

        int result = repository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.doNothing().when(repository).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeEmptyResult() {
        Mockito.doNothing().when(repository).deleteByInstanceIdAndInstanceType(2L, FlowNodeType.GATEWAY);

        int result = repository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.GATEWAY);

        assertEquals(0, result);
    }

    @Test
    public void testFindAllByFlowInstanceId() {
        Mockito.when(repository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Arrays.asList(node1, node2, node3));

        Optional<Page<NodeInstanceEntity>> result = repository.findAll(Mockito.mock(Specification.class));

        assertTrue(result.isPresent());
        Page<NodeInstanceEntity> page = result.get();
        assertEquals(3, page.getContent().size());
    }
}