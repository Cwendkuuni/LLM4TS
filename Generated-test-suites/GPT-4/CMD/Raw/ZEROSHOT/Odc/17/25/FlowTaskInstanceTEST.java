
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceEntity entity;
    @Mock
    private RuntimeDelegateMapper convertor;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private TaskService taskService;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // Initially it should return false
        boolean isPresent = flowTaskInstance.isPresentOnThisMachine();
        assertFalse(isPresent);
    }

    @Test
    public void testAlloc() {
        try {
            flowTaskInstance.alloc();
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testComplete() {
        try {
            flowTaskInstance.complete(true);
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("Automatic tasks do not support this execution"));
        }

        try {
            flowTaskInstance.complete(false);
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("Automatic tasks do not support this execution"));
        }
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertNotNull(flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testAbort() {
        try {
            flowTaskInstance.abort();
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("Automatic tasks do not support this execution"));
        }
    }

    @Test
    public void testGetResult() {
        ExecutionException thrown = assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult());
        assertTrue(thrown.getMessage().contains("Task isn't on this machine"));
    }

    @Test
    public void testGetResultWithTimeout() {
        TimeoutException thrown = assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult(10L, TimeUnit.SECONDS));
        assertTrue(thrown.getMessage().contains("Task isn't on this machine"));
    }
}
