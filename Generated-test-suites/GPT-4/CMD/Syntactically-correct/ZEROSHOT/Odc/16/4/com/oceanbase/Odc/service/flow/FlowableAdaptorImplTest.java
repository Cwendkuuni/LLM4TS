package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.*;
import com.oceanbase.odc.service.flow.instance.*;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    private Long flowInstanceId = 1L;
    private String processInstanceId = "processInstanceId";
    private String processDefinitionId = "processDefinitionId";
    private String activityId = "activityId";
    private String name = "name";
    private FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
    private FlowableElementType flowableElementType = FlowableElementType.task;

    @Before
    public void setUp() throws Exception{
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository, times(1)).findById(flowInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        assertEquals(Optional.of(1L), flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId));
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verify(flowInstanceRepository, times(1)).findById(flowInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        assertEquals(Optional.of(1L), flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(getFlowInstanceEntity()));
        assertEquals(Optional.of(processInstanceId), flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testGetFlowableElementByType() {
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(Arrays.asList(getNodeInstanceEntity()));
        assertEquals(1, flowableAdaptorImpl.getFlowableElementByType(flowInstanceId, instanceType, flowableElementType).size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));
    }

    @Test
    public void testGetApprovalInstanceByName() {
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong()))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        when(nodeInstance.getNodeType()).thenReturn(FlowNodeType.APPROVAL_TASK);
        when(nodeInstance.getId()).thenReturn(1L);
        when(nodeInstance.getFlowInstanceId()).thenReturn(1L);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);
        nodeInstanceEntity.setName(name);
        nodeInstanceEntity.setActivityId(activityId);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.APPROVAL_TASK, flowableElementType))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        FlowableElement flowableElement = mock(FlowableElement.class);
        when(flowableElement.getName()).thenReturn(name);
        when(flowableElement.getActivityId()).thenReturn(activityId);
        when(flowableElement.getType()).thenReturn(flowableElementType);
        flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);
    }

    private FlowInstanceEntity getFlowInstanceEntity(){
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId(processInstanceId);
        return entity;
    }

    private NodeInstanceEntity getNodeInstanceEntity(){
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId(activityId);
        return entity;
    }
}
