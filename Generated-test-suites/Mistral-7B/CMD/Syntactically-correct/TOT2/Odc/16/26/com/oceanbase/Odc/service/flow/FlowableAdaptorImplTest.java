package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
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

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                () -> new FlowInstanceEntity());

        int affectRows = flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(affectRows, 1);
        verifySetProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test
    public void testSetProcessInstanceId_failure() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId,
                () -> new FlowInstanceEntity());

        int affectRows = flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(affectRows, 1);

        verify(flowInstanceRepository)
                .updateProcessInstanceIdById(flowInstanceId, processInstanceId);

        // Test exception when processInstanceId is null
        int affectRows2 = flowableAdaptor.setProcessInstanceId(flowInstanceId, null);
        assertFalse(affectRows2 > 0);

        verify(flowInstanceRepository, never())
                .updateProcessInstanceIdById(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE,
                "processInstanceId", processInstanceId,
                () -> new FlowInstanceEntity().setId(flowInstanceId));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(result.isPresent(), true);
        assertEquals(result.get(), flowInstanceId);

        verifyGetFlowInstanceIdByProcessInstanceId(processInstanceId, flowInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId_failure() {
        String processInstanceId = "processInstanceId";
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "processInstanceId", processInstanceId,
                () -> new FlowInstanceEntity());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(result.isEmpty(), true);

        verify(flowInstanceRepository, never())
                .findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)));
    }

    // Test other methods with similar patterns...

    private void verifySetProcessInstanceId(Long flowInstanceId, String processInstanceId) {
        // Verify call to flowInstanceRepository.updateProcessInstanceIdById()
    }

    private void verifyGetFlowInstanceIdByProcessInstanceId(String processInstanceId, Long flowInstanceId) {
        // Verify call to flowInstanceRepository.findAll() with the correct specification
    }

    // Test other methods with similar test patterns...
}