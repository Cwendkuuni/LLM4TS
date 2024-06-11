
package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import javax.persistence.EntityManager;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private EntityManager entityManager;
    
    private NodeInstanceEntity nodeInstanceEntity;
  
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstanceEntity = new NodeInstanceEntity();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<Long> instanceId = Arrays.asList(123L, 456L);
        List<FlowNodeType> instanceType = Arrays.asList(FlowNodeType.START_EVENT, FlowNodeType.CATCH_EVENT);
        List<FlowableElementType> flowableElementType = Arrays.asList(FlowableElementType.BOUNDARY);

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        verify(entityManager).createQuery(any(String.class));
    }

    @Test
    public void testFindByFlowInstanceId() {
        nodeInstanceEntityRepository.findByFlowInstanceId(123L);
        verify(entityManager).createQuery(any(String.class));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(123L, 456L);

        nodeInstanceEntityRepository.findByIds(ids);
        verify(entityManager).createQuery(any(String.class));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(123L);
        verify(entityManager).createQuery(any(String.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(123L, FlowNodeType.START_EVENT);
        verify(entityManager).createQuery(any(String.class));
    }
}
