
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    EntityManager em;
    
    @Mock
    TypedQuery<NodeInstanceEntity> query;

    @InjectMocks
    private NodeInstanceEntityRepository repository;
    
    private NodeInstanceEntity nodeInstanceEntity1, nodeInstanceEntity2;

    @Before
    public void setUp() {
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity2 = new NodeInstanceEntity();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(em.createQuery("select * from NodeInstanceEntity where instanceId = :instanceId and instanceType = :#{#instanceType.name()} and flowableElementType = :#{#flowableElementType.name()}", NodeInstanceEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1l, FlowNodeType.START_EVENT, FlowableElementType.BPMN_ACTIVITY);
        verify(query).setParameter("instanceId", 1l);
        verify(query).setParameter("instanceType", FlowNodeType.START_EVENT);
        verify(query).setParameter("flowableElementType", FlowableElementType.BPMN_ACTIVITY);
        assertEquals(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2), result);
    }

    // Similarly, rest of the methods can be tested based on their method definitions

}
