
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Test
    public void testGetFlowableElementByType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("activityId");
        nodeInstanceEntity.setName("nodeName");
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.FLOW_NODE_ELEMENT);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.APPROVAL_TASK, FlowableElementType.FLOW_NODE_ELEMENT)).thenReturn(
                Arrays.asList(nodeInstanceEntity));

        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(1L, FlowNodeType.APPROVAL_TASK, FlowableElementType.FLOW_NODE_ELEMENT);

        assertEquals(result.get(0).getActivityId(),"activityId");
        assertEquals(result.get(0).getName(),"nodeName");
        assertEquals(result.get(0).getType(),FlowableElementType.FLOW_NODE_ELEMENT);
    }

    @Test
    public void testSetProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(1L, "pid")).thenReturn(1);
        
        flowableAdaptorImpl.setProcessInstanceId(1L, "pid");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId("pid");
        when(flowInstanceRepository.findAll(any())).thenReturn(Arrays.asList(flowInstanceEntity));
        
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("pid");
        
        assertTrue(result.isPresent());
        assertEquals(result.get(), new Long(1));
    }

    // .. continue for all public methods in FlowableAdaptorImpl. 
}
