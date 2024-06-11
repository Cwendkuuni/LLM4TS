import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
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
import com.oceanbase.odc.service.flow.FlowableElementTestData;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElementTestDataBuilder;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = FlowableAdaptorImpl.class)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:testdb", "spring.datasource.driverClassName=org.h2.Driver"})
@ActiveProfiles("test")
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptor adaptor;

    @Autowired
    private FlowInstanceRepository instanceRepository;

    @Autowired
    private NodeInstanceEntityRepository nodeRepository;

    @Autowired
    private SequenceInstanceRepository sequenceRepository;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Autowired
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Autowired
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private OdcRuntimeDelegateMapper runtimeDelegateMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private static List<FlowableElementTestData> testDataList() {
        return Arrays.asList(new FlowableElementTestDataBuilder().build());
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @MethodSource("testDataList")
    public void testSetProcessInstanceId(FlowableElementTestData testData) {
        Long flowInstanceId = 1L;
        String processInstanceId = "process-instance-id";
        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<FlowInstanceEntity> optional = instanceRepository.findById(flowInstanceId);
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, optional::isPresent);
        assertEquals(processInstanceId, optional.get().getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process-instance-id";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId(processInstanceId);
        entity.setId(flowInstanceId);

        when(instanceRepository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(Arrays.asList(entity));

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "process-definition-id";
        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<FlowInstanceEntity> optional = instanceRepository.findById(flowInstanceId);
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", flowInstanceId, optional::isPresent);
        assertEquals(processDefinitionId, optional.get().getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "process-definition-id";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessDefinitionId(processDefinitionId);
        entity.setId(flowInstanceId);

        when(instanceRepository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(Arrays.asList(entity));

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process-instance-id";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId(processInstanceId);
        entity.setId(flowInstanceId);

        when(instanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.END;

        List<NodeInstanceEntity> entities = Arrays.asList(
                new NodeInstanceEntity(), new NodeInstanceEntity());

        NodeInstanceEntity expectedNodeInstance = new NodeInstanceEntity();
        expectedNodeInstance.setInstanceId(instanceId);
        expectedNodeInstance.setInstanceType(instanceType);
        expectedNodeInstance.setFlowableElementType(flowableElementType);
        expectedNodeInstance.setFlowInstanceId(1L);
        expectedNodeInstance.setActivityId("activity-id");

        when(nodeRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(Arrays.asList(expectedNodeInstance));

        List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(expectedNodeInstance.getActivityId(), result.get(0).getActivityId());
        assertEquals(expectedNodeInstance.getName(), result.get(0).getName());
        assertEquals(expectedNodeInstance.getFlowableElementType(), result.get(0).getType());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activity-id";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<ServiceTaskInstanceEntity> optional = Optional.of(entity);
        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId))
                .thenReturn(optional);

        Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getRuntimeDelegateMapper());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activity-id";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<UserTaskInstanceEntity> optional = Optional.of(entity);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId))
                .thenReturn(optional);

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getFlowService());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approval-name";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setName(name);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<UserTaskInstanceEntity> optional = Optional.of(entity);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(optional);

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getFlowService());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activity-id";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity entity = new GateWayInstanceEntity();
        entity.setActivityId(activityId);
        entity.setFlowInstanceId(flowInstanceId);

        Optional<GateWayInstanceEntity> optional = Optional.of(entity);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId))
                .thenReturn(optional);

        Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getFlowService());
    }

    @Test
    public void testSetFlowableElement() {
        Long nodeInstanceId = 1L;
        FlowableElement flowableElement = new FlowableElement("activity-id", "name", FlowableElementType.START);

        adaptor.setFlowableElement(new BaseFlowNodeInstance(nodeInstanceId, FlowNodeType.USER_TASK), flowableElement);

        List<NodeInstanceEntity> entities = nodeRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                nodeInstanceId, FlowNodeType.USER_TASK, FlowableElementType.START);

        assertEquals(1, entities.size());
        assertEquals(flowableElement.getActivityId(), entities.get(0).getActivityId());
        assertEquals(flowableElement.getName(), entities.get(0).getName());
        assertEquals(flowableElement.getType(), entities.get(0).getFlowableElementType());
    }
}