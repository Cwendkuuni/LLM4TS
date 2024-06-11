import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.common.api.FlowableEngineConstants;
import org.flowable.engine.impl.context.FlowableEngineContext;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.engine.runtime.Task;
import org.flowable.engine.runtime.TaskBuilder;
import org.flowable.task.api.event.TaskEventType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableEngineTest;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest extends FlowableEngineTest {

 private static final String PROCESS_DEFINITION_KEY = "ProcessDefinitionKey";
 private static final String FLOW_INSTANCE_ID = "FlowInstanceId";
 private static final String TASK_DEFINITION_KEY = "TaskDefinitionKey";
 private static final String USER_TASK_NAME = "UserTaskName";
 private static final String TASK_TYPE = TaskType.SERVICE_CALL.name();
 private static final long TASK_ID = 1L;
 private static final long ORGANIZATION_ID = 1L;
 private static final long NODE_ID = 1L;
 private static final long FLOW_NODE_ID = 1L;
 private FlowableAdaptor flowableAdaptor;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private RuntimeDelegateMapper runtimeDelegateMapper;
 private FlowTaskInstance flowTaskInstance;
 private BaseRuntimeFlowableDelegate<String> targetTaskHandle;
 private ServiceTaskStartedListener listener;

 @Before
 public void setUp() {
 serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 runtimeDelegateMapper = Mockito.mock(RuntimeDelegateMapper.class);
 flowableAdaptor = new FlowableAdaptor(runtimeDelegateMapper, getRuntimeService(), serviceTaskInstanceRepository);
 listener = Mockito.mock(ServiceTaskStartedListener.class);
 flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), runtimeDelegateMapper, flowableAdaptor,
 getEventPublisher(), getTaskService(), getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
 serviceTaskInstanceRepository);
 flowTaskInstance.setId(NODE_ID);
 targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 when(runtimeDelegateMapper.map(TASK_TYPE)).thenReturn(targetTaskHandle.getClass());
 when(getTaskService().createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list().size())
 .thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 when(getTaskService().createTaskQuery().taskDefinitionKey(TASK_DEFINITION_KEY).list().get(0).getId())
 .thenReturn(TASK_ID);
 when(getTaskService().createTaskQuery().taskDefinitionKey(TASK_DEFINITION_KEY).list().get(0).getName())
 .thenReturn(USER_TASK_NAME);
 when(getEventPublisher().removeEventListener(eq(listener))).thenReturn(null);
 when(getEventPublisher().addEventListener(any()))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 listener = (ServiceTaskStartedListener) invocation.getArgument(0);
 return null;
 }
 });
 }

 @Test
 public void testCreateFlowTaskInstance() {
 // GIVEN
 String processDefinitionKey = PROCESS_DEFINITION_KEY;
 FlowableEngineContext engineContext = getFlowableEngineContext();
 ProcessInstance processInstance = new ProcessInstanceBuilder().processDefinitionKey(processDefinitionKey)
 .name("ProcessName")
 .start();
 engineContext.setProcessInstance(processInstance);

 // WHEN
 flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), runtimeDelegateMapper, flowableAdaptor,
 getEventPublisher(), getTaskService(), getNodeInstanceEntityRepository(), getSequenceInstanceRepository(),
 serviceTaskInstanceRepository);
 flowTaskInstance.create();

 // THEN
 assertEquals("FlowTaskInstance created with correct initialization", FLOW_INSTANCE_ID, flowTaskInstance.getFlowInstanceId());
 assertEquals("FlowTaskInstance status is created", FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
 assertEquals("FlowTaskInstance id is set", NODE_ID, flowTaskInstance.getId());
 verify(serviceTaskInstanceRepository, times(1)).save(eq(new ServiceTaskInstanceEntity()));
 }

 @Test
 public void testGetCoreFlowableElementType() {
 // GIVEN
 // WHEN
 FlowNodeType flowNodeType = flowTaskInstance.getCoreFlowableElementType();

 // THEN
 assertEquals("FlowNodeType is SERVICE_TASK", FlowNodeType.SERVICE_TASK, flowNodeType);
 }

 @Test
 public void testBindServiceTask() {
 // GIVEN
 Task task = Mockito.mock(Task.class);
 when(getTaskService().createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list().get(0)).thenReturn(task);
 when(task.getId()).thenReturn(TASK_ID);

 // WHEN
 flowTaskInstance.bindServiceTask(targetTaskHandle);

 // THEN
 assertEquals("Task status is executing", FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 verify(targetTaskHandle, times(1)).bind(eq(flowableAdaptor));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeallocIfTaskNotPresent() {
 // GIVEN
 // WHEN
 flowTaskInstance.dealloc();

 // THEN
 fail("Dealloc should throw UnsupportedOperationException when task is not present on machine");
 }

 @Test
 public void testAlloc() {
 // GIVEN
 // WHEN
 flowTaskInstance.alloc();

 // THEN
 verify(getEventPublisher(), times(1)).addEventListener(any());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeleteIfTaskNotExists() {
 // GIVEN
 doNothing().when(serviceTaskInstanceRepository).deleteById(NODE_ID);

 // WHEN
 flowTaskInstance.delete();

 // THEN
 fail("Delete should throw UnsupportedOperationException when task is not present on machine");
 }

 @Test
 public void testComplete() {
 // GIVEN
 when(taskService.complete(TASK_ID, anyMap())).thenReturn(null);

 // WHEN
 flowTaskInstance.complete(false);

 // THEN
 verify(listener, times(1)).onTaskStarted(any(TaskEvent.class));
 verify(getTaskService(), times(1)).complete(TASK_ID, anyMap());
 }

 @Test
 public void testAbort() {
 // GIVEN
 when(taskService.complete(TASK_ID, anyMap())).thenThrow(new RuntimeException("TestException"));
 when(taskService.cancel(TASK_ID, true)).thenReturn(null);

 // WHEN
 flowTaskInstance.abort();

 // THEN
 verify(listener, never()).onTaskStarted(any(TaskEvent.class));
 verify(getTaskService(), times(1)).cancel(TASK_ID, true);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCancelIfTaskNotPresent() {
 // GIVEN
 // WHEN
 flowTaskInstance.cancel(true);

 // THEN
 fail("Cancel should throw UnsupportedOperationException when task is not present on machine");
 }

 @Test
 public void testIsDone() {
 // GIVEN
 when(targetTaskHandle.isDone()).thenReturn(true);

 // WHEN
 boolean result = flowTaskInstance.isDone();

 // THEN
 assertTrue("Task is done", result);
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // GIVEN
 String result = "TestResult";
 when(targetTaskHandle.get()).thenReturn(result);

 // WHEN
 String resultFromFlowTaskInstance = flowTaskInstance.getResult();

 // THEN
 assertEquals("Result is correct", result, resultFromFlowTaskInstance);
 }

 @Test(expected = InterruptedException.class)
 public void testGetResultWithInterruptedException() throws InterruptedException, ExecutionException {
 // GIVEN
 when(targetTaskHandle.get()).thenThrow(new InterruptedException("InterruptedException"));

 // WHEN
 try {
 flowTaskInstance.getResult();
 } catch (InterruptedException e) {
 // THEN
 fail("Should throw InterruptedException when getting result from task");
 }
 }

 @Test(expected = ExecutionException.class)
 public void testGetResultWithExecutionException() throws InterruptedException, ExecutionException {
 // GIVEN
 when(targetTaskHandle.get()).thenThrow(new ExecutionException("ExecutionException"));

 // WHEN
 try {
 flowTaskInstance.getResult();
 } catch (ExecutionException e) {
 // THEN
 fail("Should throw ExecutionException when getting result from task");
 }
 }

 @Test(expected = TimeoutException.class)
 public void testGetResultWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 // GIVEN
 when(targetTaskHandle.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("TimeoutException"));

 // WHEN
 try {
 flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS);
 } catch (TimeoutException e) {
 // THEN
 fail("Should throw TimeoutException when getting result from task with timeout");
 }
 }

 @Test
 public void testGetTargetDelegateClass() {
 // GIVEN

 // WHEN
 Class<BaseRuntimeFlowableDelegate<String>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();

 // THEN
 assertEquals("Target delegate class is correct", BaseRuntimeFlowableDelegate.class, targetDelegateClass);
 }
}