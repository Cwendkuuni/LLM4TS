
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.junit.Test;

public class FlowTaskInstanceTest {

    private ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
    private TaskService taskService = mock(TaskService.class);
    private EventPublisher eventPublisher = mock(EventPublisher.class);
    private RuntimeDelegateMapper delegateConvertor = mock(RuntimeDelegateMapper.class);
    private FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
    private NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
    private SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);

    @Test
    public void testBindServiceTask() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(mock(ServiceTaskInstanceEntity.class), delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        
        flowTaskInstance.bindServiceTask(target);
        
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(mock(ServiceTaskInstanceEntity.class), delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        
        flowTaskInstance.cancel(false);
        
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelNotPresent() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(mock(ServiceTaskInstanceEntity.class), delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        
        flowTaskInstance.cancel(false);
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(mock(ServiceTaskInstanceEntity.class), delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        
        when(target.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneNotPresent() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(mock(ServiceTaskInstanceEntity.class), delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        
        flowTaskInstance.isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(mock(ServiceTaskInstanceEntity.class), delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        
        Object result = new Object(); // Mock actual result
        when(target.get()).thenReturn(result);
        
        assertEquals(result, flowTaskInstance.getResult());
    }

    // Additional test cases for potential edge cases and exception handling scenarios go here

}

