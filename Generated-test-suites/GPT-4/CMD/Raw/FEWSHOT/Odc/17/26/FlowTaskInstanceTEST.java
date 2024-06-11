package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {
    
    @Mock
    private FlowableAdaptor mockFlowableAdaptor;

    @Mock
    private TaskService mockTaskService;

    @Mock
    private EventPublisher mockEventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private ServiceTaskInstanceRepository mockServiceTaskRepository;

    @Mock
    private NodeInstanceEntityRepository mockNodeRepository;

    @Mock
    private SequenceInstanceRepository mockSequenceRepository;

    @Mock
    private Task mockTask;

    @Mock
    private BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle;

    private FlowTaskInstance flowTaskInstance;

    private final long organizationId = 1L;

    private final ExecutionStrategyConfig strategyConfig = mock(ExecutionStrategyConfig.class);

    private final TaskType taskType = TaskType.AUTO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, 1L, strategyConfig, false, false,
                delegateConvertor, mockFlowableAdaptor,
                mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository,
                mockServiceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBindServiceTask() {
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        assertTrue(flowTaskInstance.isPresentOnThisMachine()); // it should be on this machine after the bind
    }

    @Test
    public void testConfirmExecute() {
        try {
            flowTaskInstance.confirmExecute();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    public void testAbort() {
        try {
            flowTaskInstance.abort();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    public void testCancel() {
        when(mockTargetTaskHandle.isDone()).thenReturn(true);
        try {
            // we expect an unsupported operation exception, since the task isn't bound yet
            flowTaskInstance.cancel(false);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
        
        // bind task, expected not to throw an exception
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        flowTaskInstance.cancel(false);
    }

    @Test
    public void testIsDone() {
        try {
            // we expect an exception, since the task isn't bound yet
            flowTaskInstance.isDone();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }

        // bind task, expect true as result
        when(mockTargetTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        try {
            // we expect an exception, since the task isn't bound yet
            flowTaskInstance.getResult();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }

        // bind task, expect null as result
        when(mockTargetTaskHandle.get()).thenReturn(null);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        assertEquals(null, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        try {
            // we expect an exception, since the task isn't bound yet
            flowTaskInstance.getResult(5, TimeUnit.SECONDS);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }

        // bind task, expect null as result
        when(mockTargetTaskHandle.get(5, TimeUnit.SECONDS)).thenReturn(null);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        assertEquals(null, flowTaskInstance.getResult(5, TimeUnit.SECONDS));
    }
    
    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(entity.getId()).thenReturn(1L);
        when(mockServiceTaskRepository.save(any())).thenReturn(entity);
        flowTaskInstance.create();
        verify(mockServiceTaskRepository).save(any());
        assertEquals(1, flowTaskInstance.getId().longValue());
    }
    
    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        verify(mockServiceTaskRepository).update(any());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(mock(ServiceTaskInstanceEntity.class));

        when(mockServiceTaskRepository.batchCreate(any())).thenReturn(entities);

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, mockServiceTaskRepository);
        verify(mockServiceTaskRepository).batchCreate(any());
        assertEquals(instances.size(), result.size());
    }
}