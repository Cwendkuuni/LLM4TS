
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    @Mock
    ServiceTaskInstanceEntity mockEntity;
    @Mock
    RuntimeDelegateMapper mockRuntimeDelegateMapper;
    @Mock
    FlowableAdaptor mockFlowableAdaptor;
    @Mock
    EventPublisher mockEventPublisher;
    @Mock
    TaskService mockTaskService;
    @Mock
    NodeInstanceEntityRepository mockNodeRepository;
    @Mock
    SequenceInstanceRepository mockSequenceRepository;
    @Mock
    ServiceTaskInstanceRepository mockServiceTaskRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(mockEntity, mockRuntimeDelegateMapper, mockFlowableAdaptor, 
            mockEventPublisher, mockTaskService, mockNodeRepository, mockSequenceRepository, mockServiceTaskRepository);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockDelegate);
        flowTaskInstance.cancel(false);
        verify(mockDelegate, times(1)).cancel(false);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockDelegate.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        Object result = new Object();
        when(mockDelegate.get()).thenReturn(result);
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertEquals(result, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
        Object result = new Object();
        when(mockDelegate.get(anyLong(), any(TimeUnit.class))).thenReturn(result);
        flowTaskInstance.bindServiceTask(mockDelegate);
        assertEquals(result, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWithoutBinding() {
        flowTaskInstance.cancel(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneWithoutBinding() {
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithoutBinding() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeoutWithoutBinding() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(mockRuntimeDelegateMapper.map(TaskType.DEFAULT), flowTaskInstance.getTargetDelegateClass());
    }
}
