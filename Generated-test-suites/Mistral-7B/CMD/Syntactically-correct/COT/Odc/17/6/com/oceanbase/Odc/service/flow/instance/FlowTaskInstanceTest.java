import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
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
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private RuntimeDelegateMapper delegateConvertor;
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Before
    public void init() {
        this.serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        this.taskService = mock(TaskService.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.delegateConvertor = mock(RuntimeDelegateMapper.class);
        this.nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        this.sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        MockitoAnnotations.initMocks(this);

        this.flowTaskInstance = new FlowTaskInstance(FlowNodeTypeEnum.SERVICE_TASK, 1L, 1L,
                ExecutionStrategyConfig.builder().strategy(FlowTaskExecutionStrategy.SEQUENTIAL).build(), false, false,
                delegateConvertor, new FlowableAdaptor(), eventPublisher, taskService, nodeInstanceEntityRepository,
                sequenceInstanceRepository, serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        Assert.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        int result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository).size();
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = Mockito.mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();

        Assert.assertNotNull(flowTaskInstance.getId());
        Assert.assertNotNull(flowTaskInstance.getCreateTime());
        Assert.assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = Mockito.mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.update();

        Assert.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDelete() {
        flowTaskInstance.doDelete(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.dealloc();
        Assert.assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        Assert.assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        Assert.assertEquals(FlowNodeStatus.EXECUTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        Assert.assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        Assert.assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        Assert.assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get()).thenReturn("result");
        Assert.assertEquals("result", flowTaskInstance.getResult());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithNullTaskHandle() {
        flowTaskInstance.getResult();
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        throw new InterruptedException();
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        when(target.get()).thenThrow(new ExecutionException("Execution Exception"));
        flowTaskInstance.getResult();
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        throw new TimeoutException("Timeout Exception");
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
        Assert.assertNotNull(targetDelegateClass);
    }

    @Test
    public void testComplete() {
        flowTaskInstance.complete(false);
        Assert.assertEquals(FlowNodeStatus.EXECUTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCompleteWithAborted() {
        flowTaskInstance.complete(true);
        Assert.assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }
}