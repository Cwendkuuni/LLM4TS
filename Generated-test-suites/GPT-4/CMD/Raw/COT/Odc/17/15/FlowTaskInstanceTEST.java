import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {
  private FlowTaskInstance instance;
  private ServiceTaskInstanceRepository mockRepository;

  @Before
  public void setup() {
      mockRepository = mock(ServiceTaskInstanceRepository.class);
      instance = new FlowTaskInstance(mock(TaskType.class), 1L, 1L, mock(ExecutionStrategyConfig.class), true, true, mock(RuntimeDelegateMapper.class), mock(FlowableAdaptor.class), mock(EventPublisher.class), mock(TaskService.class), mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), mockRepository);
  }

  @Test
  public void testCreate() {
      instance.create();
      verify(mockRepository, times(1)).save(any());
  }

  @Test
  public void testUpdate() {
      instance.setId(1L);
      instance.update();
      verify(mockRepository, times(1)).update(any());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIsPresentOnThisMachine() {
      assertFalse(instance.isPresentOnThisMachine());
  }

  // Other test cases go here...
}