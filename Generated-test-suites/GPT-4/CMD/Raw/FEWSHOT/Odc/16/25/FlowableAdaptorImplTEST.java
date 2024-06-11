package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceEntity flowInstanceEntity;
    
    @Mock
    private NodeInstanceEntity nodeInstanceEntity;
    
    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
    
    @Mock
    private GateWayInstanceEntity gateWayInstanceEntity;
    
    @Mock
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Test
    public void setProcessInstanceIdTest() {
        flowableAdaptorImpl.setProcessInstanceId(1L, "123");
        verify(flowInstanceRepository).updateProcessInstanceIdById(any(Long.class), any(String.class));
        assertEquals(1, affectRows);
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceIdTest() {
        when(flowInstanceEntity.getId()).thenReturn(1L);
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
        Optional<Long> actual = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("123");
        assertEquals(Optional.of(1L), actual);
    }

    // continue for rest of the public methods in the source Java class
}