package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.mock;
import org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private DelegateExecution execution;

 @InjectMocks
 private BaseRuntimeFlowableDelegate<String> testSubject;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.execution = mock(DelegateExecution.class);
 this.delegate = new BaseRuntimeFlowableDelegate<>();
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 // Given
 when(execution.getProcessDefinitionId()).thenReturn("someProcessDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("someActivityId");

 // When
 testSubject.initTargetTaskInstanceId(execution);

 // Then
 assertEquals("someProcessDefinitionId", testSubject.getFlowInstanceId());
 assertEquals("someActivityId", testSubject.getActivityId());
 }

 @Test(expected = ServiceTaskError.class)
 public void testInitTargetTaskInstanceId_fail() {
 // Given
 when(execution.getProcessDefinitionId()).thenThrow(new RuntimeException("Process definition not found"));

 // When
 testSubject.initTargetTaskInstanceId(execution);

 // Then
 assertNull(testSubject.getFlowInstanceId());
 throw new ServiceTaskError(new RuntimeException("Process definition not found"));
 }

 @Test
 public void testRun_success() throws Exception {
 // Given
 when(execution.getProcessDefinitionId()).thenReturn("someProcessDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("someActivityId");
 Callable<String> callable = mock(Callable.class);
 when(callable.call()).thenReturn("testData");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("someProcessDefinitionId")).thenReturn(1L);

 // When
 testSubject.run(execution);

 // Then
 assertEquals("testData", testSubject.get());
 }

 @Test(expected = ServiceTaskError.class)
 public void testRun_failure() throws Exception {
 // Given
 when(execution.getProcessDefinitionId()).thenReturn("someProcessDefinitionId");
 when(execution.getCurrentActivityId()).thenReturn("someActivityId");
 Callable<String> callable = mock(Callable.class);
 when(callable.call()).thenThrow(new RuntimeException("Task execution failed"));
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("someProcessDefinitionId")).thenReturn(1L);

 // When
 testSubject.run(execution);

 // Then
 assertNull(testSubject.get());
 throw new ServiceTaskError(new RuntimeException("Task execution failed"));
 }
}