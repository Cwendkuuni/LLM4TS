import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance subjectUnderTest;

    @BeforeEach
    public void setUp() {
        // Given 
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        RuntimeDelegateMapper convertor = mock(RuntimeDelegateMapper.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        // When  
        subjectUnderTest = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        // Call the method you want to test
        subjectUnderTest.create();
       
        // Use assertions to verify the behavior
        assertNotNull(subjectUnderTest);
    }

    // TODO: Add more tests for each method in the class, and also edge cases / exceptions
    
}