
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

  @Mock
  TaskService taskService;
  
  @Mock
  ServiceTaskInstanceRepository serviceTaskRepository;
  
  @Mock
  EventPublisher eventPublisher;

  @Mock
  RuntimeDelegateMapper delegateConvertor;

  @Mock
  NodeInstanceEntityRepository nodeRepository;

  @Mock
  SequenceInstanceRepository sequenceRepository;

  @Mock
  BaseRuntimeFlowableDelegate<Task> targetTaskHandle;

  
  private FlowTaskInstance instance;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
    instance = new FlowTaskInstance(TaskType.NORMAL, 123L, 123L, strategyConfig,
            false, false, delegateConvertor, mock(FlowableAdaptor.class), eventPublisher, taskService, 
            nodeRepository, sequenceRepository, serviceTaskRepository);

    instance.bindServiceTask(targetTaskHandle);
  }

  //Testing bindServiceTask method
  @Test
  public void testBindServiceTaskUpdatesStatus() {
    instance.bindServiceTask(targetTaskHandle);
    assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
  }

  // Checking edge case where task isn't on this machine
  @Test(expected = UnsupportedOperationException.class)
  public void testGetResultThrowsExceptionWhenTaskNotOnThisMachine() throws Exception {
    when(instance.isPresentOnThisMachine()).thenReturn(false);
    instance.getResult();
  }

}

