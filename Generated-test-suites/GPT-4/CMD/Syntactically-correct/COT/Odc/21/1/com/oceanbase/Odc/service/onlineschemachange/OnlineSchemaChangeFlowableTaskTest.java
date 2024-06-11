package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OnlineSchemaChangeFlowableTaskTest {

    @Mock
    private DelegateExecution mockExecution;

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onSchemaChangeTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsSuccessful() {
        onSchemaChangeTask.status = TaskStatus.DONE;
        assertTrue(onSchemaChangeTask.isSuccessful());

        onSchemaChangeTask.status = TaskStatus.CANCELED;
        assertFalse(onSchemaChangeTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onSchemaChangeTask.status = TaskStatus.FAILED;
        assertTrue(onSchemaChangeTask.isFailure());

        onSchemaChangeTask.status = TaskStatus.RUNNING;
        assertFalse(onSchemaChangeTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        // TODO: implement
    }

    @Test
    public void testOnSuccessful() {
        // TODO: implement
    }

    @Test
    public void testOnTimeout() {
        // TODO: implement
    }

    @Test
    public void testOnProgressUpdate() {
        // TODO: implement
    }

    @Test
    public void testCancel() {
        // TODO: implement
    }

    @Test
    public void testIsCancelled() {
        onSchemaChangeTask.status = TaskStatus.CANCELED;
        assertTrue(onSchemaChangeTask.isCancelled());

        onSchemaChangeTask.status = TaskStatus.RUNNING;
        assertFalse(onSchemaChangeTask.isCancelled());
    }

    // Below are testing for private methods. Perceptionally, we dont test private 
    // methods because it's supposed to be tested, directly or indirectly, by other 
    // public methods that are invoking it. If we still want to tests them, we need 
    // to make some trade-off like changing their access modifiers.

    //...

}
