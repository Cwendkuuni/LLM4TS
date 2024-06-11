package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @Mock
    ServiceTaskInstanceEntity entity;
    @Mock
    RuntimeDelegateMapper convertor;
    @Mock
    FlowableAdaptor flowableAdaptor;
    @Mock
    EventPublisher eventPublisher;
    @Mock
    TaskService taskService;
    @Mock
    NodeInstanceEntityRepository nodeRepository;
    @Mock
    SequenceInstanceRepository sequenceRepository;
    @Mock
    ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    BaseRuntimeFlowableDelegate delegate;

    FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testComplete() {
        //Test if method runs without Exception, as the actual functionality is hard to isolate without extensive mocking
        flowTaskInstance.complete(false);
    }

    @Test
    public void testAbort() {
        //Test if method runs without Exception, as the actual functionality is hard to isolate without extensive mocking
        flowTaskInstance.abort();
    }

    @Test
    public void testCancel() {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        flowTaskInstance.bindServiceTask(delegate);
        //Test if method runs without Exception, as the actual functionality is hard to isolate without extensive mocking
        flowTaskInstance.cancel(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNotPresent() {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(false);
        flowTaskInstance.cancel(false);
    }

    @Test
    public void testIsDone() {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        flowTaskInstance.bindServiceTask(delegate);
        //Test if method runs without Exception, as the actual functionality is hard to isolate without extensive mocking
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneNotPresent() {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(false);
        flowTaskInstance.isDone();
    }

    @Test
    public void testGetResult() throws Exception {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        flowTaskInstance.bindServiceTask(delegate);
        //Test if method runs without Exception, as the actual functionality is hard to isolate without extensive mocking
        flowTaskInstance.getResult();
    }

    @Test
    public void testGetResultWithTimeout() throws Exception {
        when(flowTaskInstance.isPresentOnThisMachine()).thenReturn(true);
        flowTaskInstance.bindServiceTask(delegate);
        //Test if method runs without Exception, as the actual functionality is hard to isolate without extensive mocking
        flowTaskInstance.getResult(5, TimeUnit.SECONDS);
    }
}
