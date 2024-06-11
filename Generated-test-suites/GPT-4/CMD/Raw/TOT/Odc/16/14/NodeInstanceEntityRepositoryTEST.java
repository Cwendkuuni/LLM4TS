package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // define your mock data and behaviors here
        // when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(mockResult);

        // call the method you're testing and validate the result
        // assertEquals(expectedResult, repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(testId, testInstanceType, testFlowableElementType));
    }

    @Test
    public void testFindByFlowInstanceId() {
        // similar steps as above
    }

    @Test
    public void testFindByIds() {
        // similar steps as above
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // similar steps as above
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // similar steps as above
    }
}