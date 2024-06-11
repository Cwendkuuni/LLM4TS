import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.context.FlowableEngineConfigurationImpl;
import org.flowable.engine.impl.interceptor.CommandExecutorImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.ServiceTask;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestRule;
import org.flowable.engine.test.mock.MockCommandExecutor;
import org.flowable.engine.test.mock.MockExecutionEntity;
import org.flowable.engine.test.mock.MockProcessDefinitionEntity;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableEngineConfiguration;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@Slf4j
public class FlowTaskInstanceTest {

    @ClassRule
    public static FlowableTestRule flowableTestRule = new FlowableTestRule();

    private static final String PROCESS_DEFINITION_KEY = "processDefinitionKey";

    private static final String USER_TASK_NAME = "userTaskName";

    private static final String SERVICE_TASK_NAME = "serviceTaskName";

    private FlowableEngineConfiguration flowableEngineConfiguration;

    private CommandExecutorImpl commandExecutor;

    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    private RuntimeDelegateMapper runtimeDelegateMapper;

    private FlowableAdaptor flowableAdaptor;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowableEngineConfiguration = Mockito.spy(new FlowableEngineConfigurationImpl());
        CommandContext commandContext = Mockito.mock(CommandContext.class);
        CommandContextUtil.setCommandContext(commandContext);
        FlowableEngineConfiguration config = Mockito.spy(FlowableEngineConfiguration.class);
        Mockito.when(config.getEngineConfiguration()).thenReturn(flowableEngineConfiguration);
        commandExecutor = new MockCommandExecutor();
        Mockito.when(flowableEngineConfiguration.getCommandExecutor()).thenReturn(commandExecutor);
        serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        runtimeDelegateMapper = Mockito.mock(RuntimeDelegateMapper.class);
        flowableAdaptor = new FlowableAdaptor(serviceTaskInstanceRepository, runtimeDelegateMapper);

        FlowableEngineConfiguration config2 = new FlowableEngineConfiguration();
        config2.setFlowableEngineConfiguration(flowableEngineConfiguration);
        Mockito.when(flowableEngineConfiguration.getFlowableEngineConfiguration()).thenReturn(config2);

        MockProcessDefinitionEntity processDefinitionEntity = new MockProcessDefinitionEntity();
        Mockito.when(processDefinitionEntity.getId()).thenReturn(1L);
        Mockito.when(processDefinitionEntity.getKey()).thenReturn(PROCESS_DEFINITION_KEY);
        ProcessDefinitionEntity processDefinition = processDefinitionEntity;

