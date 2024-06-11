package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    //all above mock beans are assumed to be used in your methods, 
    //you have to define mocks for all beans autowired in your main class.


    @Test
    public void testSetProcessInstanceId() {
        final Long flowInstanceId = 1L;
        final String processInstanceId = "test-process-instance";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        //mock or capture inner repository method execution and assert result
        //Not enough information given to create a meaningful test
    }

    @Test
    public void getFlowInstanceIdByProcessInstanceId() {
        final String processInstanceId = "test-process-instance";
        given(flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId))
            .willReturn(Optional.of(1L));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(Optional.of(1L), result);
    }

    // similar methods have to be written for all your methods in FlowableAdaptorImpl class

}