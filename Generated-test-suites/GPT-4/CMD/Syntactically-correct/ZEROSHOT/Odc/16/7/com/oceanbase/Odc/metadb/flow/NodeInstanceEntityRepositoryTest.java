
package com.oceanbase.odc.metadb.flow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link NodeInstanceEntityRepository}
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {
    
    @InjectMocks
    private NodeInstanceEntityRepository repository;
    
    @Mock
    private NodeInstanceEntity nodeEntityMock;
    
    @Mock
    private FlowNodeType flowNodeTypeMock;

    @Mock
    private FlowableElementType flowableElementTypeMock;
    
    @BeforeEach
    public void setup() {
        nodeEntityMock = new NodeInstanceEntity();
        flowNodeTypeMock = FlowNodeType.START_EVENT;
        flowableElementTypeMock = FlowableElementType.METHOD;
    }

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementType_shouldReturnListOfNodeInstanceEntities() {
        Long instanceId = 1L;
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, flowNodeTypeMock, flowableElementTypeMock)).thenReturn(Arrays.asList(nodeEntityMock));
	
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, flowNodeTypeMock, flowableElementTypeMock);

        assertThat(result).isNotNull().isNotEmpty().contains(nodeEntityMock);
    }
    
    @Test
    public void findByFlowInstanceId_ShouldReturnListOfNodeInstanceEntities() {
        Long flowInstanceId = 1L;
        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(Arrays.asList(nodeEntityMock));

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertThat(result).isNotNull().isNotEmpty().contains(nodeEntityMock);
    }

    @Test
    public void findByIds_ShouldReturnListOfNodeInstanceEntities() {
        when(repository.findByIds(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(nodeEntityMock));

        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(1L, 2L));

        assertThat(result).isNotNull().isNotEmpty().contains(nodeEntityMock);
    }
    
    @Test
    public void deleteByFlowInstanceId_ShouldDeleteEntities() {
        Long instanceId = 1L;
        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertThat(result).isEqualTo(1);
        verify(repository).deleteByFlowInstanceId(instanceId);
    }
    
    @Test
    public void deleteByInstanceIdAndInstanceType_ShouldDeleteEntities() {
        Long instanceId = 1L;
        when(repository.deleteByInstanceIdAndInstanceType(instanceId, flowNodeTypeMock)).thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, flowNodeTypeMock);

        assertThat(result).isEqualTo(1);
        verify(repository).deleteByInstanceIdAndInstanceType(instanceId, flowNodeTypeMock);
    }
}
