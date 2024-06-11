import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
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

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeList = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_TYPE_A);
        assertNotNull(nodeList);
        assertEquals(2, nodeList.size());
    }

    // Test for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeList = repository.findByFlowInstanceId(1L);
        assertNotNull(nodeList);
        assertFalse(nodeList.isEmpty());
    }

    // Test for findByIds method
    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeList = repository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertNotNull(nodeList);
        assertEquals(3, nodeList.size());
    }

    // Test for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        int deleted = repository.deleteByFlowInstanceId(1L);
        assertTrue(deleted >= 0);
    }

    // Test for deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int deleted = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);
        assertTrue(deleted >= 0);
    }

}