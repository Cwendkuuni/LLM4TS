import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for {@link FlowTaskInstance}
 *
 * @author yh263208
 * @date 2023-03-16 10:10
 */
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private ServiceTaskStartedListener serviceTaskStartedListener;
    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(new FlowableElement()));
    }

    @Test
    public void testCreate() throws Exception {
        // Arrange
        doNothing().when(serviceTaskRepository).save(any(ServiceTaskInstanceEntity.class));

        // Act
        flowTaskInstance.create();

        // Assert
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
        Verify.verifyNotEmpty(flowTaskInstance.getId());
        Verify.verifyNotEmpty(flowTaskInstance.getCreateTime());
        Verify.verifyNotEmpty(flowTaskInstance.getUpdateTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateAlreadyExists() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Error creating task")).when(serviceTaskRepository)
                .save(any(ServiceTaskInstanceEntity.class));
        doReturn(true).when(flowTaskInstance).exists();

        // Act & Assert
        flowTaskInstance.create();

        // Verify exception
        Verify.verifyException(IllegalStateException.class);
    }

    @Test
    public void testUpdate() throws Exception {
        // Arrange
        doReturn(true).when(flowTaskInstance).exists();
        doNothing().when(serviceTaskRepository).update(any(ServiceTaskInstanceEntity.class));

        // Act
        flowTaskInstance.update();

        // Assert
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateNotFound() throws Exception {
        // Arrange
        doReturn(false).when(flowTaskInstance).exists();

        // Act & Assert
        flowTaskInstance.update();

        // Verify exception
        Verify.verifyException(IllegalStateException.class);
    }

    @Test
    public void testDelete() throws Exception {
        // Arrange
        doNothing().when(serviceTaskRepository).deleteById(anyLong());

        // Act
        flowTaskInstance.delete();

        // Assert
        verify(serviceTaskRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Error deleting task")).when(serviceTaskRepository)
                .deleteById(anyLong());
        doReturn(false).when(flowTaskInstance).exists();

        // Act & Assert
        flowTaskInstance.delete();

        // Verify exception
        Verify.verifyException(IllegalStateException.class);
    }

    @Test
    public void testBindServiceTask() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        // Act
        flowTaskInstance.bindServiceTask(target);

        // Assert
        verify(flowTaskInstance, times(1)).setStatus(eq(FlowNodeStatus.EXECUTING));
        verify(flowTaskInstance, times(1)).targetTaskHandle = target;
    }

    @Test
    public void testIsPresentOnThisMachine() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Act
        boolean isPresent = flowTaskInstance.isPresentOnThisMachine();

        // Assert
        assertTrue(isPresent);
    }

    @Test
    public void testDealloc() throws Exception {
        // Arrange
        doNothing().when(eventPublisher).removeEventListener(any(ServiceTaskStartedListener.class));
        flowTaskInstance.alloc();

        // Act
        flowTaskInstance.dealloc();

        // Assert
        verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
        verify(flowTaskInstance, times(1)).listener = null;
    }

    @Test
    public void testAlloc() throws Exception {
        // Arrange
        doNothing().when(eventPublisher).addEventListener(any(ServiceTaskStartedListener.class));

        // Act
        flowTaskInstance.alloc();

        // Assert
        verify(eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
        verify(flowTaskInstance, times(1)).listener = eq(serviceTaskStartedListener);
    }

    @Test
    public void testConfirmExecute() throws Exception {
        // Arrange
        doNothing().when(flowableAdaptor).getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        doNothing().when(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));

        // Act
        flowTaskInstance.confirmExecute();

        // Assert
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK));
        verify(flowTaskInstance, times(1)).complete(false);
    }

    @Test
    public void testAbort() throws Exception {
        // Arrange
        doNothing().when(flowableAdaptor).getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        doNothing().when(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));

        // Act
        flowTaskInstance.abort();

        // Assert
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), any(FlowableElementType.class),
                eq(FlowableElementType.USER_TASK));
        verify(flowTaskInstance, times(1)).complete(true);
    }

    @Test
    public void testCancel() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Act
        flowTaskInstance.cancel(false);

        // Assert
        verify(target, times(1)).cancel(false);
    }

    @Test
    public void testIsDone() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Act
        when(target.isDone()).thenReturn(true);

        // Assert
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Act
        when(target.get()).thenReturn("Result");

        // Assert
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Act & Assert
        target.get();
        flowTaskInstance.getResult();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecution() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        doThrow(new ExecutionException("Error getting result")).when(target).get();

        // Act & Assert
        flowTaskInstance.getResult();

        // Verify exception
        Verify.verifyException(ExecutionException.class);
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeout() throws Exception {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        doThrow(new TimeoutException("Error getting result")).when(target).get(anyLong(), any(TimeUnit.class));

        // Act & Assert
        flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS);

        // Verify exception
        Verify.verifyException(TimeoutException.class);
    }

}