package com.oceanbase.odc.metadb.flow;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENCE_FLOW;

        List<NodeInstanceEntity> expectedResult = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assert.isTrue(result.size() == 1, "Should return 1 node instance entity");
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(),new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        Assert.isTrue(result.size() == 2, "Should return 2 node instance entities");
    }

    @Test
    public void testFindByIds() {
        Long id = 1L;

        List<NodeInstanceEntity> expectedResult = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByIds(Collections.singleton(id))).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Collections.singleton(id));

        Assert.isTrue(result.size() == 1, "Should return 1 node instance entity");
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        Assert.isTrue(result == 1, "Should delete 1 node instance entity");
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        Assert.isTrue(result == 1, "Should delete 1 node instance entity");
    }
}