import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

@RunWith(MockitoJUnitRunner.class)
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
    private RuntimeDelegateMapper delegateMapper;
    
    @Test
    public void testBindServiceTask() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateMapper, 
            new FlowableAdaptor(), eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        
        BaseRuntimeFlowableDelegate<?> targetTask = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTask);
        
        assertEquals(targetTask, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }
    
    @Test
    public void testConfirmExecute() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateMapper, 
            new FlowableAdaptor(), eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        
        flowTaskInstance.confirmExecute();
        
        assertEquals(FlowNodeStatus.EXECUTED, flowTaskInstance.getStatus());
    }
    
    @Test
    public void testAbort() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateMapper, 
            new FlowableAdaptor(), eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        
        flowTaskInstance.abort();
        
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }
    
    @Test
    public void testCancel() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateMapper, 
            new FlowableAdaptor(), eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        
        BaseRuntimeFlowableDelegate<?> targetTask = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTask);
        
        flowTaskInstance.cancel(false);
        
        Mockito.verify(targetTask, Mockito.times(1)).cancel(false);
    }
}