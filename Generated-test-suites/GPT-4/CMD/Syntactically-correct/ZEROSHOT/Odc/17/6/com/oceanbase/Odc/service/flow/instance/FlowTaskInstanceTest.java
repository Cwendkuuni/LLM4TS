
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntityRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private FlowableAdaptor flowableAdaptorMock;
    private EventPublisher eventPublisherMock;
    private TaskService taskServiceMock;
    private NodeInstanceEntityRepository nodeRepositoryMock;
    private SequenceInstanceRepository sequenceRepositoryMock;
    private ServiceTaskInstanceRepository serviceTaskRepositoryMock;
    private ExecutionStrategyConfig strategyConfigMock;
    private RuntimeDelegateMapper delegateConvertorMock;
    private BaseRuntimeFlowableDelegate targetTaskHandleMock;

    @Before
    public void setUp() {
        flowableAdaptorMock = mock(FlowableAdaptor.class);
        eventPublisherMock = mock(EventPublisher.class);
        taskServiceMock = mock(TaskService.class);
        nodeRepositoryMock = mock(NodeInstanceEntityRepository.class);
        sequenceRepositoryMock = mock(SequenceInstanceRepository.class);
        serviceTaskRepositoryMock = mock(ServiceTaskInstanceRepository.class);
        strategyConfigMock = mock(ExecutionStrategyConfig.class);
        delegateConvertorMock = mock(RuntimeDelegateMapper.class);
        targetTaskHandleMock = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance = new FlowTaskInstance(TaskType.SOME_TASK, 1L, 1L, strategyConfigMock, true, true,
            delegateConvertorMock, flowableAdaptorMock, eventPublisherMock, taskServiceMock, nodeRepositoryMock,
            sequenceRepositoryMock, serviceTaskRepositoryMock);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBindServiceTask() {
        when(targetTaskHandleMock.getTargetTaskInstanceId()).thenReturn(1L);

        flowTaskInstance.bindServiceTask(targetTaskHandleMock);

        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());

        flowTaskInstance.bindServiceTask(targetTaskHandleMock);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() throws ExecutionException, InterruptedException {
        flowTaskInstance.bindServiceTask(targetTaskHandleMock);
        when(targetTaskHandleMock.isDone()).thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.getResult(10, TimeUnit.SECONDS);
    }
}
