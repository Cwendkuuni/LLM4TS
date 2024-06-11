
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.BPMN);
        // Assert your expected results here, as per your test setup. For example, assert size:
        assertEquals(0, results.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(0, results.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        assertEquals(0, results.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsDeleted = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(0,rowsDeleted);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int rowsDeleted = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(0, rowsDeleted);
    }
}
