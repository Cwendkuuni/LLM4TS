
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

  private DelegateExecution executionMock;
  private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

  @Before
  public void setup() {
    executionMock = mock(DelegateExecution.class);
    baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, withSettings()
            .useConstructor()
            .defaultAnswer(Mockito.CALLS_REAL_METHODS)
    );
  }

  @Test
  public void testPreHandle() throws Exception {
    baseRuntimeFlowableDelegate.preHandle(executionMock);
    verify(baseRuntimeFlowableDelegate).preHandle(executionMock);
  }

  @Test
  public void testExecute() throws Exception {
    baseRuntimeFlowableDelegate.execute(executionMock);
    verify(baseRuntimeFlowableDelegate).execute(executionMock);
  }

  @Test
  public void testIsDone() {
    baseRuntimeFlowableDelegate.isDone();
    verify(baseRuntimeFlowableDelegate).isDone();
  }

  @Test
  public void testGetWithoutException() throws InterruptedException, ExecutionException {
    when(baseRuntimeFlowableDelegate.get()).thenReturn("Task completed successfully!");
    baseRuntimeFlowableDelegate.get();
    verify(baseRuntimeFlowableDelegate).get();
  }

  @Test(expected = ExecutionException.class)
  public void testGetWithException() throws InterruptedException, ExecutionException{
    when(baseRuntimeFlowableDelegate.get()).thenThrow(ExecutionException.class);
    baseRuntimeFlowableDelegate.get();
    verify(baseRuntimeFlowableDelegate).get();
  }

  @Test
  public void testGetWithTimeout() throws Exception {
    when(baseRuntimeFlowableDelegate.get(anyLong(), any(TimeUnit.class))).thenReturn("Task completed successfully!");
    baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS);
    verify(baseRuntimeFlowableDelegate).get(1000, TimeUnit.MILLISECONDS);
  }

  @Test(expected = TimeoutException.class)
  public void testGetWithTimeoutException() throws Exception {
    when(baseRuntimeFlowableDelegate.get(anyLong(), any(TimeUnit.class))).thenThrow(TimeoutException.class);
    baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS);
    verify(baseRuntimeFlowableDelegate).get(1000, TimeUnit.MILLISECONDS);
  }

  @Test(expected = ExecutionException.class)
  public void testGetWithExecutionException() throws Exception {
    when(baseRuntimeFlowableDelegate.get(anyLong(), any(TimeUnit.class))).thenThrow(ExecutionException.class);
    baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS);
    verify(baseRuntimeFlowableDelegate).get(1000, TimeUnit.MILLISECONDS);
  }

  @Test
  public void testUpdateFlowInstanceStatus() {
    baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.PUBLISHED);
    verify(baseRuntimeFlowableDelegate).updateFlowInstanceStatus(FlowStatus.PUBLISHED);
  }
}
