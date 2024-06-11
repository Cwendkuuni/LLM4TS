import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT,
                        FlowableElementType.USER_TASK);
        assertNotNull(nodeInstances);
        assertTrue(nodeInstances.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(nodeInstances);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(nodeInstances);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rows = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, rows);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int rows = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(0, rows);
    }

}