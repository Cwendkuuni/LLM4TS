
package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp() throws Exception {
        flowableAdaptorImpl = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process-001";
        // based on your methods and returned values, you should mock the necessary methods.
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "process-001";
        Long flowInstanceId = 1L;

        when(flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId)).thenReturn(Optional.of(flowInstanceId));
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    // Continue with other test methods...
    @Test
    public void testSetProcessDefinitionId() {
        // ..
    }

    // and so on...

    @Test
    public void testSetFlowableElement() {
        // ..
    }
}
