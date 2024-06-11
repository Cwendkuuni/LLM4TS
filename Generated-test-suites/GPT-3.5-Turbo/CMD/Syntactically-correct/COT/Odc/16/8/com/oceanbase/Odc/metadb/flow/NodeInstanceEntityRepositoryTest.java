
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_B;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(0, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, result);
    }
}
