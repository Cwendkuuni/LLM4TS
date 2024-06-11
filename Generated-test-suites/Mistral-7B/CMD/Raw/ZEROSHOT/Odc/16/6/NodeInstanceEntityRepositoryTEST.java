import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null));

        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .willReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        then(nodeInstanceEntityRepository).should().findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, null, null, null, flowInstanceId));

        given(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .willReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Then
        then(nodeInstanceEntityRepository).should().findByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        // Given
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, null, null, null, null), new NodeInstanceEntity(2L, null, null, null, null));

        given(nodeInstanceEntityRepository.findByIds(ids))
                .willReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Then
        then(nodeInstanceEntityRepository).should().findByIds(ids);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;

        // When
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Then
        then(nodeInstanceEntityRepository).should().deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        // When
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        then(nodeInstanceEntityRepository).should().deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertEquals(1, result);
    }
}