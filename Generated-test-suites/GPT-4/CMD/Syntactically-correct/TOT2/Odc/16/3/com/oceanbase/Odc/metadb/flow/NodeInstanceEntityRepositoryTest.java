package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeInstanceEntityRepositoryTest {

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        assertNotNull(mockRepo.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.DECISION, FlowableElementType.NONE));
    }

    @Test
    public void findByFlowInstanceIdTest() {
        assertNotNull(mockRepo.findByFlowInstanceId(1L));
    }

    @Test
    public void findByIdsTest() {
        assertNotNull(mockRepo.findByIds(Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        assertEquals(1, mockRepo.deleteByFlowInstanceId(1L));
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        assertEquals(1, mockRepo.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.DECISION));
    }
}