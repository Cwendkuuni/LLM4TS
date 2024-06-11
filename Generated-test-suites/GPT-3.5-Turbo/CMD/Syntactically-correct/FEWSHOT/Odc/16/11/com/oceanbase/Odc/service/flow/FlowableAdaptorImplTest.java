package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

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

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setNodeInstanceEntityRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setNodeRepository(nodeRepository);
        flowableAdaptor.setSequenceRepository(sequenceRepository);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "12345";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdWhenNoResult() {
        String processInstanceId = "12345";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "123";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "123";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals("12345", result.get());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "task1";
        Long flowInstanceId = 1L;
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetTaskInstanceByActivityIdWhenNoResult() {
        String activityId = "task1";
        Long flowInstanceId = 1L;
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.empty());
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "approval1";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityIdWhenNoResult() {
        String activityId = "approval1";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approval1";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), eq(name), eq(flowInstanceId)))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetApprovalInstanceByNameWhenNoResult() {
        String name = "approval1";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class), eq(name), eq(flowInstanceId)))
                .thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "gateway1";
        Long flowInstanceId = 1L;
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityIdWhenNoResult() {
        String activityId = "gateway1";
        Long flowInstanceId = 1L;
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.empty());
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("task1", "Task 1", FlowableElementType.TASK);
        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(eq(nodeInstance.getId()), eq(nodeInstance.getNodeType()), eq(flowableElement.getType())))
                .thenReturn(Collections.emptyList());
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        verify(nodeInstanceRepository, times(1)).save(any(NodeInstanceEntity.class));
    }
}