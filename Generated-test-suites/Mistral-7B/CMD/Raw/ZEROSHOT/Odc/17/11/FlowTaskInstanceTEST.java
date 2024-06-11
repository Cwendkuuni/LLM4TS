package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.test.TestConstants;
import com.oceanbase.odc.service.flow.test.TestUtils;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.test.mock.MockTaskService;
import com.oceanbase.odc.service.flow.test.mock.MockTaskServiceEventPublisher;
import com.oceanbase.odc.service.flow.test.mock.MockTaskServiceNodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.test.mock.MockTaskServiceServiceTaskInstanceEntityRepository;

/**
 * Class FlowTaskInstanceTest
 * <p>
 * Test for {@link FlowTaskInstance}
 *
 * @author yh263208
 * @since ODC_release_3.3.0
 */
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    private MockTaskService mockTaskService;
    private MockTaskServiceEventPublisher mockEventPublisher;
    private MockTaskServiceNodeInstanceEntityRepository mockNodeRepository;
    private MockTaskServiceServiceTaskInstanceEntityRepository mockServiceTaskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockTaskService = new MockTaskService();
        mockEventPublisher = new MockTaskServiceEventPublisher();
        mockNodeRepository = new MockTaskServiceNodeInstanceEntityRepository();
        mockServiceTaskRepository = new MockTaskServiceServiceTaskInstanceEntityRepository();
        flowTaskInstance = new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, delegateConvertor,
                new FlowableAdaptor(null, mockNodeRepository, mockServiceTaskRepository),
                mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository,
                mockServiceTaskRepository);
        TaskService taskService = mock(TaskService.class);
        when(mockTaskService.createTaskQuery().list()).thenReturn(Collections.emptyList());
        when(mockTaskService).thenReturn(taskService);
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(true);
        when(delegateConvertor.map(eq(TaskType.TEST_TASK_TYPE))).thenReturn(new BaseRuntimeFlowableDelegateTest() {});
        when(mockServiceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(invocation -> {
            ServiceTaskInstanceEntity entity = invocation.getArgument(0);
            entity.setId(TestConstants.TEST_ID);
            entity.setCreateTime(TestConstants.TEST_TIME);
            entity.setUpdateTime(TestConstants.TEST_TIME);
            return entity;
        });
        when(mockNodeRepository.getById(TestConstants.TEST_ID)).thenReturn(new FlowNodeInstanceEntity() {});
        when(mockNodeRepository.getById(anyLong())).thenReturn(new FlowNodeInstanceEntity() {});
        when(mockTaskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(TestUtils.createTestTaskList());
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateExist() {
        flowTaskInstance.setId(TestConstants.TEST_ID);
        flowTaskInstance.create();
        throw new IllegalStateException("Test exception");
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = TestUtils.createTestFlowTaskInstanceList(2);
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, mockServiceTaskRepository);
        assertEquals(2, result.size());
        assertEquals(TestConstants.TEST_ID_1, result.get(0).getId());
        assertEquals(TestConstants.TEST_ID_2, result.get(1).getId());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNotFound() {
        flowTaskInstance.setId(TestConstants.TEST_ID);
        throw new IllegalArgumentException("Test exception");
    }

    @Test
    public void testDelete() {
        flowTaskInstance.delete();
        verify(mockServiceTaskRepository).deleteById(TestConstants.TEST_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNotFound() {
        flowTaskInstance.setId(TestConstants.TEST_ID);
        throw new IllegalArgumentException("Test exception");
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegateTest();
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(mockEventPublisher).removeEventListener(any());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(mockEventPublisher).addEventListener(any());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.COMPLETE, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        flowTaskInstance.cancel(true);
        verify(mockTaskService).cancelTask(anyLong(), true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelInterrupt() {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(TestUtils.createTestTask());
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        flowTaskInstance.bindServiceTask(TestUtils.createTestTask());
        flowTaskInstance.getResult();
        verify(mockTaskService).getTaskInfo(anyLong());
    }

    @Test
    public void testGetResultTimeout() throws Exception {
        flowTaskInstance.bindServiceTask(TestUtils.createTestTask());
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        verify(mockTaskService).getTaskInfo(anyLong(), eq(1), eq(TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeoutException() throws Exception {
        flowTaskInstance.bindServiceTask(TestUtils.createTestTask());
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        throw new TimeoutException("Test timeout exception");
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegateTest.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() {
        flowTaskInstance.complete(false);
        assertEquals(FlowNodeStatus.COMPLETE, flowTaskInstance.getStatus());
        verify(mockTaskService).completeAll(anyList());
    }

    @Test
    public void testCompleteAborted() {
        flowTaskInstance.complete(true);
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
        verify(mockTaskService).completeAll(anyList());
    }

}

class BaseRuntimeFlowableDelegateTest implements BaseRuntimeFlowableDelegate<Object> {

    @Override
    public void cancel(boolean mayInterruptIfRunning) {

    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void complete() {

    }

    @Override
    public void complete(Map<String, ?> variables) {

    }

    @Override
    public Map<String, Object> getVariables() {
        return null;
    }

    @Override
    public String getName() {
        return "TestTaskName";
    }

    @Override
    public void setTaskInstanceId(String taskInstanceId) {

    }

    @Override
    public String getTargetTaskInstanceId() {
        return null;
    }

    @Override
    public String getParentTaskInstanceId() {
        return null;
    }

    @Override
    public void setParentTaskInstanceId(String parentTaskInstanceId) {

    }

    @Override
    public void setExecutionTime(long executionTime) {

    }

    @Override
    public long getExecutionTime() {
        return 0;
    }

    @Override
    public void setProcessInstanceId(String processInstanceId) {

    }

    @Override
    public String getProcessInstanceId() {
        return null;
    }

    @Override
    public void setClientId(String clientId) {

    }

    @Override
    public String getClientId() {
        return null;
    }

    @Override
    public void setTenantId(String tenantId) {

    }

    @Override
    public String getTenantId() {
        return null;
    }

    @Override
    public void setTenantTaskDefinitionKey(String tenantTaskDefinitionKey) {

    }

    @Override
    public String getTenantTaskDefinitionKey() {
        return null;
    }

    @Override
    public void setProcessDefinitionKey(String processDefinitionKey) {

    }

    @Override
    public String getProcessDefinitionKey() {
        return null;
    }

    @Override
    public void setBusinessKey(Object businessKey) {

    }

    @Override
    public Object getBusinessKey() {
        return null;
    }

    @Override
    public void setProcessInstanceName(String processInstanceName) {

    }

    @Override
    public String getProcessInstanceName() {
        return null;
    }

    @Override
    public void setProcessInstanceBusinessKey(Object businessKey) {

    }

    @Override
    public Object getProcessInstanceBusinessKey() {
        return null;
    }

    @Override
    public void setExecutionId(String executionId) {

    }

    @Override
    public String getExecutionId() {
        return null;
    }

    @Override
    public void setAsyncBeforeExec(Runnable asyncBeforeExec) {

    }

    @Override
    public void setAsyncAfterExec(Runnable asyncAfterExec) {

    }

    @Override
    public void setAsyncBeforeCompletion(Runnable asyncBeforeCompletion) {

    }

    @Override
    public void setAsyncAfterCompletion(Runnable asyncAfterCompletion) {

    }

    @Override
    public void setVariableLocal(String variableName, Object value) {

    }

    @Override
    public Object getVariableLocal(String variableName) {
        return null;
    }

    @Override
    public void removeVariableLocal(String variableName) {

    }

    @Override
    public void removeAllVariablesLocal() {

    }

    @Override
    public void signalEventDefinition(String eventDefinitionId) {

    }

    @Override
    public void signalEventDefinition(String eventDefinitionId, Object variable) {

    }

    @Override
    public void signalEventSubProcessInstance(Long subProcessInstanceId) {

    }

    @Override
    public void setVariable(String variableName, Object value) {

    }

    @Override
    public Object getVariable(String variableName) {
        return null;
    }

    @Override
    public void removeVariable(String variableName) {

    }

    @Override
    public void removeAllVariables() {

    }

    @Override
    public void setVariableLocal(String variableName, Object value) {

    }

    @Override
    public Object getVariableLocal(String variableName) {
        return null;
    }

    @Override
    public void removeVariableLocal(String variableName) {

    }

    @Override
    public void removeAllVariablesLocal() {

    }

    @Override
    public void signalEventDefinition(String eventDefinitionId, String expression) {

    }

    @Override
    public void signalEventDefinition(String eventDefinitionId, String expression, Object variable) {

    }

    @Override
    public void signalEventSubProcessInstance(Long subProcessInstanceId, String signalName) {

    }

    @Override
    public void executeAsyncBefore() {

    }

    @Override
    public void executeAsyncAfter() {

    }

    @Override
    public void executeAsyncBeforeCompletion() {

    }

    @Override
    public void executeAsyncAfterCompletion() {

    }

    @Override
    public void executeNativeScript(String script) throws Exception {

    }

    @Override
    public Map<String, Object> getVariablesLocal() {
        return Collections.emptyMap();
    }

    @Override
    public void setVariableLocal(Map<String, Object> variables) {

    }

    @Override
    public Map<String, Object> getVariablesGlobal() {
        return Collections.emptyMap();
    }

    @Override
    public void setVariableGlobal(Map<String, Object> variables) {

    }

    @Override
    public void setVariableGlobal(Map<String, Object> variables, boolean merge) {

    }

    @Override
    public void setVariableLocal(Map<String, Object> variables) {

    }

    @Override
    public void setVariableLocal(Map<String, Object> variables, boolean merge) {

    }

}