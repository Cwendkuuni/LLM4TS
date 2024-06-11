package com.oceanbase.odc.service.flow.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private RetryExecutor retryExecutor;
    @Mock
    private Execution execution;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Integer> delegate;

    @BeforeEach
    public void setupMocks() {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getFlowInstanceById(anyLong())).thenReturn(new Execution() {
            @Override
            public String getProcessInstanceId() {
                return "processInstanceId";
            }

            @Override
            public String getExecutionId() {
                return "executionId";
            }

            @Override
            public String getProcessDefinitionId() {
                return "processDefinitionId";
            }

            @Override
            public String getActivityId() {
                return "activityId";
            }

            @Override
            public String getBusinessKey() {
                return "businessKey";
            }

            @Override
            public Map<String, Object> getVariables() {
                return new HashMap<>();
            }

            @Override
            public void setVariables(Map<String, Object> variables) {

            }

            @Override
            public void signalAll() {

            }

            @Override
            public void signalEventByName(String eventName) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void setVariableLocal(String variableName, Object value) {

            }

            @Override
            public void setVariable(String variableName, Object value) {

            }

            @Override
            public Object getVariable(String variableName) {
                return null;
            }

            @Override
            public void deleteVariable(String variableName) {

            }

            @Override
            public void addListener(String eventName, Object listener) {

            }

            @Override
            public void removeListener(String eventName, Object listener) {

            }

            @Override
            public void execute(DelegateExecution execution) {

            }

            @Override
            public <T> Future<T> call(Callable<T> callable) throws Exception {
                return null;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public T get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }

            @Override
            public <T> T getNow() {
                return null;
            }
        });
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(new FlowTaskInstance() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public FlowableTask getTask() {
                return null;
            }

            @Override
            public String getTaskDefinitionKey() {
                return "taskDefinitionKey";
            }

            @Override
            public String getTaskDefinitionId() {
                return "taskDefinitionId";
            }

            @Override
            public String getName() {
                return "taskName";
            }

            @Override
            public String getAssignee() {
                return "assignee";
            }

            @Override
            public String getParentTaskId() {
                return null;
            }

            @Override
            public String getPriority() {
                return null;
            }

            @Override
            public String getResourceName() {
                return null;
            }

            @Override
            public String getType() {
                return TaskType.TASK.name();
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Long getProcessInstanceId() {
                return 1L;
            }

            @Override
            public Long getExecutionId() {
                return 1L;
            }

            @Override
            public Long getBusinessKey() {
                return null;
            }

            @Override
            public void dealloc() {

            }

            @Override
            public void bindServiceTask(BaseFlowableDelegate<?> serviceTask) {

            }

            @Override
            public void updateStatus(FlowStatus status) {

            }

            @Override
            public void setStatus(FlowStatus status) {

            }

            @Override
            public void setStrategyConfig(ExecutionStrategyConfig strategyConfig) {

            }

            @Override
            public ExecutionStrategyConfig getStrategyConfig() {
                return null;
            }
        });
        when(flowableAdaptor.getTaskInstanceById(anyLong())).thenReturn(new FlowTaskInstance() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public FlowableTask getTask() {
                return null;
            }

            @Override
            public String getTaskDefinitionKey() {
                return "taskDefinitionKey";
            }

            @Override
            public String getTaskDefinitionId() {
                return "taskDefinitionId";
            }

            @Override
            public String getName() {
                return "taskName";
            }

            @Override
            public String getAssignee() {
                return "assignee";
            }

            @Override
            public String getParentTaskId() {
                return null;
            }

            @Override
            public String getPriority() {
                return null;
            }

            @Override
            public String getResourceName() {
                return null;
            }

            @Override
            public String getType() {
                return TaskType.TASK.name();
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Long getProcessInstanceId() {
                return 1L;
            }

            @Override
            public Long getExecutionId() {
                return 1L;
            }

            @Override
            public Long getBusinessKey() {
                return null;
            }

            @Override
            public void dealloc() {

            }

            @Override
            public void bindServiceTask(BaseFlowableDelegate<?> serviceTask) {

            }

            @Override
            public void updateStatus(FlowStatus status) {

            }

            @Override
            public void setStatus(FlowStatus status) {

            }

            @Override
            public void setStrategyConfig(ExecutionStrategyConfig strategyConfig) {

            }

            @Override
            public ExecutionStrategyConfig getStrategyConfig() {
                return null;
            }
        });
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(retryExecutor.run(any(Callable.class))).thenReturn(true);
    }

    @Test
    public void testInitCallable() throws Exception {
        Callable<Integer> callable = delegate.initCallable(execution);
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId());
        verify(flowableAdaptor).getFlowInstanceById(1L);
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);
    }

    @Test
    public void testRun() throws Exception {
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);

        delegate.run(execution);

        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(delegate, any(Thread.class)));
        verify(flowInstanceRepository).updateStatusById(1L, eq(FlowStatus.STARTED));
        verify(execution).setVariable("returnObject", any());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
        delegate.run(execution);

        delegate.get();

        verify(execution, times(2)).isDone();
        verify(execution).isDone();
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.FAILED);
        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.FAILED));
    }

    @Test
    public void testPreHandle() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.preHandle(execution);
        verifyNoInteractions(eventPublisher, flowInstanceRepository, flowableAdaptor, retryExecutor);
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(eq(delegate));
    }
}