import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Test
    public void testSetProcessInstanceId() {
        FlowableAdaptorImpl adaptor = new FlowableAdaptorImpl();
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(Optional.of(flowInstanceId), adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId));
    }

    @Test
    public void testSetProcessDefinitionId() {
        FlowableAdaptorImpl adaptor = new FlowableAdaptorImpl();
        Long flowInstanceId = 1L;
        String processDefinitionId = "54321";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId)).thenReturn(1);

        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        assertEquals(Optional.of(flowInstanceId), adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId));
    }

    // Add more test cases for other methods

}