import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NodeInstanceEntityRepositoryTest {

    private INodeInstanceEntityRepositoryImpl nodeInstanceEntityRep;

    @Before
    public void setUp() {
        nodeInstanceEntityRep = new INodeInstanceEntityRepositoryImpl();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        // Assuming there is a method to create the node entities
        List<NodeInstanceEntity> createdList = nodeInstEntRep.findByInstanceIdAndInstanceTypeAndFlowableElementType(
            instanceId, instanceType, FlowableElementType.USER_TASK);
            
        assertNotNull(createdList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> createdList = nodeInstEntRep.findByFlowInstanceId(flowInstanceId);

        assertNotNull(createdList);
    }

    // similar tests for other methods
}