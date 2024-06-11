package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NodeInstanceEntityRepositoryTest {
    
    @InjectMocks
    private NodeInstanceEntityRepositoryTest nodeInstanceEntityRepository;

    @Mock
    private EntityManager em;
    
    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.CALL_ACTIVITY;

        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(em.createQuery(any(String.class)).getResultList()).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        verify(em).createQuery(any(String.class));
        assertEquals(expected, actual);
    }
    
    // Similar tests can be written for `findByFlowInstanceId()`, `findByIds()`, `deleteByFlowInstanceId()`, and `deleteByInstanceIdAndInstanceType()`.
}