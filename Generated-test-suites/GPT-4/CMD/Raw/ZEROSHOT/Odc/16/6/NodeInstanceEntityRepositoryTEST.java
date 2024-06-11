
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.DECISION;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        Assert.assertNotNull(results);
        // Add more assertions based on the business logic
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        Assert.assertNotNull(results);
        // Add more assertions based on the business logic
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByIds(ids);

        Assert.assertNotNull(results);
        // Add more assertions based on the business logic
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int deletedRows = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        Assert.assertTrue(deletedRows > 0);
        // Add more assertions based on the business logic
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.DECISION;

        int deletedRows = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        Assert.assertTrue(deletedRows > 0);
        // Add more assertions based on the business logic
    }
}
