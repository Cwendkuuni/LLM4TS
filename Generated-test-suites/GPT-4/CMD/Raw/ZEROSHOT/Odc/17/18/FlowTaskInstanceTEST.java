
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
    private ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
    private TaskService taskService = mock(TaskService.class);
    private Task taskInfo = mock(Task.class);
    private ExecutionStrategyConfig config = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1, 1);
    private ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
    private BaseRuntimeFlowableDelegate delegate = mock(BaseRuntimeFlowableDelegate.class);

    @Test
    public void testFlowTaskInstanceNotNull() {
        FlowTaskInstance instance = new FlowTaskInstance(entity, null, flowableAdaptor, null, taskService,
                null, null, serviceTaskRepository);

        assertNotNull(instance);
        assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
    }

    @Test
    public void testUpdateTaskInstance() {
        FlowTaskInstance instance = new FlowTaskInstance(entity, null, flowableAdaptor, null, taskService,
                null, null, serviceTaskRepository);
        
        instance.update();
        verify(serviceTaskRepository, times(1)).update(entity);
    }

    @Test
    public void testDeleteTaskInstance() {
        FlowTaskInstance instance = new FlowTaskInstance(entity, null, flowableAdaptor, null, taskService,
                null, null, serviceTaskRepository);
        
        instance.delete(instance.getId());
        verify(serviceTaskRepository, times(1)).deleteById(instance.getId());
    }

    @Test
    public void testBindServiceTask() {
        when(delegate.getTargetTaskInstanceId()).thenReturn(1L);
        FlowTaskInstance instance = new FlowTaskInstance(entity, null, flowableAdaptor, null, taskService,
                null, null, serviceTaskRepository);

        instance.bindServiceTask(delegate);
        assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        when(delegate.isDone()).thenReturn(true);
        FlowTaskInstance instance = new FlowTaskInstance(entity, null, flowableAdaptor, null, taskService,
                null, null, serviceTaskRepository);

        instance.bindServiceTask(delegate);
        assertEquals(true, instance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.get(0, TimeUnit.MILLISECONDS)).thenReturn(true);
        FlowTaskInstance instance = new FlowTaskInstance(entity, null, flowableAdaptor, null, taskService,
                null, null, serviceTaskRepository);

        instance.bindServiceTask(delegate);
        assertEquals(true, instance.getResult(0, TimeUnit.MILLISECONDS));
    }
}
