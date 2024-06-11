package com.oceanbase.odc.service.flow.instance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowTaskInstanceTest {
    
    @Mock
    ServiceTaskInstanceEntity entityMock;

    @Mock
    RuntimeDelegateMapper runtimeDelegateMapperMock;

    @Mock
    FlowableAdaptor flowableAdaptorMock;

    @Mock
    EventPublisher eventPublisherMock;

    @Mock
    TaskService taskServiceMock;

    @Mock
    NodeInstanceEntityRepository nodeRepositoryMock;

    @Mock
    ServiceTaskInstanceRepository serviceTaskRepositoryMock;
    
    @InjectMocks
    FlowTaskInstance flowTaskInstance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdate() {
        // mock behaviors
        when(serviceTaskRepositoryMock.update(any())).thenReturn(1);

        // testing method call
        flowTaskInstance.update();

        // verify behaviors
        verify(serviceTaskRepositoryMock).update(any());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate runtimeDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(runtimeDelegate.getTargetTaskInstanceId()).thenReturn(1L);
        flowTaskInstance.bindServiceTask(runtimeDelegate);

        // assert the status is now EXECUTING
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        // this method call "complete()" function and make sure status can only by PENDING
        // so we set it PENDING before running the method.
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        assertDoesNotThrow(() -> flowTaskInstance.confirmExecute());
    }
    
    @Test
    public void testCompleteInvalidStatus() {
        // this method call "complete()" function and ensuring it throws UnsupportedOperationException
        // when FlowNodeStatus is not PENDING
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.confirmExecute());
    }

    @Test
    public void testIsDone() {
        // so that "isPresentOnThisMachine()" returns true
        flowTaskInstance.bindServiceTask(new RuntimeDelegateExample());

        assertDoesNotThrow(() -> flowTaskInstance.isDone());
    }

    @Test
    public void testIsDoneTaskNotPresent() {
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(new RuntimeDelegateExample());
        assertEquals("success", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.bindServiceTask(new RuntimeDelegateExample());
        assertDoesNotThrow(() -> assertEquals("success", flowTaskInstance.getResult(500, TimeUnit.MILLISECONDS)));
    }
}

class RuntimeDelegateExample implements BaseRuntimeFlowableDelegate {
    // overriding all abstract methods without any functionality
    // to make sure there are no exceptions
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) { return false; }

    @Override
    public boolean isDone() { return true; }

    @Override
    public String get() throws InterruptedException, ExecutionException { return "success"; }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return "success"; }
}