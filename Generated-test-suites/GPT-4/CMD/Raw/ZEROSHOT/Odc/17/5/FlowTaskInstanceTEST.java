
package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import org.flowable.task.api.Task;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class FlowTaskInstanceTest {
    FlowTaskInstance instanceUnderTest;

    TaskService mockTaskService;
    EventPublisher mockEventPublisher;
    RuntimeDelegateMapper mockDelegateConvertor;
    FlowableAdaptor mockFlowableAdaptor;
    ServiceTaskInstanceRepository mockServiceTaskRepository;
    NodeInstanceEntityRepository mockNodeRepository;
    SequenceInstanceRepository mockSequenceRepository;
    BaseRuntimeFlowableDelegate mockTargetTaskHandle;

    @Before
    public void setup() {
      mockTaskService = mock(TaskService.class);
      mockEventPublisher = mock(EventPublisher.class);
      mockDelegateConvertor = mock(RuntimeDelegateMapper.class);
      mockFlowableAdaptor = mock(FlowableAdaptor.class);
      mockServiceTaskRepository = mock(ServiceTaskInstanceRepository.class);
      mockNodeRepository = mock(NodeInstanceEntityRepository.class);
      mockSequenceRepository = mock(SequenceInstanceRepository.class);
      mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
      instanceUnderTest = new FlowTaskInstance(
        TaskType.MANUAL,
        1234L,
        5678L,
        new ExecutionStrategyConfig(),
        false,
        false,
        mockDelegateConvertor,
        mockFlowableAdaptor,
        mockEventPublisher,
        mockTaskService,
        mockNodeRepository,
        mockSequenceRepository,
        mockServiceTaskRepository
      );
    }

    @Test 
    public void testCreate() {
        instanceUnderTest.create();
        verify(mockServiceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        Long id = 456L;
        FlowNodeStatus status = FlowNodeStatus.CANCELLED;
        Long targetTaskId = 189L;
        TaskType taskType = TaskType.AUTOMATED;
        instanceUnderTest.setId(id);
        instanceUnderTest.setStatus(status);
        instanceUnderTest.setTargetTaskId(targetTaskId);
        instanceUnderTest.taskType = taskType;
        instanceUnderTest.update();
        verify(mockServiceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testBindServiceTask() {
        when(mockTargetTaskHandle.getTargetTaskInstanceId()).thenReturn(instanceUnderTest.getId());
        instanceUnderTest.bindServiceTask(mockTargetTaskHandle);
        assertSame(instanceUnderTest.getStatus(), FlowNodeStatus.EXECUTING);
        verify(mockTargetTaskHandle, times(1)).getTargetTaskInstanceId();
    }

    @Test
    public void testIsPresentOnThisMachine() {
        instanceUnderTest.targetTaskHandle = null;
        assertFalse(instanceUnderTest.isPresentOnThisMachine());
        instanceUnderTest.targetTaskHandle = mockTargetTaskHandle;
        assertTrue(instanceUnderTest.isPresentOnThisMachine());
    }

    @Test
    public void testAlloc() {
        instanceUnderTest.alloc();
        verify(mockEventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testDealloc() {
        instanceUnderTest.listener = new ServiceTaskStartedListener(instanceUnderTest);
        instanceUnderTest.dealloc();
        verify(mockEventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testCompleteWithStrategyAuto() {
        instanceUnderTest.strategyConfig.setStrategy(FlowTaskExecutionStrategy.AUTO);
        try {
            instanceUnderTest.confirmExecute();
            fail("Expected exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e.getMessage());
        }
    }
}
