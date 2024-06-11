import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENTIAL;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(instanceId, instanceType, flowableElementType)));
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START, FlowableElementType.SEQUENTIAL)),
                new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END, FlowableElementType.END)));
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(id1, FlowNodeType.START, FlowableElementType.SEQUENTIAL)),
                new NodeInstanceEntity(new NodeInstanceEntityId(id2, FlowNodeType.END, FlowableElementType.END)));
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @Rollback(false)
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 2;
        nodeInstanceEntityRepository.save(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START, FlowableElementType.SEQUENTIAL)));
        nodeInstanceEntityRepository.save(new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END, FlowableElementType.END)));
        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @Rollback(false)
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expected = 1;
        nodeInstanceEntityRepository.save(new NodeInstanceEntity(new NodeInstanceEntityId(instanceId, instanceType, FlowableElementType.SEQUENTIAL)));
        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START, FlowableElementType.SEQUENTIAL)),
                new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END, FlowableElementType.END)));
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findAll();
        Assert.assertEquals(expected, actual);
    }
}