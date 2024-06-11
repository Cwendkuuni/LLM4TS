import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test case for empty result
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processId");
        assertFalse(result.isPresent());

        // Test case for single result
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(entity));
        result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processId");
        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    @Test
    public void testSetFlowableElement() {
        // Test case for existing element
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        nodeInstance.setId(1L);
        nodeInstance.setNodeType("TEST");
        nodeInstance.setFlowInstanceId(1L);

        when(flowInstanceRepository.save(any())).thenReturn(new FlowInstanceEntity());
        flowableAdaptor.setFlowableElement(nodeInstance, new FlowableElement("123", "Test Element", FlowableElementType.TYPE));

        verify(flowInstanceRepository, times(0)).save(any());
    }

    // Add more test cases for other methods as needed

}