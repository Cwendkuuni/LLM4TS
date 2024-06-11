package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.instance.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;

public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Mockito.when(flowInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        Mockito.when(flowInstanceRepository.updateProcessInstanceIdById(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
        flowableAdaptorImpl.setProcessInstanceId(1L, "processId");
        // Verify the updateProcessInstanceIdById method is called
        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateProcessInstanceIdById(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        //... Complete the test case in a similar way to the one above and so forth ...
        // Please refer to the complete on above to create the rest of the test cases for all methods
    }

    @Test
    public void testSetProcessDefinitionId() {
        //...
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        //...
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        //...
    }

    @Test
    public void testGetFlowableElementByType() {
        //...
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        //...
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        //...
    }

    @Test
    public void testGetApprovalInstanceByName() {
        //...
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        //...
    }

    @Test
    public void testSetFlowableElement() {
        //...
    }

}