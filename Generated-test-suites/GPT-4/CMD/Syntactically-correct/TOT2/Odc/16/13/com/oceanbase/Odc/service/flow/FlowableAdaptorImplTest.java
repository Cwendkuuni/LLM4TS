package com.oceanbase.odc.service.flow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setProcessInstanceIdTest() {
        //... 
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceIdTest() {
        //... 
    }

    @Test
    public void setProcessDefinitionIdTest() {
        //... 
    }

    @Test
    public void getFlowInstanceIdByProcessDefinitionIdTest() {
        //... 
    }

    @Test
    public void getProcessInstanceIdByFlowInstanceIdTest() {
        //... 
    }

    @Test
    public void getFlowableElementByTypeTest() {
        //... 
    }

    @Test
    public void getTaskInstanceByActivityIdTest() {
        //... 
    }

    @Test
    public void getApprovalInstanceByActivityIdTest() {
        //... 
    }

    @Test
    public void getApprovalInstanceByNameTest() {
        //... 
    }

    @Test
    public void getGatewayInstanceByActivityIdTest() {
        //... 
    }

    @Test
    public void setFlowableElementTest() {
        //...
    }

    // Also add negative or edge case tests...
}