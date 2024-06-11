
import com.oceanbase.odc.service.flow.instance.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Test
    public void testUpdate() {
        // Arrange
        Long mockId = 1L;
        FlowNodeStatus mockStatus = FlowNodeStatus.EXECUTING;
        ServiceTaskInstanceRepository mockRepo = mock(ServiceTaskInstanceRepository.class);
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(mockId);
        entity.setStatus(mockStatus);
        
        FlowTaskInstance instance = mock(FlowTaskInstance.class);
        when(instance.getId()).thenReturn(mockId);
        when(instance.getStatus()).thenReturn(mockStatus);
        
        // Act
        instance.update();
        
        // Assert
        verify(mockRepo).update(entity);
        verifyNoMoreInteractions(mockRepo);
    }
}
