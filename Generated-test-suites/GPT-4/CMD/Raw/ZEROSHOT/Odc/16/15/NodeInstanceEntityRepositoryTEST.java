
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit4.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.FLOW_JOB;
        FlowableElementType flowableElementType = FlowableElementType.CLOUD_FUNCTION;
        
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(new ArrayList<>());
        List<NodeInstanceEntity> found = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(found.size(), 0);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(new ArrayList<>());
        List<NodeInstanceEntity> found = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(found.size(), 0);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Collections.singletonList(1L);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(new ArrayList<>());
        List<NodeInstanceEntity> found = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(found.size(), 0);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(result, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.FLOW_JOB;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(result, 1);
    }
}
