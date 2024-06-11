package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {
    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
            Mockito.anyLong(), Mockito.any(), Mockito.any())
        ).thenReturn(new ArrayList<NodeInstanceEntity>(Arrays.asList(nodeInstanceEntity)));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.END, FlowableElementType.SEQUENCE_FLOW);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(
            Mockito.anyLong())
        ).thenReturn(new ArrayList<NodeInstanceEntity>(Arrays.asList(nodeInstanceEntity)));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIds() {
        Mockito.when(nodeInstanceEntityRepository.findByIds(
            Mockito.anyCollection())
        ).thenReturn(new ArrayList<NodeInstanceEntity>(Arrays.asList(nodeInstanceEntity)));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(
            Mockito.anyLong())
        ).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(
            Mockito.anyLong(), Mockito.any())
        ).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.END);

        assertEquals(1, result);
    }
}