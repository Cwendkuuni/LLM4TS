package com.oceanbase.odc.service.flow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;

public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepositoryMock;

    @InjectMocks
    private FlowableAdaptorImpl testFlowableAdaptorImpl;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testgetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceRepositoryMock.findById(any())).thenReturn(Optional.empty());

        Optional<Long> result = testFlowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("instanceId");

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testgetFlowableElementByType() {
        when(nodeInstanceRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(), any(), any())).thenReturn(Collections.emptyList());

        List<FlowableElement> result = testFlowableAdaptorImpl.getFlowableElementByType(1L, instanceTypeMock, flowableElementTypeMock);

        assertEquals(Collections.emptyList(), result);
    }

    // TODO: Continue with similar tests for each of the other available methods 
}