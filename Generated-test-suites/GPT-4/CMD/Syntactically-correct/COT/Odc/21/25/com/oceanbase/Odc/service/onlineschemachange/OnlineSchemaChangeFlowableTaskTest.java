package com.oceanbase.odc.service.onlineschemachange;
import static org.junit.Assert.*;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeFlowableTaskTest{
    private OnlineSchemaChangeFlowableTask task;
    private TaskService taskServiceMock;
    private DelegateExecution delegateExecutionMock;

    @Before
    public void setup() {
        task = new OnlineSchemaChangeFlowableTask();
        taskServiceMock= Mockito.mock(TaskService.class);
        delegateExecutionMock = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testStart() throws Exception {
        Mockito.when(delegateExecutionMock.getCurrentActivityId()).thenReturn("1");
        task.start(1L, taskServiceMock, delegateExecutionMock);
    }

    @Test
    public void testIsSuccessful() {
        assertFalse(task.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        assertFalse(task.isFailure());
    }

    @Test
    public void testOnFailure() {
        task.onFailure(1L, taskServiceMock);
    }

    @Test
    public void testOnSuccessful(){
        task.onSuccessful(1L, taskServiceMock);
    }

    @Test
    public void testOnTimeout(){
        task.onTimeout(1L, taskServiceMock);
    }

    @Test
    public void testOnProgressUpdate(){
        task.onProgressUpdate(1L, taskServiceMock);
    }
    
    @Test
    public void testCancel(){
        task.cancel(true, 1L, taskServiceMock);
    }

    @Test
    public void testIsCancelled(){
        assertFalse(task.isCancelled());
    }
}