import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    // .. mock all other dependencies ..

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        // define behavior for your mocked interfaces, execute methods and verify results
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceRepository.findAll(any())).thenReturn(Collections.singletonList(getFlowInstanceEntity()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");

        verify(flowInstanceRepository, times(1)).findAll(any());

        assertEquals("Failed to get the expected result", Optional.of(1L), result);
    }

    // Add more test methods for all other methods in your main class

    private FlowInstanceEntity getFlowInstanceEntity() {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        return flowInstanceEntity;
    }
}