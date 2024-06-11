
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowableElement;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private Specification specification;
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp(){
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId(){
        doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(any(Long.class), any(String.class));
        flowableAdaptorImpl.setProcessInstanceId(1L, "processInstanceId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId(){
        when(flowInstanceRepository.findAll(any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertTrue(result.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId(){
        doNothing().when(flowInstanceRepository).updateProcessDefinitionIdById(any(Long.class), any(String.class));
        flowableAdaptorImpl.setProcessDefinitionId(1L, "processDefinitionId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId(){
        when(flowInstanceRepository.findAll(any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId(){
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId("processInstanceId");
        when(flowInstanceRepository.findById(any())).thenReturn(Optional.of(flowInstanceEntity));
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        assertEquals("processInstanceId", result.get());
    }

    @Test
    public void testGetFlowableElementByType(){
        List<NodeInstanceEntity> nodeInstanceEntityList = Collections.singletonList(new NodeInstanceEntity());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(nodeInstanceEntityList);
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class));
        assertEquals(nodeInstanceEntityList.size(), result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId(){
        // Fill when statements for each mock when required.
        // Create object for the return of each mock if any.
    }

    @Test
    public void testGetApprovalInstanceByActivityId(){
        // Fill when statements for each mock when required.
        // Create object for the return of each mock if any.
    }

    @Test
    public void testGetApprovalInstanceByName(){
        // Fill when statements for each mock when required.
        // Create object for the return of each mock if any.
    }

    @Test
    public void testGetGatewayInstanceByActivityId(){
        // Fill when statements for each mock when required.
        // Create object for the return of each mock if any.
    }

    @Test
    public void testSetFlowableElement(){
        // Fill when statements for each mock when required.
        // Create object for the return of each mock if any.
    }
}
