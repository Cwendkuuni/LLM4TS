import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
        //TODO: Add test logic
    }

    @Test
    public void testFindByFlowInstanceId() {
        //TODO: Add test logic
    }

    @Test
    public void testFindByIds() {
        //TODO: Add test logic
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        //TODO: Add test logic
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        //TODO: Add test logic
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.TASK);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId_NoDelete() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(999L);
        assertEquals(0, result);
    }

    // Add more edge case and exception handling tests as needed

}