package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {
    @InjectMocks
    NodeInstanceEntityRepository repository;

    @Mock
    NodeInstanceEntityRepository mockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> list = new ArrayList<>();

        when(mockRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.StartEvent, FlowableElementType.Task)).thenReturn(list);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.StartEvent, FlowableElementType.Task);

        assertEquals(list, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> list = new ArrayList<>();
        when(mockRepository.findByFlowInstanceId(1L)).thenReturn(list);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);

        assertEquals(list, result);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> list = new ArrayList<>();
        when(mockRepository.findByIds(Arrays.asList(1L, 2L))).thenReturn(list);

        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(1L, 2L));

        assertEquals(list, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(mockRepository.deleteByFlowInstanceId(1L)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(mockRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.StartEvent)).thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.StartEvent);

        assertEquals(1, result);
    }
}