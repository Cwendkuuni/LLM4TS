import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowableElement;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        // Test case for setting process instance id
        // Typical Use Case
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test case for getting flow instance id by process instance id
        // Error Scenario
        String processInstanceId = "12345";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        // Test case for getting flowable element by type
        // Edge Case
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType elementType = FlowableElementType.TASK;
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, elementType))
                .thenReturn(Collections.emptyList());
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, elementType);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // Test case for getting task instance by activity id
        // Typical Use Case
        String activityId = "task001";
        Long flowInstanceId = 1L;
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // Test case for getting approval instance by activity id
        // Error Scenario
        String activityId = "approval001";
        Long flowInstanceId = 1L;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.empty());
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // Test case for getting gateway instance by activity id
        // Edge Case
        String activityId = "gateway001";
        Long flowInstanceId = 1L;
        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), eq(activityId), eq(flowInstanceId)))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        // Test case for setting flowable element
        // Typical Use Case
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.SERVICE_TASK);
        FlowableElement flowableElement = new FlowableElement("task001", "Task 001", FlowableElementType.TASK);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        verify(nodeInstanceEntityRepository, times(1)).save(any(NodeInstanceEntity.class));
    }

}