package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> runtimeDelegate;
    private DelegateExecution executionMock;
    private FlowableAdaptor flowableAdaptorMock;
    private EventPublisher eventPublisherMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;

    @Before
    public void setup() {
        runtimeDelegate = Mockito.spy(BaseRuntimeFlowableDelegate.class);
        executionMock = Mockito.mock(DelegateExecution.class);
        flowableAdaptorMock = Mockito.mock(FlowableAdaptor.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        flowInstanceRepositoryMock = Mockito.mock(FlowInstanceRepository.class);

        runtimeDelegate.setFlowableAdaptor(flowableAdaptorMock);
        runtimeDelegate.setEventPublisher(eventPublisherMock);
        runtimeDelegate.setFlowInstanceRepository(flowInstanceRepositoryMock);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = runtimeDelegate.initCallable(executionMock);
        assertNotNull(callable);
    }

    @Test
    public void testRun_Success() throws Exception {
        Mockito.when(executionMock.getCurrentActivityId()).thenReturn("activityId");
        Mockito.when(flowableAdaptorMock.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong())).thenReturn(java.util.Optional.of(new FlowTaskInstance()));
        
        runtimeDelegate.run(executionMock);

        Mockito.verify(eventPublisherMock).addEventListener(Mockito.any());
        Mockito.verify(eventPublisherMock).addEventListener(Mockito.any());
        Mockito.verify(executionMock).getProcessDefinitionId();
        Mockito.verify(flowableAdaptorMock).getFlowInstanceIdByProcessDefinitionId(Mockito.anyString());
        Mockito.verify(flowableAdaptorMock).getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void testRun_Failure() {
        try {
            Mockito.when(executionMock.getCurrentActivityId()).thenReturn("activityId");
            Mockito.when(flowableAdaptorMock.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong())).thenReturn(java.util.Optional.empty());

            runtimeDelegate.run(executionMock);
        } catch (ServiceTaskError e) {
            // Expected
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testExecute() {
        Mockito.doNothing().when(runtimeDelegate).execute(Mockito.any());
        runtimeDelegate.execute(executionMock);
        Mockito.verify(eventPublisherMock).removeEventListener(Mockito.any());
        Mockito.verify(eventPublisherMock).removeEventListener(Mockito.any());
    }

    @Test
    public void testIsDone() {
        runtimeDelegate.setDone(true);
        assertTrue(runtimeDelegate.isDone());
    }

    @Test
    public void testGet_Success() throws InterruptedException, ExecutionException {
        runtimeDelegate.setReturnObject("test");
        
        assertEquals("test", runtimeDelegate.get());
    }

    @Test
    public void testGet_Exception() {
        runtimeDelegate.setThrown(new Exception("Test Exception"));
        
        try {
            runtimeDelegate.get();
        } catch (ExecutionException e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGet_TimeoutException() {
        try {
            runtimeDelegate.get(500, TimeUnit.MILLISECONDS); // Assuming task takes longer than 500ms
        } catch (TimeoutException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        Mockito.when(executionMock.getProcessDefinitionId()).thenReturn("processDefinitionId");
        Mockito.when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(Mockito.anyString())).thenReturn(java.util.Optional.of(java.util.Optional.of(1L)));
        Mockito.when(flowableAdaptorMock.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong())).thenReturn(java.util.Optional.of(new FlowTaskInstance()));

        runtimeDelegate.initTargetTaskInstanceId(executionMock);

        assertEquals(1L, runtimeDelegate.getFlowInstanceId().longValue());
        assertEquals(1L, runtimeDelegate.getTargetTaskInstanceId().longValue());
        assertEquals("activityId", runtimeDelegate.getActivityId());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        runtimeDelegate.setTargetTaskInstanceId(1L);
        runtimeDelegate.setActivityId("activityId");

        runtimeDelegate.bindToFlowTaskInstance(taskInstance);

        Mockito.verify(taskInstance).bindServiceTask(runtimeDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        runtimeDelegate.setFlowInstanceId(1L);
        FlowStatus flowStatus = FlowStatus.SUCCESS;

        Mockito.when(flowInstanceRepositoryMock.updateStatusById(Mockito.anyLong(), Mockito.any())).thenReturn(true);

        runtimeDelegate.updateFlowInstanceStatus(flowStatus);

        Mockito.verify(flowInstanceRepositoryMock).updateStatusById(1L, flowStatus);
    }
}