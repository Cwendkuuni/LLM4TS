import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class FlowTaskInstanceTest {
    private FlowTaskInstance testInstance;
    private ServiceTaskInstanceEntityRepository mockRepo;
    private ExecutionStrategyConfig strategyConfig;
    private TaskService mockTaskService;
    private EventPublisher mockEventPublisher;
    private RuntimeDelegateMapper mockConvertor;

    @Before
    public void setup() {
        mockRepo = Mockito.mock(ServiceTaskInstanceEntityRepository.class);
        strategyConfig = ExecutionStrategyConfig.from(new ServiceTaskInstanceEntity());
        mockTaskService = Mockito.mock(TaskService.class);
        mockEventPublisher = Mockito.mock(EventPublisher.class);
        mockConvertor = Mockito.mock(RuntimeDelegateMapper.class);

        testInstance = new FlowTaskInstance(TaskType.ANY, 1L, 1L, strategyConfig, true, false, mockConvertor, new FlowableAdaptor(), mockEventPublisher, mockTaskService, new NodeInstanceEntityRepository(), new SequenceInstanceRepository(), mockRepo);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(testInstance.isPresentOnThisMachine());
    }

    @Test
    public void testBatchCreate() {
        ArrayList<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(testInstance);
        
        assertEquals(FlowTaskInstance.batchCreate(instances, mockRepo), instances);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(testInstance.getCoreFlowableElementType(), FlowableElementType.SERVICE_TASK);
    }
    
    // ... other tests follow
}