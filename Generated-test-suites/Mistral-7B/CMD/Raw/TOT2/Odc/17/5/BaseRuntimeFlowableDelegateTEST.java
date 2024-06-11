package com.oceanbase.odc.service.flow.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private DelegateExecution execution;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private FlowTaskInstance flowTaskInstance;
 @InjectMocks
 private BaseRuntimeFlowableDelegate<String> testDelegate;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 testDelegate.setEventPublisher(eventPublisher);
 testDelegate.setFlowableAdaptor(flowableAdaptor);
 testDelegate.setFlowInstanceRepository(flowInstanceRepository);
 }

 @Test
 public void testInit() {
 // Test initialization
 testDelegate.init(eventPublisher);
 }

 @Test
 public void testRun_Success() throws Exception {
 // Test successful execution
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(flowTaskInstance);
 doAnswer(new Answer<Callable<String>>() {
 @Override
 public Callable<String> answer(InvocationOnMock invocation) throws Throwable {
 return new Callable<String>() {
 @Override
 public String call() throws Exception {
 return "success";
 }
 };
 }
 }).when(testDelegate).initCallable(execution);
 testDelegate.run(execution);
 Assertions.assertNull(testDelegate.getThrown());
 Assertions.assertNotNull(testDelegate.getReturnObject());
 }

 @Test
 public void testRun_Exception() throws Exception {
 // Test exception
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException("Error"));
 testDelegate.run(execution);
 Assertions.assertNotNull(testDelegate.getThrown());
 Assertions.assertNull(testDelegate.getReturnObject());
 }

 @Test
 public void testPreHandle() {
 // Test preHandle
 testDelegate.preHandle(execution);
 }

 @Test
 public void testExecute() throws Exception {
 // Test execute
 testDelegate.execute(execution);
 }

 @Test
 public void testIsDone() throws Exception {
 // Test isDone
 testDelegate.run(execution);
 Thread.sleep(1000);
 testDelegate.isDone();
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 // Test get
 testDelegate.run(execution);
 testDelegate.get();
 }

 @Test
 public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Test getTimeout
 testDelegate.run(execution);
 testDelegate.get(1, TimeUnit.SECONDS);
 }

 @Test
 public void testInitTargetTaskInstanceId() throws Exception {
 // Test initTargetTaskInstanceId
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(flowTaskInstance);
 testDelegate.initTargetTaskInstanceId(execution);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // Test bindToFlowTaskInstance
 testDelegate.bindToFlowTaskInstance(flowTaskInstance);
 }

 @Test
 public void testUpdateFlowInstanceStatus() throws Exception {
 // Test updateFlowInstanceStatus
 doAnswer(new Answer<Boolean>() {
 @Override
 public Boolean answer(InvocationOnMock invocation) throws Throwable {
 return true;
 }
 }).when(flowInstanceRepository).updateStatusById(anyLong(), any(FlowStatus.class));
 testDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 }

 @Test
 @Disabled("This test requires a real execution environment to work")
 public void testServiceTaskExecution() throws Exception {
 // Test service task execution
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(flowTaskInstance);
 doAnswer(new Answer<Callable<String>>() {
 @Override
 public Callable<String> answer(InvocationOnMock invocation) throws Throwable {
 return new Callable<String>() {
 @Override
 public String call() throws Exception {
 throw new RuntimeException("Test error");
 }
 };
 }
 }).when(testDelegate).initCallable(execution);
 testDelegate.run(execution);
 Assertions.assertNotNull(testDelegate.getThrown());
 Assertions.assertNull(testDelegate.getReturnObject());
 }
}