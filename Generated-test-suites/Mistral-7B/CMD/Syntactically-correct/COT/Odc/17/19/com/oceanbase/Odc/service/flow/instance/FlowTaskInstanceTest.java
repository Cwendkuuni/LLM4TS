package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;

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
import com.oceanbase.odc.service.flow.model.ExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for FlowTaskInstance
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
    private ServiceTaskStartedListener listener;

    @Mock
    private TaskInfo taskInfo;

    @Mock
    private Task task;

    @Mock
    private FlowNodeType nodeType;

    @Mock
    private FlowableElementType elementType;

    @Mock
    private FlowableElement element;

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    /**
     * Test create method
     */
    @Test
    public void testCreate() {
        flowTaskInstance.create();
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    /**
     * Test batchCreate method
     */
    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(null, null, null, null, false,
                false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(instances.size(), result.size());
    }

    /**
     * Test getCoreFlowableElementType method
     */
    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    /**
     * Test bindServiceTask method
     */
    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(target, flowTaskInstance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    /**
     * Test isPresentOnThisMachine method
     */
    @Test
    public void testIsPresentOnThisMachine() {
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.dealloc();
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.alloc();
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    /**
     * Test confirmExecute method
     */
    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
    }

    /**
     * Test abort method
     */
    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    /**
     * Test cancel method
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testCancel() throws InterruptedException, ExecutionException {
        flowTaskInstance.alloc();
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        flowTaskInstance.cancel(true);
        assertEquals(FlowNodeStatus.CANCELED, flowTaskInstance.getStatus());
    }

    /**
     * Test isDone method
     */
    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isDone());
        flowTaskInstance.cancel(true);
        assertFalse(flowTaskInstance.isDone());
    }

    /**
     * Test getResult method
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        String result = (String) flowTaskInstance.getResult();
        verify(target).get();
        assertEquals(result, target.get());
    }

    /**
     * Test getResult method with timeout
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        String result = (String) flowTaskInstance.getResult(5, TimeUnit.SECONDS);
        verify(target).get(5, TimeUnit.SECONDS);
        assertEquals(result, target.get());
    }

    /**
     * Test complete method
     */
    @Test
    public void testComplete() {
        flowTaskInstance.complete(false);
        assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
        flowTaskInstance.complete(true);
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    /**
     * Test mapToTaskEntity method
     */
    @Test
    public void testMapToTaskEntity() {
        ServiceTaskInstanceEntity result = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
        assertEquals(flowTaskInstance.getId(), result.getId());
        assertEquals(flowTaskInstance.getStatus(), result.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), result.getFlowInstanceId());
        assertEquals(flowTaskInstance.isStartEndpoint(), result.isStartEndpoint());
        assertEquals(flowTaskInstance.isEndEndpoint(), result.isEndEndpoint());
        assertEquals(flowTaskInstance.strategyConfig.getStrategy(), result.getStrategy());
        assertEquals(flowTaskInstance.getTaskType(), result.getTaskType());
        assertEquals(flowTaskInstance.getTargetTaskId(), result.getTargetTaskId());
        assertEquals(flowTaskInstance.strategyConfig.getPendingExpireIntervalSeconds(),
                result.getWaitExecExpireIntervalSeconds());
        assertEquals(flowTaskInstance.strategyConfig.getExecutionTime(), result.getExecutionTime());
    }
}