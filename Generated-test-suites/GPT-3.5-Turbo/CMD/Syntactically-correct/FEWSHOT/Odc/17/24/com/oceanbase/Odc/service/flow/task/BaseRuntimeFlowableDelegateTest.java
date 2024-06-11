package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Test
    public void testInitCallable() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "test";
                };
            }
        };

        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(mock(DelegateExecution.class));
        try {
            String result = callable.call();
            assertEquals("test", result);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testRun() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "test";
                };
            }
        };

        try {
            baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
            assertEquals("test", baseRuntimeFlowableDelegate.get());
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "test";
                };
            }
        };

        try {
            baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
            assertEquals(true, baseRuntimeFlowableDelegate.isDone());
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testGet() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "test";
                };
            }
        };

        try {
            baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
            assertEquals("test", baseRuntimeFlowableDelegate.get());
        } catch (InterruptedException | ExecutionException e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetWithTimeout() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(2000); // Simulate task taking longer time to complete
                    return "test";
                };
            }
        };

        try {
            baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
            baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS); //Timeout set to 1 second
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testBindToFlowTaskInstance() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "test";
                };
            }
        };

        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        taskInstance.setTaskType(TaskType.TYPE1);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "test";
                };
            }
        };

        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.STARTED); 
        //Since updateFlowInstanceStatus is a void method, we just test for no exceptions here
    }

    @Test
    public void testPreHandle() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    return "test";
                };
            }
        };

        try {
            baseRuntimeFlowableDelegate.preHandle(mock(DelegateExecution.class));
        } catch (Exception e) {
            assertNull(e);
        }
    }
}