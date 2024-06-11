import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity());
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByFlowInstanceId
    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity());
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for findByIds
    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity());
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 1;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for deleteByInstanceIdAndInstanceType
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 1;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertEquals(expectedResult, result);
    }

    // Test for empty result for findByInstanceIdAndInstanceTypeAndFlowableElementType
    @Test
    public void testEmptyResultForFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    // Test for empty ids for findByIds
    @Test(expected = java.util.NoSuchElementException.class)
    public void testEmptyIdsForFindByIds() {
        Collection<Long> ids = Collections.emptyList();
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
    }

    // Test for negative id for findByIds
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNegativeIdForFindByIds() {
        Collection<Long> ids = Arrays.asList(-1L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
    }

    // Test for null instanceId for deleteByFlowInstanceId
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNullInstanceIdForDeleteByFlowInstanceId() {
        Long instanceId = null;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
    }

    // Test for null ids for deleteByIds
    @Test(expected = java.util.NoSuchElementException.class)
    public void testNullIdsForDeleteByIds() {
        Collection<Long> ids = null;
        int result = nodeInstanceEntityRepository.deleteByIds(ids);
    }
}