
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;

public class FlowTaskInstanceTest {

    @Mock
    TaskService taskServiceMock;
    @Mock
    EventPublisher eventPublisherMock;
    @Mock
    NodeInstanceEntityRepository nodeRepositoryMock;
    @Mock
    SequenceInstanceRepository sequenceRepositoryMock;
    @Mock
    ServiceTaskInstanceRepository serviceTaskRepositoryMock;
    @Mock
    RuntimeDelegateMapper runtimeDelegateMapperMock;
    @Mock
    FlowableAdaptor flowableAdaptorMock;
    @Mock
    ServiceTaskInstanceEntity serviceTaskInstanceEntityMock;

    private FlowTaskInstance testInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ExecutionStrategyConfig esc = new ExecutionStrategyConfig();
        esc.setPendingExpireIntervalSeconds(10);
        esc.setExecutionTime(1);
        esc.setStrategy(FlowTaskExecutionStrategy.MANUAL);

        when(serviceTaskInstanceEntityMock.getId()).thenReturn(1L);
        when(serviceTaskRepositoryMock.save(any())).thenReturn(serviceTaskInstanceEntityMock);

        testInstance = new FlowTaskInstance(TaskType.APPROVAL, 1L, 1L, esc, true, true, runtimeDelegateMapperMock,
            flowableAdaptorMock, eventPublisherMock, taskServiceMock, nodeRepositoryMock,
            sequenceRepositoryMock, serviceTaskRepositoryMock);
    }

    @Test
    public void createTest() {
        testInstance.create();
        assert(testInstance.getId().equals(serviceTaskInstanceEntityMock.getId()));
        verify(serviceTaskRepositoryMock, times(1)).save(any());
    }
}
