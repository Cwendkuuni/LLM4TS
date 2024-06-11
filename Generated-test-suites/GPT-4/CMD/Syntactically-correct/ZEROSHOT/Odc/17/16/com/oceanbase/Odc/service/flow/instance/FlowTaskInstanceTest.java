package com.oceanbase.odc.service.flow.instance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FlowTaskInstanceTest {
  
  @InjectMocks
  FlowTaskInstance flowTaskInstance;

  @Mock
  ServiceTaskInstanceEntity entity;
  
  @Mock
  RuntimeDelegateMapper convertor;

  @Mock
  FlowableAdaptor flowableAdaptor;

  @Mock
  EventPublisher eventPublisher;

  @Mock
  TaskService taskService;

  @Mock
  NodeInstanceEntityRepository nodeRepository;

  @Mock
  SequenceInstanceRepository sequenceRepository;

  @Mock
  ServiceTaskInstanceRepository serviceTaskRepository;

  @Mock
  TaskType taskType;

  @Mock
  ExecutionStrategyConfig strategyConfig;

  @Mock
  ServiceTaskInstanceEntity serviceTaskInstanceEntity;

  @Mock
  BaseRuntimeFlowableDelegate<?> runtimeFlowableDelegate;

  @Before
  public void init() {
      MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreate() {
      when(serviceTaskRepository.save(serviceTaskInstanceEntity)).thenReturn(serviceTaskInstanceEntity);
      flowTaskInstance.create();
      verify(serviceTaskRepository, times(1)).save(any());
  }

  @Test
  public void testUpdate() {
      when(serviceTaskRepository.update(serviceTaskInstanceEntity)).thenReturn(1);
      flowTaskInstance.update();
      verify(serviceTaskRepository, times(1)).update(any());
  }

  @Test
  public void testDelete() {
      doNothing().when(serviceTaskRepository).deleteById(anyLong());
      flowTaskInstance.doDelete(1L);
      verify(serviceTaskRepository, times(1)).deleteById(anyLong());
  }

  @Test
  public void testIsPresentOnThisMachine() {
      Assert.assertEquals(false, flowTaskInstance.isPresentOnThisMachine());
  }

  @Test
  public void testIsDone() {
      when(runtimeFlowableDelegate.isDone()).thenReturn(true);
      Assert.assertEquals(true, flowTaskInstance.isDone());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetResultException() throws ExecutionException, InterruptedException {
      flowTaskInstance.getResult();
  }

  @Test
  public void testGetResultSuccess() throws ExecutionException, InterruptedException {
      when(runtimeFlowableDelegate.get()).thenReturn(null);
      Assert.assertNull(flowTaskInstance.getResult());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetResultWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
      flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS);
  }

  @Test
  public void testGetResultWithTimeoutSuccess() throws InterruptedException, ExecutionException, TimeoutException {
      when(runtimeFlowableDelegate.get(anyLong(), any())).thenReturn(null);
      Assert.assertNull(flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS));
  }

  @After
  public void demolish() {
      flowTaskInstance = null;
      entity = null;
      convertor = null;
      flowableAdaptor = null;
      eventPublisher = null;
      taskService = null;
      nodeRepository = null;
      sequenceRepository = null;
      serviceTaskRepository = null;
      taskType = null;
      strategyConfig = null;
      serviceTaskInstanceEntity = null;
      runtimeFlowableDelegate = null;
  }
}
