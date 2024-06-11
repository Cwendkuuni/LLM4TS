
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest{

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
    @Mock
    private BaseRuntimeFlowableDelegate targetTaskHandle;
    @Mock
    private ServiceTaskStartedListener listener;


    @Test
    public void testFlowTaskInstance() throws InterruptedException, ExecutionException, TimeoutException {
        
        when(entity.getId()).thenReturn(12345L);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, convertor,
            flowableAdaptor,
            eventPublisher,
            taskService,
            nodeRepository,
            sequenceRepository,
            serviceTaskRepository);
        assertNotNull(flowTaskInstance);

        // test bindServiceTask
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(12345L);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        // test isPresentOnThisMachine
        assertTrue(flowTaskInstance.isPresentOnThisMachine());

        // test dealloc
        flowTaskInstance.dealloc();

        // test alloc
        flowTaskInstance.alloc();

        // test abort
        flowTaskInstance.abort();

        // test cancel
        flowTaskInstance.cancel(false);

        // test isDone
        when(targetTaskHandle.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());

        // test getResult
        when(targetTaskHandle.get()).thenReturn("TestResults");
        assertEquals("TestResults", flowTaskInstance.getResult());

        // test getResult with timeOut
        when(targetTaskHandle.get(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn("TestResults");
        assertEquals("TestResults", flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS));

        // test resourceType
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }
}
