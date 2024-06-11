import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class FlowableAdaptorImplTest {

    private NodeInstanceEntityRepository nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
    private EventPublisher eventPublisher = mock(EventPublisher.class);
    private FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
    private UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
    private TaskService taskService = mock(TaskService.class);
    private FormService formService = mock(FormService.class);
    private AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
    private GateWayInstanceRepository gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
  
    private FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();
  
    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123456";
  
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);
  
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
  
        assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).orElse(null));
    }
  
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "123456";
  
        when(flowInstanceRepository.findAll(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity()));
  
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
  
        assertTrue(flowInstanceId.isPresent());
    }

    // Add more test methods for other functionalities

}