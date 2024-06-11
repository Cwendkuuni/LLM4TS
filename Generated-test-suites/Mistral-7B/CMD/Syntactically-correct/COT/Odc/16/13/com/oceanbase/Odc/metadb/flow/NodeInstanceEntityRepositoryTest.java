import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId_whenFlowInstanceIdIsValid_thenNumberOfNodesDeletedShouldBeGreaterThanZero() {
        Long instanceId = 1L;
        int expectedNumberOfNodesDeleted = 2;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertGreaterThan(result, 0);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_whenInstanceIdAndInstanceTypeAreValid_thenNumberOfNodesDeletedShouldBeGreaterThanZero() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfNodesDeleted = 2;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assertions.assertGreaterThan(result, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceId_whenFlowInstanceIdIsInvalid() {
        Long instanceId = -1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_whenInstanceIdOrInstanceTypeIsInvalid() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

}