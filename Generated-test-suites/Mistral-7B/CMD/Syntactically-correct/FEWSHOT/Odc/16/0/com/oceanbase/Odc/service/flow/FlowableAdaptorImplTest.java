package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeStub;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeTest;
import com.oceanbase.odc.service.iam.auth.UserContext;
import com.oceanbase.odc.service.iam.auth.UserContextMock;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private NodeInstanceEntityRepository nodeInstanceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;
    private EventPublisher eventPublisherMock;
    private NodeInstanceEntityRepository nodeRepositoryMock;
    private SequenceInstanceRepository sequenceRepositoryMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    private TaskService taskServiceMock;
    private FormService formServiceMock;
    private AuthenticationFacade authenticationFacadeMock;
    private GateWayInstanceRepository gateWayInstanceRepositoryMock;

    @Before
    public void setup() {
        nodeInstanceRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        nodeRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceRepositoryMock = Mockito.mock(SequenceInstanceRepository.class);
        flowInstanceRepositoryMock = Mockito.mock(FlowInstanceRepository.class);
        userTaskInstanceRepositoryMock = Mockito.mock(UserTaskInstanceRepository.class);
        taskServiceMock = Mockito.mock(TaskService.class);
        formServiceMock = Mockito.mock(FormService.class);
        authenticationFacadeMock = Mockito.mock(AuthenticationFacade.class);
        gateWayInstanceRepositoryMock = Mockito.mock(GateWayInstanceRepository.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepositoryMock);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepositoryMock);
        flowableAdaptor.setEventPublisher(eventPublisherMock);
        flowableAdaptor.setNodeRepository(nodeRepositoryMock);
        flowableAdaptor.setSequenceRepository(sequenceRepositoryMock);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepositoryMock);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepositoryMock);
        flowableAdaptor.setTaskService(taskServiceMock);
        flowableAdaptor.setFormService(formServiceMock);
        flowableAdaptor.setAuthenticationFacade(authenticationFacadeMock);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepositoryMock);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        Mockito.doNothing().when(flowInstanceRepositoryMock).updateProcessInstanceIdById(flowInstanceId,
                processInstanceId);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepositoryMock, Mockito.times(1)).updateProcessInstanceIdById(flowInstanceId,
                processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);

        Mockito.when(flowInstanceRepositoryMock.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");

        Mockito.verify(flowInstanceRepositoryMock, Mockito.times(1)).findAll(Mockito.any(Specification.class));
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    // Add other test methods for other methods in FlowableAdaptorImpl

    @Test
    public void testInnerConvert() {
        GateWayInstanceEntity gateway = Mockito.mock(GateWayInstanceEntity.class);
        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);

        Mockito.when(gatewayRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "activityId", 1L))
                .thenReturn(Optional.of(gateway));

        Optional<FlowGatewayInstance> result = flowableAdaptor.innerConvert(
                Optional.of(gateway), flowableAdaptor,
                        (gateway, flowService) -> new FlowGatewayInstance(gateway, flowService, nodeRepositoryMock,
                                sequenceRepositoryMock, gateWayInstanceRepositoryMock));

        Mockito.verify(gatewayRepositoryMock, Mockito.times(1))
                .findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "activityId", 1L);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }
}