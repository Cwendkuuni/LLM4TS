package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.*;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    @InjectMocks
    FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    FlowInstanceRepository flowInstanceRepository;
    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    TaskService taskService;
    @Mock
    FormService formService;
    @Mock
    AuthenticationFacade authenticationFacade;
    @Mock
    GateWayInstanceRepository gateWayInstanceRepository;

    @Test
    public void testSetProcessInstanceId() {
        when(flowInstanceRepository.findById(any())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(any(), any())).thenReturn(1);

        flowableAdaptorImpl.setProcessInstanceId(1L, "processInstanceId");
        verify(flowInstanceRepository).updateProcessInstanceIdById(any(), any());
    }

    @Test
    public void testSetProcessDefinitionId() {
        when(flowInstanceRepository.findById(any())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(any(), any())).thenReturn(1);

        flowableAdaptorImpl.setProcessDefinitionId(1L, "processDefinitionId");
        verify(flowInstanceRepository).updateProcessDefinitionIdById(any(), any());
    }

    //Add more test methods...
}