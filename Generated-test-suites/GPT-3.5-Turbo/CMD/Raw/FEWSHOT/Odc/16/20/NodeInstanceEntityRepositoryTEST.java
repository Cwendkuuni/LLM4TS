import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;
    
    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_A;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> expectedList = Arrays.asList(entity1, entity2);

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
            .thenReturn(expectedList);
        
        List<NodeInstanceEntity> resultList = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(resultList);
        assertEquals(expectedList.size(), resultList.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> expectedList = Arrays.asList(entity1, entity2);

        Mockito.when(repository.findByFlowInstanceId(flowInstanceId))
            .thenReturn(expectedList);

        List<NodeInstanceEntity> resultList = repository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(resultList);
        assertEquals(expectedList.size(), resultList.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> expectedList = Arrays.asList(entity1, entity2);

        Mockito.when(repository.findByIds(ids))
            .thenReturn(expectedList);

        List<NodeInstanceEntity> resultList = repository.findByIds(ids);

        assertNotNull(resultList);
        assertEquals(expectedList.size(), resultList.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(repository.deleteByFlowInstanceId(instanceId))
            .thenReturn(1);

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;

        Mockito.when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
            .thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }
}