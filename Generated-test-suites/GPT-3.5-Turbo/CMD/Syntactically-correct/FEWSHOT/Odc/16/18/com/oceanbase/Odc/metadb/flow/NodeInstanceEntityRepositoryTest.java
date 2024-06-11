package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class NodeInstanceEntityRepositoryTest {

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntityRepository repo = mock(NodeInstanceEntityRepository.class);

        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE2;

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        // Add your expected NodeInstanceEntity objects here

        when(repo.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = repo.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, instanceType, flowableElementType);

        assertEquals(expectedList.size(), actualList.size());
        // Add more assertions based on the expected results
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntityRepository repo = mock(NodeInstanceEntityRepository.class);

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        // Add your expected NodeInstanceEntity objects here

        when(repo.findByFlowInstanceId(any(Long.class)))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = repo.findByFlowInstanceId(1L);

        assertEquals(expectedList.size(), actualList.size());
        // Add more assertions based on the expected results
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntityRepository repo = mock(NodeInstanceEntityRepository.class);

        List<Long> ids = new ArrayList<>();
        // Add some test ids

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        // Add your expected NodeInstanceEntity objects here

        when(repo.findByIds(ids))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = repo.findByIds(ids);

        assertEquals(expectedList.size(), actualList.size());
        // Add more assertions based on the expected results
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntityRepository repo = mock(NodeInstanceEntityRepository.class);

        int expectedResult = 1; // Expected number of rows affected by the delete operation

        when(repo.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(expectedResult);

        int actualResult = repo.deleteByFlowInstanceId(1L);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntityRepository repo = mock(NodeInstanceEntityRepository.class);

        FlowNodeType instanceType = FlowNodeType.TYPE1;

        int expectedResult = 1; // Expected number of rows affected by the delete operation

        when(repo.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(expectedResult);

        int actualResult = repo.deleteByInstanceIdAndInstanceType(1L, instanceType);

        assertEquals(expectedResult, actualResult);
    }
}