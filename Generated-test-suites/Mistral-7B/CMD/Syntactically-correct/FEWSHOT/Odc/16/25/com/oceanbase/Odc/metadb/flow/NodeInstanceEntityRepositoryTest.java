package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntityBuilder;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeInstanceBuilder;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        nodeInstanceEntity = new NodeInstanceEntityBuilder().build();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedList = Arrays.asList(new NodeInstanceEntityBuilder().build(), nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(expectedList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        assertEquals(expectedList, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedList = Arrays.asList(nodeInstanceEntity, nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedList, result);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> expectedList = Arrays.asList(nodeInstanceEntity, nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedList, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        when(nodeInstanceEntityRepository).thenAnswer(invocationOnMock -> {
            int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
            return result;
        });

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(2, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        when(nodeInstanceEntityRepository).thenAnswer(invocationOnMock -> {
            int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
            return result;
        });

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(2, result);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> expectedList = Arrays.asList(nodeInstanceEntity, nodeInstanceEntity);
        Pageable pageable = PageRequest.of(0, 10);

        when(nodeInstanceEntityRepository).thenReturn(new NodeInstanceEntityRepository() {
            @Override
            public Page<NodeInstanceEntity> findAll(Specification<NodeInstanceEntity> spec, Pageable pageable) {
                return new PageImpl<>(expectedList, pageable, expectedList.size());
            }
        });

        Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(null, pageable);

        assertEquals(expectedList, result.getContent());
    }

    @Test
    public void testCount() {
        Long expectedCount = 2L;

        when(nodeInstanceEntityRepository).thenAnswer(invocationOnMock -> {
            return nodeInstanceEntityRepository.count();
        });

        Long result = nodeInstanceEntityRepository.count();

        assertEquals(expectedCount, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntityToSave = new NodeInstanceEntityBuilder().build();

        nodeInstanceEntityRepository.save(nodeInstanceEntityToSave);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindFirstByOrderByIdDesc() {
        nodeInstanceEntityRepository.findFirstByOrderByIdDesc();
    }
}