package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.specification.FlowInstanceSpecs;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableElementTestBuilder;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstanceTestBuilder;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstanceTestBuilder;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceTestBuilder;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.auth.principal.SystemPrincipal;
import com.oceanbase.odc.service.flow.repository.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.repository.GateWayInstanceRepository;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.repository.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.test.mock.FlowableServiceMock;
import com.oceanbase.odc.service.flow.test.mock.FormServiceMock;

/**
 * Test class for {@link FlowableAdaptorImpl}
 *
 * @author yh263208
 * @date 2023-03-26 16:21
 * @see FlowableAdaptorImpl
 */
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private FormService formService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowServiceMock flowService;

    @Mock
    private FormServiceMock formServiceMock;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private AuthenticationFacadeMock authenticationFacadeMock;

    private Long flowInstanceId = 1L;
    
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, anyString(),
                anyLong())).thenReturn(Optional.empty());
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.emptyList());
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(),
                anyLong())).thenReturn(Optional.empty());
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(),
                anyLong())).thenReturn(Optional.empty());
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), anyString(),
                anyLong())).thenReturn(Optional.empty());
        when(nodeInstanceRepository.save(any(NodeInstanceEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(flowService.getFlowInstanceById(anyLong())).thenReturn(new FlowInstanceEntity());
        when(formServiceMock.getFormKey(anyString())).thenReturn("formKey");
        when(authenticationFacade.getPrincipal()).thenReturn(new SystemPrincipal());
    }

    /**
     * Test method for {@link FlowableAdaptor#setProcessInstanceId(Long, String)}
     */
    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, "processInstanceId");
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, "processInstanceId");
    }

    /**
     * Test method for {@link FlowableAdaptor#getFlowInstanceIdByProcessInstanceId(String)}
     */
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Optional<FlowInstanceEntity> flowInstanceEntityOptional = Optional.of(new FlowInstanceEntity());
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(flowInstanceEntityOptional.get()));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    /**
     * Test method for {@link FlowableAdaptor#setProcessDefinitionId(Long, String)}
     */
    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, "processDefinitionId");
        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, "processDefinitionId");
    }

    /**
     * Test method for {@link FlowableAdaptor#getFlowInstanceIdByProcessDefinitionId(String)}
     */
    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Optional<FlowInstanceEntity> flowInstanceEntityOptional = Optional.of(new FlowInstanceEntity());
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(flowInstanceEntityOptional.get()));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    /**
     * Test method for {@link FlowableAdaptor#getProcessInstanceIdByFlowInstanceId(Long)}
     */
    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertFalse(result.isPresent());
    }

    /**
     * Test method for {@link FlowableAdaptor#getFlowableElementByType(Long, FlowNodeType, FlowableElementType)}
     */
    @Test
    public void testGetFlowableElementByType() {
        List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(flowInstanceId, FlowNodeType.USER_TASK,
                FlowableElementType.START);
        assertTrue(result.isEmpty());

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setActivityId("activityId");
        nodeInstanceEntity.setName("name");
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START);
        nodeInstanceEntity.setInstanceType(FlowNodeType.USER_TASK);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(),
                any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(List.of(nodeInstanceEntity));

        result = flowableAdaptorImpl.getFlowableElementByType(flowInstanceId, FlowNodeType.USER_TASK,
                FlowableElementType.START);
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity.getName(), result.get(0).getName());
    }

    /**
     * Test method for {@link FlowableAdaptor#getTaskInstanceByActivityId(String, Long)}
     */
    @Test
    public void testGetTaskInstanceByActivityId() {
        Optional<ServiceTaskInstanceEntity> serviceTaskInstanceEntityOptional = Optional.of(new ServiceTaskInstanceEntity());
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, anyString(),
                anyLong())).thenReturn(serviceTaskInstanceEntityOptional);
        Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId("activityId", flowInstanceId);
        assertTrue(result.isPresent());
        verify(eventPublisher).publish(any());
    }

    /**
     * Test method for {@link FlowableAdaptor#getApprovalInstanceByActivityId(String, Long)}
     */
    @Test
    public void testGetApprovalInstanceByActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, anyString(),
                anyLong())).thenReturn(userTaskInstanceEntityOptional);
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId("activityId", flowInstanceId);
        assertTrue(result.isPresent());
    }

    /**
     * Test method for {@link FlowableAdaptor#getApprovalInstanceByName(String, Long)}
     */
    @Test
    public void testGetApprovalInstanceByName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, anyString(),
                anyLong())).thenReturn(userTaskInstanceEntityOptional);
        Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName("name", flowInstanceId);
        assertTrue(result.isPresent());
    }

    /**
     * Test method for {@link FlowableAdaptor#getGatewayInstanceByActivityId(String, Long)}
     */
    @Test
    public void testGetGatewayInstanceByActivityId() {
        Optional<GateWayInstanceEntity> gateWayInstanceEntityOptional = Optional.of(new GateWayInstanceEntity());
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, anyString(),
                anyLong())).thenReturn(gateWayInstanceEntityOptional);
        Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId("activityId", flowInstanceId);
        assertTrue(result.isPresent());
    }

    /**
     * Test method for {@link FlowableAdaptor#setFlowableElement(BaseFlowNodeInstance, FlowableElement)}
     */
    @Test
    public void testSetFlowableElement() {
        FlowableElementTestBuilder flowableElementTestBuilder = new FlowableElementTestBuilder();
        FlowableElement flowableElement = flowableElementTestBuilder.build();
        BaseFlowNodeInstance baseFlowNodeInstance = new BaseFlowNodeInstance();
        baseFlowNodeInstance.setId(1L);
        baseFlowNodeInstance.setNodeType(FlowNodeType.USER_TASK);
        baseFlowNodeInstance.setFlowInstanceId(flowInstanceId);
        FlowableElementType flowableElementType = flowableElementTestBuilder.getFlowableElementType();

        flowableAdaptorImpl.setFlowableElement(baseFlowNodeInstance, flowableElement);
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setInstanceType(FlowNodeType.USER_TASK);
        nodeInstanceEntity.setActivityId(flowableElement.getActivityId());
        nodeInstanceEntity.setName(flowableElement.getName());
        verify(nodeInstanceRepository).save(nodeInstanceEntity);
    }
}