package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import org.mockito.quality.Strictness;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    @Mock
    private FormService formService;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

    @InjectMocks(strictness = Strictness.LENIENT)
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullFlowInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(null, "processInstanceId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullProcessInstanceId() {
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(
                Arrays.asList(new FlowInstanceEntity(flowInstanceId)));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdWhenMultipleInstancesExist() {
        String processInstanceId = "processInstanceId";
        Long firstFlowInstanceId = 1L;
        Long secondFlowInstanceId = 2L;

        when(flowInstanceRepository.findAll(eq(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))).thenReturn(
                Arrays.asList(new FlowInstanceEntity(firstFlowInstanceId), new FlowInstanceEntity(secondFlowInstanceId)));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertFalse(result.isPresent());
    }

    // Add test cases for the remaining methods in the same manner

}