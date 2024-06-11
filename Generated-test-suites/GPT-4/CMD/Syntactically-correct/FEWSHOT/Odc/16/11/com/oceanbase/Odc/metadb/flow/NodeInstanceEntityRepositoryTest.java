
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.USERTASK);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertFalse(results.isEmpty());
    }
    
    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertFalse(results.isEmpty());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int rowsDeleted = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertTrue(rowsDeleted > 0);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        int rowsDeleted = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertTrue(rowsDeleted > 0);
    }
}
