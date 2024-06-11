import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assertions.assertNotNull(nodeInstanceEntities);
        Assertions.assertEquals(1, nodeInstanceEntities.size());
    }
}