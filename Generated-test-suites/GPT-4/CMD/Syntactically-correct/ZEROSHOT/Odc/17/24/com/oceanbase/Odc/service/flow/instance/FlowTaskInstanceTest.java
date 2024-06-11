package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.engine.impl.TaskServiceImpl;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;


public class FlowTaskInstanceTest {
   
   private FlowTaskInstance target;

   // Mocks
   private ServiceTaskInstanceEntityRepository serviceTaskInstanceRepositoryMock;
   private TaskService taskServiceMock;
   private EventPublisher eventPublisherMock;
   private RuntimeDelegateMapper runtimeDelegateMapperMock;
   private BaseRuntimeFlowableDelegate flowableDelegateMock;

   // Constants for tests
   private static final Long ORGANIZATION_ID = 1L;
   private static final Long FLOW_INSTANCE_ID = 2L;
   private static final TaskType TASK_TYPE = TaskType.AGENT;
   private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.HUMAN, null, null);
   private static final Boolean START_ENDPOINT = false;
   private static final Boolean END_ENDPOINT = false;
   
   // Setup
   @Before
   public void setUp() {
       serviceTaskInstanceRepositoryMock = mock(ServiceTaskInstanceRepository.class);
       taskServiceMock = mock(TaskService.class);
       eventPublisherMock = mock(EventPublisher.class);
       runtimeDelegateMapperMock = mock(RuntimeDelegateMapper.class);
       flowableDelegateMock = mock(BaseRuntimeFlowableDelegate.class);
       
       target = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, START_ENDPOINT, END_ENDPOINT, runtimeDelegateMapperMock, flowableAdaptorMock, eventPublisherMock, taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock, serviceTaskInstanceRepositoryMock);
   }

   @Test
   public void testGetCoreFlowableElementType() {
      assertEquals(FlowableElementType.SERVICE_TASK, target.getCoreFlowableElementType());
   }

   @Test
   public void testAllocDealloc() {
      target.alloc();
      verify(eventPublisherMock).addEventListener(any(ServiceTaskStartedListener.class));

      target.dealloc();
      verify(eventPublisherMock).removeEventListener(any(ServiceTaskStartedListener.class));
   }

   @Test
   public void testIsPresentOnThisMachine() {
      assertFalse(target.isPresentOnThisMachine());
      
      target.bindServiceTask(flowableDelegateMock);
      
      assertTrue(target.isPresentOnThisMachine());
   }
   
   // Testing other methods would require setting up quite involved scenarios.
   // Hence, we focus on testing the methods which can be done with relatively simple mocking.
}
