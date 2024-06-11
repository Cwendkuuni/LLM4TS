package com.oceanbase.odc.metadb.flow;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {
    
    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    
    @InjectMocks
    NodeInstanceEntity nodeInstanceEntity;
    
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 101L; 
        FlowNodeType instanceType = FlowNodeType.ACTION;
        FlowableElementType flowableElementType = FlowableElementType.BOUNDARY_EVENT;
        
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
            .thenReturn(Arrays.asList(nodeInstanceEntity));
        
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        
        assertEquals(1, results.size());
    }
    
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 102L; 
        
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
            .thenReturn(Arrays.asList(nodeInstanceEntity));
        
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        
        assertEquals(1, results.size());
    }
    
    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(101L, 102L);
        
        when(nodeInstanceEntityRepository.findByIds(ids))
            .thenReturn(Arrays.asList(nodeInstanceEntity));
        
        List<NodeInstanceEntity> results = nodeInstanceEntityRepository.findByIds(ids);
        
        assertEquals(1, results.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 101L;
       
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
            .thenReturn(1);
        
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 101L; 
        FlowNodeType instanceType = FlowNodeType.ACTION;
        
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
            .thenReturn(1);
        
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        
        assertEquals(1, result);
    }
}