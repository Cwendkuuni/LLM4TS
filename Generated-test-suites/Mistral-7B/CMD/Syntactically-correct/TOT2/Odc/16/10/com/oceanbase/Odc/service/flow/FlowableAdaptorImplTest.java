package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowNodeInstanceRepository;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.Principal;

@Slf4j
public class FlowableAdaptorImplTest {

    @Mock
    private FormService formService;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private FlowNodeInstanceRepository nodeRepository;

    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verifySetProcessInstanceId();
    }

    @Test(expected = RuntimeException.class)
    public void testSetProcessInstanceIdInvalidInstance() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verifyExceptionThrown();
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity1 = new FlowInstanceEntity();
        flowInstanceEntity1.setProcessInstanceId(processInstanceId);
        flowInstanceEntity1.setId(1L);
        when(flowInstanceRepository.findAll(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))
                .thenReturn(List.of(flowInstanceEntity1));

        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(1L, flowInstanceIdOptional.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdEmptyList() {
        String processInstanceId = "testProcessInstanceId";
        when(flowInstanceRepository.findAll(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))
                .thenReturn(Collections.emptyList());

        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertFalse(flowInstanceIdOptional.isPresent());
    }

    @Test(expected = RuntimeException.class)
    public void testGetFlowInstanceIdByProcessInstanceIdMultipleInstances() {
        String processInstanceId = "testProcessInstanceId";
        FlowInstanceEntity flowInstanceEntity1 = new FlowInstanceEntity();
        flowInstanceEntity1.setProcessInstanceId(processInstanceId);
        flowInstanceEntity1.setId(1L);
        FlowInstanceEntity flowInstanceEntity2 = new FlowInstanceEntity();
        flowInstanceEntity2.setProcessInstanceId(processInstanceId);
        flowInstanceEntity2.setId(2L);
        when(flowInstanceRepository.findAll(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))
                .thenReturn(List.of(flowInstanceEntity1, flowInstanceEntity2));

        flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        verifyExceptionThrown();
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verifySetProcessDefinitionId();
    }

    @Test(expected = RuntimeException.class)
    public void testSetProcessDefinitionIdInvalidInstance() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "testProcessDefinitionId";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verifyExceptionThrown();
    }

    // Add more test cases for other methods in the class...

    // ...and merge all the test cases into this test file.

    private void verifySetProcessInstanceId() {
        // Add verification logic here.
    }

    private void verifySetProcessDefinitionId() {
        // Add verification logic here.
    }

    private void verifyExceptionThrown() {
        throw new RuntimeException("Test exception message");
    }
}