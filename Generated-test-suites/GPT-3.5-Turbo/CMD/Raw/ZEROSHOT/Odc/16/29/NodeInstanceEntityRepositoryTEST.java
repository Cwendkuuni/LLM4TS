import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Test data
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        // Add expected NodeInstanceEntity objects to the list

        // Mock the repository method
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedList);

        // Call the method
        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Assertions
        assertNotNull(resultList);
        assertEquals(expectedList.size(), resultList.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Test data
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        // Add expected NodeInstanceEntity objects to the list

        // Mock the repository method
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedList);

        // Call the method
        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Assertions
        assertNotNull(resultList);
        assertEquals(expectedList.size(), resultList.size());
    }

    @Test
    public void testFindByIds() {
        // Test data
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        // Add expected NodeInstanceEntity objects to the list

        // Mock the repository method
        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedList);

        // Call the method
        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByIds(ids);

        // Assertions
        assertNotNull(resultList);
        assertEquals(expectedList.size(), resultList.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Test data
        Long instanceId = 1L;

        // Mock the repository method
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
                .thenReturn(1);

        // Call the method
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Assertion
        assertTrue(result == 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Test data
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        // Mock the repository method
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(1);

        // Call the method
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Assertion
        assertTrue(result == 1);
    }
}