import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository repository;

    private NodeInstanceEntity testEntity;

    @Before
    public void setup() {
        testEntity = new NodeInstanceEntity();
        testEntity.setFlowInstanceId(1L);
        testEntity.setInstanceId(1L);
        testEntity.setInstanceType(FlowNodeType.START);
        testEntity.setFlowableElementType(FlowableElementType.START_EVENT);
        repository.save(testEntity);
    }

    @After
    public void teardown() {
        repository.deleteAll();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.START;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(testEntity);
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Collections.singletonList(testEntity);
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(testEntity);
        List<NodeInstanceEntity> result = repository.findByIds(ids);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int expectedResult = 1;
        int result = repository.deleteByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(expectedResult, result);
        List<NodeInstanceEntity> nodes = repository.findByFlowInstanceId(flowInstanceId);
        Assert.assertTrue(nodes.isEmpty());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 1;
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertEquals(expectedResult, result);
        List<NodeInstanceEntity> nodes = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, FlowableElementType.START_EVENT);
        Assert.assertTrue(nodes.isEmpty());
    }
}