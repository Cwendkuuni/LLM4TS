
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {
    @InjectMocks
    FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    EventPublisher eventPublisher;
    @Mock
    NodeInstanceEntityRepository nodeRepository;
    @Mock
    SequenceInstanceRepository sequenceRepository;
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(1L, "processId");
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(anyLong(), anyString());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("processId");
        verify(flowInstanceRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptorImpl.setProcessDefinitionId(1L, "processId");
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(anyLong(), anyString());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("processId");
        verify(flowInstanceRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        verify(flowInstanceRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetFlowableElementByType() {
        flowableAdaptorImpl.getFlowableElementByType(1L, FlowNodeType.APPROVAL_TASK, FlowableElementType.USERTASK);
        verify(nodeInstanceRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any());
    }

    // continue for the rest of the methods...
}
