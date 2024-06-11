import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.List;
import java.util.Map;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
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
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private RuntimeDelegateMapper mockRuntimeDelegateMapper;
    private FlowableAdaptor mockFlowableAdaptor;
    private EventPublisher mockEventPublisher;
    private TaskService mockTaskService;
    private NodeInstanceEntityRepository mockNodeRepository;
    private SequenceInstanceRepository mockSequenceRepository;
    private ServiceTaskInstanceRepository mockServiceTaskRepository;
    private ServiceTaskStartedListener mockListener;

    @Before
    public void setup() {
        mockRuntimeDelegateMapper = mock(RuntimeDelegateMapper.class);
        mockFlowableAdaptor = mock(FlowableAdaptor.class);
        mockEventPublisher = mock(EventPublisher.class);
        mockTaskService = mock(TaskService.class);
        mockNodeRepository = mock(NodeInstanceEntityRepository.class);
        mockSequenceRepository = mock(SequenceInstanceRepository.class);
        mockServiceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        mockListener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance = new FlowTaskInstance(TaskType.TYPE_ABC, 123L, 456L,
                new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20),
                false, true, mockRuntimeDelegateMapper, mockFlowableAdaptor,
                mockEventPublisher, mockTaskService, mockNodeRepository,
                mockSequenceRepository, mockServiceTaskRepository);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> mockTarget = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTarget);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        // Assert whatever is expected
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        // Assert whatever is expected
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> mockTarget = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTarget);
        flowTaskInstance.cancel(true);
        // Assert whatever is expected
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> mockTarget = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTarget);
        assertFalse(flowTaskInstance.isDone());
        // Assert whatever is expected
    }

    @Test
    public void testGetResult() {
        BaseRuntimeFlowableDelegate<?> mockTarget = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTarget);
        Object result = null;
        try {
            result = flowTaskInstance.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Assert whatever is expected
    }

    @Test
    public void testGetResultWithTimeout() {
        BaseRuntimeFlowableDelegate<?> mockTarget = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTarget);
        Object result = null;
        try {
            result = flowTaskInstance.getResult(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        // Assert whatever is expected
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        // Assert whatever is expected
    }
}