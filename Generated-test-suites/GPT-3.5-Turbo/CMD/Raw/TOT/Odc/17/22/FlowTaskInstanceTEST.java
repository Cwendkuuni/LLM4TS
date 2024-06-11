import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {
  
  @Mock
  private ServiceTaskInstanceEntity mockEntity;
  @Mock
  private RuntimeDelegateMapper mockMapper;
  @Mock
  private FlowableAdaptor mockAdaptor;
  @Mock
  private EventPublisher mockPublisher;
  @Mock
  private TaskService mockTaskService;
  @Mock
  private NodeInstanceEntityRepository mockNodeRepository;
  @Mock
  private SequenceInstanceRepository mockSequenceRepository;
  @Mock
  private ServiceTaskInstanceRepository mockServiceTaskRepository;

  private FlowTaskInstance flowTaskInstance;

  @Before
  public void setUp() {
    when(mockEntity.getId()).thenReturn(1L);
    when(mockEntity.getCreateTime()).thenReturn(System.currentTimeMillis());
    when(mockEntity.getUpdateTime()).thenReturn(System.currentTimeMillis());
    
    flowTaskInstance = new FlowTaskInstance(mockEntity, mockMapper, mockAdaptor, mockPublisher, mockTaskService,
        mockNodeRepository, mockSequenceRepository, mockServiceTaskRepository);
  }

  @Test
  public void testCreate() {
    assertNotNull(flowTaskInstance);
    flowTaskInstance.create();
    // Verify that service task instance is created successfully
    assertNotNull(flowTaskInstance.getId());
  }

  @Test
  public void testUpdate() {
    assertNotNull(flowTaskInstance);
    flowTaskInstance.update();
    // Verify that service task instance is updated successfully
    assertNotNull(flowTaskInstance.getId());
  }

  @Test
  public void testCancel() {
    assertNotNull(flowTaskInstance);
    // Test for unsupported operation exception when task is not on this machine
    assertThrows(UnsupportedOperationException.class, () -> {
      flowTaskInstance.cancel(false);
    });
  }

  @Test
  public void testIsDone() {
    assertNotNull(flowTaskInstance);
    // Test for unsupported operation exception when task is not on this machine
    assertThrows(UnsupportedOperationException.class, () -> {
      flowTaskInstance.isDone();
    });
  }

  @Test
  public void testGetResult() {
    assertNotNull(flowTaskInstance);
    // Test for unsupported operation exception when task is not on this machine
    assertThrows(UnsupportedOperationException.class, () -> {
      flowTaskInstance.getResult();
    });
  }

  @Test
  public void testGetResultWithTimeout() {
    assertNotNull(flowTaskInstance);
    // Test for unsupported operation exception when task is not on this machine
    assertThrows(UnsupportedOperationException.class, () -> {
      flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    });
  }

}