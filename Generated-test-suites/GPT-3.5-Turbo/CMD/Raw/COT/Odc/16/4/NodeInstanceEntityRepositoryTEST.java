import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Test for existing data
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.TASK);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Test for non-existing data
        List<NodeInstanceEntity> emptyResult = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(100L, FlowNodeType.END_EVENT, FlowableElementType.TASK);
        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> result = repository.findByIds(ids);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = repository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(1, result);
    }
}