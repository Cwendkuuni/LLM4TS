import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import com.oceanbase.odc.utils.PagingAndSortingUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private NodeInstanceEntity entity;

    @Before
    public void setUp() {
        entity = new NodeInstanceEntity();
        entity.setInstanceId(1L);
        entity.setInstanceType(FlowNodeType.START_EVENT);
        entity.setFlowableElementType(FlowableElementType.NONE);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        nodeInstanceEntityRepository.save(entity);

        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.NONE);
        assertEquals(1, entities.size());
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.NONE);
    }

    // Test other methods with basic use cases

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, FlowNodeType.START_EVENT,
                FlowableElementType.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null,
                FlowableElementType.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, null);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithEmptyList() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.NONE);
        assertTrue(entities.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithNullIds() {
        nodeInstanceEntityRepository.findByIds(null);
    }

    @Test
    public void testFindByIdsWithEmptyList() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(Collections.emptyList());
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByIdsWithCollectionOfIds() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(1L);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(2L);
        nodeInstanceEntityRepository.save(entity2);

        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, entities.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithInvalidInstanceId() {
        doThrow(new RuntimeException("Database error")).when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(1L,
                FlowNodeType.START_EVENT);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
            }
        });

        fail("Expected RuntimeException, but got no exception.");
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithValidInstanceId() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(1L);
        entity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntityRepository.save(entity);

        int deletedEntities = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L,
                FlowNodeType.START_EVENT);
        assertEquals(1, deletedEntities);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(1L,
                FlowNodeType.START_EVENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullInstanceType() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithInvalidFlowNodeType() {
        doThrow(new RuntimeException("Database error")).when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(1L,
                FlowNodeType.END_EVENT);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.END_EVENT);
            }
        });

        fail("Expected RuntimeException, but got no exception.");
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithValidInstanceIdAndInstanceType() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(1L);
        entity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntityRepository.save(entity);

        int deletedEntities = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L,
                FlowNodeType.START_EVENT);
        assertEquals(1, deletedEntities);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(1L,
                FlowNodeType.START_EVENT);
    }
}