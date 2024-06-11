import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.common.event.EventPublisher;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock 
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private SequenceInstanceRepository sequenceRepository;
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private FormService formService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private FlowNodeType flowNodeType;

    @Mock
    private FlowInstanceEntity flowInstanceEntity;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setup() {
        flowableAdaptorImpl = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        Mockito.when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).findById(flowInstanceId);
        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Mockito.when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(List.of(flowInstanceEntity));

        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).findAll(Mockito.any());
    }

    // Add more test cases for other methods
}