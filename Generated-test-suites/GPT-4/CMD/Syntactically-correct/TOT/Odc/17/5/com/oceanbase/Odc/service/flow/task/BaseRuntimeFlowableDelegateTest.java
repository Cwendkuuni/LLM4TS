package com.oceanbase.odc.service.flow.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegateTestSubclass baseRuntimeFlowableDelegate;
    
    @Mock
    EventPublisher eventPublisher;
    
    @Mock
    ActiveTaskStatisticsListener activeTaskStatisticsListener;
    
    @Mock
    TaskInstanceCreatedListener taskInstanceCreatedlistener;
    
    @Test
    public void testInit() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        verify(eventPublisher, times(1)).addEventListener(activeTaskStatisticsListener);
        verify(eventPublisher, times(1)).addEventListener(taskInstanceCreatedlistener);
    }

    private class BaseRuntimeFlowableDelegateTestSubclass extends BaseRuntimeFlowableDelegate<FlowTaskInstance> {

        @Override
        protected Callable<FlowTaskInstance> initCallable(DelegateExecution execution) {
            return null;
        }
    }
}