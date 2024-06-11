import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(nodeInstanceEntities);
        assertEquals(1, nodeInstanceEntities.size());
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntities.get(0);
        assertNotNull(nodeInstanceEntity);
    }
}