/*
 * Package Imports
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/*
 * Other Imports
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.NODE;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT;

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
            .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    // Test for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
            .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepositoryMock.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    // Test for findByIds method
    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByIds(ids))
            .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepositoryMock.findByIds(ids);

        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    // Test for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumDeleted = 2;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
            .thenReturn(expectedNumDeleted);

        int actualNumDeleted = nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedNumDeleted, actualNumDeleted);
    }

    // Test for deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.NODE;
        int expectedNumDeleted = 2;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
            .thenReturn(expectedNumDeleted);

        int actualNumDeleted = nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedNumDeleted, actualNumDeleted);
    }
}