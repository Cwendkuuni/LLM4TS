
package com.oceanbase.odc.service.flow;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private FlowableAdaptorImpl flowableAdaptorImpl;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptorImpl = new FlowableAdaptorImpl();
        flowableAdaptorImpl.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptorImpl.setFlowInstanceRepository(flowInstanceRepository);
    }
    
    @Test
    public void testGetFlowableElementByType() {
        List<NodeInstanceEntity> entities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("actId");
        nodeInstanceEntity.setName("name");
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.BPMN_FLOWNODE);
        entities.add(nodeInstanceEntity);
                
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(entities);

        List<FlowableElement> flowableElements = flowableAdaptorImpl.getFlowableElementByType(123L, FlowNodeType.APPROVAL_TASK, FlowableElementType.BPMN_FLOWNODE);
        
        assertEquals(1, flowableElements.size());
        assertEquals("actId", flowableElements.get(0).getActivityId());
        assertEquals("name", flowableElements.get(0).getName());
        assertEquals(FlowableElementType.BPMN_FLOWNODE, flowableElements.get(0).getType());
    }
    
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        List<FlowInstanceEntity> entityList = new ArrayList<>();
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(123L);
        entityList.add(flowInstanceEntity);

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(entityList);
        
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("processId");

        assertTrue(result.isPresent());
        assertEquals(123L, (long)result.get());
    }
    
    @Test
    public void testInvalidGetFlowInstanceIdByProcessInstanceId() {
        List<FlowInstanceEntity> entityList = new ArrayList<>();

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(entityList);
        
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("invalidProcessId");

        assertFalse(result.isPresent());
    }
    
    // Similar test cases should be implemented for all other methods
}
