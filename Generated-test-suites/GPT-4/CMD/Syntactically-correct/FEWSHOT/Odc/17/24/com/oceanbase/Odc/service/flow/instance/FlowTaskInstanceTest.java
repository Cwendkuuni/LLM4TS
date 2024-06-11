
package com.oceanbase.odc.service.flow.instance;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private TaskService mockTaskService;
    private ServiceTaskInstanceRepository mockServiceTaskRepository;

    @Before
    public void setUp() {
        mockTaskService = Mockito.mock(TaskService.class);
        mockServiceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);

        // Create a test FlowTaskInstance.
        // Adapted from 2nd constructor shown in task code, populated with sample data.
        // References to 'flowableAdaptor', 'eventPublisher', 'nodeRepository', and 'sequenceRepository' are mocked.
        this.flowTaskInstance = new FlowTaskInstance(TaskType.AUDIT, 1L, 1L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, null, null, null),
                true, false,
                Mockito.mock(RuntimeDelegateMapper.class),
                Mockito.mock(FlowableAdaptor.class),
                Mockito.mock(EventPublisher.class),
                mockTaskService,
                Mockito.mock(NodeInstanceEntityRepository.class),
                Mockito.mock(SequenceInstanceRepository.class),
                mockServiceTaskRepository);
    }

    @Test
    public void testCreateAndUpdateTask() {
        //Configure mocks
        when(mockServiceTaskRepository.save(any())).thenReturn(new ServiceTaskInstanceEntity());

        //Test Create
        flowTaskInstance.create();
        verify(mockServiceTaskRepository).save(any());
        
        //Test update
        when(mockServiceTaskRepository.update(any())).thenReturn(1);
        flowTaskInstance.update();
        verify(mockServiceTaskRepository).update(any());
    }

    @Test
    public void testDeleteTask() {
        // Test Delete
        flowTaskInstance.doDelete(1L);
        verify(mockServiceTaskRepository).deleteById(1L);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(mockDelegate);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testConfirmExecuteAndAbort() {
        //Save Task first
        when(mockServiceTaskRepository.save(any())).thenReturn(new ServiceTaskInstanceEntity());
        flowTaskInstance.create();
        verify(mockServiceTaskRepository).save(any());
        //Prepare for confirmExecute() and abort()
        when(mockTaskService.createTaskQuery()).thenReturn(Mockito.mock(TaskService.TaskQuery.class));
        Task dummyTask = Mockito.mock(Task.class);
        when(dummyTask.getId()).thenReturn("1000");
        List<Task> dummyList = Arrays.asList(dummyTask);
        when(flowTaskInstance.getFlowableAdaptor().getProcessInstanceIdByFlowInstanceId(anyLong()))
         .thenReturn(Optional.of("12345"));

        //Test confirmExecute()
        flowTaskInstance.confirmExecute();
        //Test abort()
        flowTaskInstance.abort();
    }
}