        MockExecutionEntity executionEntity = new MockExecutionEntity();
        executionEntity.setId(1L);
        Mockito.when(serviceTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));

        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getRuntimeService())
                .thenReturn(Mockito.mock(RuntimeService.class));
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getEventNotifier())
                .thenReturn(Mockito.mock(EventNotifier.class));

        Mockito.when(runtimeDelegateMapper.map(TaskType.USER_TASK)).thenReturn(UserTaskRuntimeDelegate.class);
        Mockito.when(runtimeDelegateMapper.map(TaskType.SERVICE_TASK)).thenReturn(ServiceTaskRuntimeDelegate.class);

        flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), runtimeDelegateMapper, flowableAdaptor,
                Mockito.mock(EventPublisher.class), Mockito.mock(TaskService.class), serviceTaskInstanceRepository,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class));
        flowTaskInstance.setFlowDefinition(processDefinition);
        flowTaskInstance.setExecution(executionEntity);
    }

    @Test
    @Deployment(resources = {"test.bpmn20.xml"})
    public void testCreateFlowTaskInstance() throws Exception {
        // Given
        FlowNodeType flowNodeType = FlowNodeType.SERVICE_TASK;
        Long organizationId = RandomUtils.nextLong();
        Long flowInstanceId = RandomUtils.nextLong();
        Long targetTaskId = RandomUtils.nextLong();
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, null);

        // When
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, organizationId, flowInstanceId,
                strategyConfig, false, false, runtimeDelegateMapper, flowableAdaptor, Mockito.mock(EventPublisher.class),
                Mockito.mock(TaskService.class), serviceTaskInstanceRepository, Mockito.mock(NodeInstanceEntityRepository.class),
                Mockito.mock(SequenceInstanceRepository.class));

        flowTaskInstance.create();

        // Then
        assertNotNull(flowTaskInstance.getId());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(TaskType.SERVICE_TASK, flowTaskInstance.getTaskType());
        assertEquals(strategyConfig.getStrategy(), flowTaskInstance.getStrategyConfig().getStrategy());
        assertNotNull(flowTaskInstance.getCreationTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
        assertEquals(false, flowTaskInstance.isStartEndpoint());
        assertEquals(false, flowTaskInstance.isEndEndPoint());
    }

    @Test
    public void testBindServiceTaskDelegate() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(ServiceTaskRuntimeDelegate.class);
        FlowTaskInstance flowTaskInstance = Mockito.spy(flowTaskInstance);

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAllocAndDeallocListeners() {
        // Given
        FlowTaskInstance flowTaskInstance = Mockito.spy(flowTaskInstance);
        Mockito.verify(flowTaskInstance.getEventPublisher()).removeEventListener(Mockito.any());
        Mockito.verify(flowTaskInstance.getEventPublisher()).addEventListener(Mockito.any());

        // When
        flowTaskInstance.dealloc();

        // Then
        Mockito.verify(flowTaskInstance.getEventPublisher()).removeEventListener(Mockito.any());

        // When
        flowTaskInstance.alloc();

        // Then
        Mockito.verify(flowTaskInstance.getEventPublisher()).addEventListener(Mockito.any());
    }

    @Test
    public void testCompleteFlowTaskInstance() throws InterruptedException, ExecutionException {
        // Given
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));
        Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyLong()).list())
                .thenReturn(Collections.singletonList(new TaskEntity()));

        // When
        flowTaskInstance.complete(false);

        // Then
        Mockito.verify(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .complete(Mockito.anyLong(), Mockito.anyMap());
    }

    @Test
    public void testAbortFlowTaskInstance() throws InterruptedException, ExecutionException {
        // Given
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));
        Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyLong()).list())
                .thenReturn(Collections.singletonList(new TaskEntity()));

        // When
        flowTaskInstance.abort();

        // Then
        Mockito.verify(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .complete(Mockito.anyLong(), Mockito.anyMap());
        Mockito.verify(flowTaskInstance.getListener()).taskAborted(Mockito.any());
    }

    @Test
    public void testCancelFlowTaskInstance() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));
        Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyLong()).list())
                .thenReturn(Collections.singletonList(new TaskEntity()));

        // When
        flowTaskInstance.cancel(true);

        // Then
        Mockito.verify(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .cancel(Mockito.anyLong(), Mockito.anyBoolean());
    }

    @Test
    public void testGetResultFlowTaskInstance() throws InterruptedException, ExecutionException {
        // Given
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));
        Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyLong()).list())
                .thenReturn(Collections.singletonList(new TaskEntity()));

        // When
        Object result = flowTaskInstance.getResult();

        // Then
        Mockito.verify(flowableEngineConfiguration.getProcessEngineServices().getTaskService()).get(Mockito.anyLong());
        assertEquals(result, flowTaskInstance.getTargetTaskHandle().get());
    }

    @Test
    public void testGetResultFlowTaskInstanceWithTimeout() throws InterruptedException, ExecutionException,
            TimeoutException {
        // Given
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));
        Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyLong()).list())
                .thenReturn(Collections.singletonList(new TaskEntity()));

        // When
        Object result = flowTaskInstance.getResult(500, TimeUnit.MILLISECONDS);

        // Then
        Mockito.verify(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .get(Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
        assertEquals(result, flowTaskInstance.getTargetTaskHandle().get(500, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testIsDoneFlowTaskInstance() {
        // Given
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));
        Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyLong()).list())
                .thenReturn(Collections.singletonList(new TaskEntity()));

        // When
        boolean done = flowTaskInstance.isDone();

        // Then
        assertTrue(done);
    }

    @Test
    public void testIsPresentOnThisMachineFlowTaskInstance() {
        // Given
        Mockito.when(flowableEngineConfiguration.getProcessEngineServices().getTaskService())
                .thenReturn(Mockito.mock(TaskService.class));
        Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyLong()).list())
                .thenReturn(Collections.singletonList(new TaskEntity()));

        // When
        boolean present = flowTaskInstance.isPresentOnThisMachine();

        // Then
        assertTrue(present);
    }

    interface EventNotifier {}

    interface RuntimeService {}

    static class UserTaskRuntimeDelegate implements BaseRuntimeFlowableDelegate<UserTaskRuntimeDelegate> {
    }

    static class ServiceTaskRuntimeDelegate implements BaseRuntimeFlowableDelegate<ServiceTaskRuntimeDelegate> {
    }

    static class MockCommandExecutor implements CommandExecutorImpl {
        @Override
        public void execute(CommandContext commandContext, Command<?> command) {

        }

        @Override
        public void execute(CommandContext commandContext, Command<?> command, Object... arguments) {

        }

        @Override
        public <T> T execute(CommandContext commandContext, Command<T> command, Object... arguments) {
            return null;
        }
    }
}