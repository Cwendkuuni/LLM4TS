package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution mockExecution;
    
    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "Test Result";
                };
            }
        };
    }

    @Test
    public void testRun() {
        try {
            delegate.run(mockExecution);
            assertTrue(delegate.isDone());
            assertEquals("Test Result", delegate.get());
        } catch (Exception e) {
            fail("Exception thrown during run: " + e.getMessage());
        }
    }

    @Test
    public void testGet() {
        try {
            delegate.run(mockExecution);
            assertEquals("Test Result", delegate.get());
        } catch (Exception e) {
            fail("Exception thrown during get: " + e.getMessage());
        }
    }

    @Test
    public void testGetTimeout() {
        try {
            delegate.run(mockExecution);
            assertEquals("Test Result", delegate.get(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            fail("Exception thrown during get(timeout): " + e.getMessage());
        }
    }
}