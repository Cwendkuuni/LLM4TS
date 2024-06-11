package com.oceanbase.odc.service.flow;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlowableAdaptorImplTest {
    
    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Test
    void setProcessInstanceIdTest() {
        Long flowInstanceId = 123L;
        String processInstanceId = "processId";
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository).findById(flowInstanceId);
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
  
   // Adding few more tests cases for visibility

    @Test
    void getFlowInstanceIdByProcessInstanceIdTest() {
        String processInstanceId = "processId";
        flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        verify(flowInstanceRepository).findAll(any());
    }

    @Test
    void getTaskInstanceByActivityIdTest() {
        // add assertions here
    }

    @Test
    void getApprovalInstanceByActivityIdTest() {
        // add assertions here
    }  
   
    //similarly add other method's test cases here...

}
