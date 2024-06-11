package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableAdaptorTest;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.GateWayInstanceRepository;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;

/**
 * Test class for {@link FlowableAdaptorImpl}
 */
public class FlowableAdaptorImplTest extends FlowableAdaptorTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private FormService formService;

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptorImpl.setAuthenticationFacade(authenticationFacade);
        flowableAdaptorImpl.setFormService(formService);
        flowableAdaptorImpl.setTaskService(taskService);
        flowableAdaptorImpl.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptorImpl.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptorImpl.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptorImpl.setGateWayInstanceRepository(gateWayInstanceRepository);
        flowableAdaptorImpl.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptorImpl.setSequenceRepository(sequenceRepository);
        flowableAdaptorImpl.setOdcRuntimeDelegateMapper(odcRuntimeDelegateMapper);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        int affectRows = 1;

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
        verify(log).info("Binding Flow/Process Instance Id exception, flowInstanceId={}, processInstanceId={}, affectRows={}",
                flowInstanceId, processInstanceId, affectRows);
        verify(log).info(equals("AffectRows has to be equal to one"));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";

        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));
        List<FlowInstanceEntity> entityList = Collections.singletonList(new FlowInstanceEntity());

        when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId_emptyResult() {
        String processInstanceId = "processInstanceId";

        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));
        List<FlowInstanceEntity> entityList = Collections.emptyList();

        when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

        assertNull(flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId));
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                Optional.of(new FlowInstanceEntity()));

        int affectRows = 1;

        flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
        verify(log).info("Binding Flow/Process Instance Id exception, flowInstanceId={}, processDefinitionId={}, affectRows={}",
                flowInstanceId, processDefinitionId, affectRows);
        verify(log).info(equals("AffectRows has to be equal to one"));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";

        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId));
        List<FlowInstanceEntity> entityList = Collections.singletonList(new FlowInstanceEntity());

        when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(1L, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId_emptyResult() {
        String processDefinitionId = "processDefinitionId";

        Specification<FlowInstanceEntity> specification =
                Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId));
        List<FlowInstanceEntity> entityList = Collections.emptyList();

        when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

        assertNull(flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId));
    }

    // ... (Continue generating test cases for other methods)

}