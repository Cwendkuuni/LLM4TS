
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.ResourceType;
  
public class FlowableAdaptorImplTest {
  
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;
  
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
  
    @Mock
    private FlowInstanceEntity flowInstanceEntity;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(123L)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(123L, "pId")).thenReturn(1);
        flowableAdaptorImpl.setProcessInstanceId(123L, "pId");
        verify(flowInstanceRepository).updateProcessInstanceIdById(123L, "pId");
    }

    @Test(expected = PreConditions.ResourceNotExistException.class)
    public void testSetProcessInstanceId_NotFound() {
        when(flowInstanceRepository.findById(123L)).thenReturn(Optional.empty());
        flowableAdaptorImpl.setProcessInstanceId(123L, "pId");
    }
    
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Add your mock object setup and verifications here.
        // Use when() to setup conditions and verify() to make sure certain methods are called.
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testGetFlowableElementByType() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testSetFlowableElement() {
        // Add your mock object setup and verifications here.
    }

    @Test
    public void testInnerConvert() {
        // Add your mock object setup and verifications here.
    }
 }
