package com.oceanbase.odc.metadb.flow;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    NodeInstanceEntity nodeInstanceEntityMock;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntity", nodeInstanceEntityMock);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT))
                .thenReturn(Collections.singletonList(nodeInstanceEntityMock));
        assertEquals(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT).size(), 1);
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(Collections.singletonList(nodeInstanceEntityMock));
        assertEquals(nodeInstanceEntityRepository.findByFlowInstanceId(1L).size(), 1);
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L))).thenReturn(Arrays.asList(nodeInstanceEntityMock, nodeInstanceEntityMock, nodeInstanceEntityMock));
        assertEquals(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L)).size(), 3);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        assertEquals(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L), 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT)).thenReturn(1);
        assertEquals(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT), 1);
    }
}
