
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    EntityManager entityManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> entities = new ArrayList<>();
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(entities);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.SEQUENCE_FLOW);
        assertEquals(entities, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> entities = new ArrayList<>();
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(entities);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(entities, result);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> entities = new ArrayList<>();
        when(entityManager.createQuery(anyString()).getResultList()).thenReturn(entities);
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(entities, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(entityManager.createQuery(anyString()).executeUpdate()).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(entityManager.createQuery(anyString()).executeUpdate()).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
    }

}
