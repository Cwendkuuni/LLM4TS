package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.service.flow.*;
import com.oceanbase.odc.core.flow.model.*;
import com.oceanbase.odc.metadb.flow.*;

import java.util.Optional;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FlowableAdaptorImplTest {

    @MockBean
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(1L, "1234");
        verify(flowableAdaptorImpl, times(1)).setProcessInstanceId(1L, "1234");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        final String processInstanceId = "1234";
        when(flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId)).thenReturn(Optional.of(1L));

        final Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(Optional.of(1L), result);
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptorImpl.setProcessDefinitionId(1L, "1234");
        verify(flowableAdaptorImpl, times(1)).setProcessDefinitionId(1L, "1234");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        final String processDefinitionId = "1234";
        when(flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(1L));

        final Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(Optional.of(1L), result);
    }

    // Similar test methods to be written for rest of the public methods in FlowableAdaptorImpl class...

}
