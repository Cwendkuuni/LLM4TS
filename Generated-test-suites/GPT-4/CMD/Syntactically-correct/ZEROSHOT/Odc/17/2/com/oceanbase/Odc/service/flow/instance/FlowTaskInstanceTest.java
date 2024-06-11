package com.oceanbase.odc.service.flow.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowTaskInstance).complete(false);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.confirmExecute();
        verify(flowTaskInstance).complete(true);
    }

    @Test
    public void testIsDone() {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        when(targetTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance.isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        when(targetTaskHandle.get()).thenReturn(any());
        flowTaskInstance.getResult();
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        when(targetTaskHandle.get(any(Long.class), any(TimeUnit.class))).thenReturn(any());
        flowTaskInstance.getResult(5, TimeUnit.SECONDS);
    }
}
