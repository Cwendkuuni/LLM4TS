package com.oceanbase.odc.service.flow;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.eq;

public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Test
    public void setProcessInstanceIdTest_present() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testId";

        // provided flow instance
        FlowInstanceEntity provided = new FlowInstanceEntity();
        provided.setId(flowInstanceId);

        // we must mock repository call
        Mockito.when(flowInstanceRepository.findById(eq(flowInstanceId)))
            .thenReturn(Optional.of(provided));

        // we expect 1 update to happen
        Mockito.when(flowInstanceRepository.updateProcessInstanceIdById(eq(flowInstanceId), eq(processInstanceId)))
            .thenReturn(1);

        // run the test case
        flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

        // verify the results were as expected
        Mockito.verify(flowInstanceRepository, Mockito.times(1))
            .updateProcessInstanceIdById(eq(flowInstanceId), eq(processInstanceId));
    }
}
