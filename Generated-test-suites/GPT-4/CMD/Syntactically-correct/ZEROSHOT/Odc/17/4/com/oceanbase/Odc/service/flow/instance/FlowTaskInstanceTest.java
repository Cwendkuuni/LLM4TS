
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;


public class FlowTaskInstanceTest {

    @Test
    public void bindServiceTaskTest() {
        // Create mocks
        BaseRuntimeFlowableDelegate delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance instance = Mockito.mock(FlowTaskInstance.class);
 
        // Define the behavior
        Mockito.when(delegate.getTargetTaskInstanceId()).thenReturn(1L);
        Mockito.when(instance.getId()).thenReturn(1L);
       
        // Call method to test
        instance.bindServiceTask(delegate);
       
        // Verify if setStatus method was called
        Mockito.verify(instance).setStatus(FlowNodeStatus.EXECUTING);
    }
    
    @Test
    public void isPresentOnThisMachineTest() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertFalse(instance.isPresentOnThisMachine());
        instance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
        assertTrue(instance.isPresentOnThisMachine());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Create mocks
        Future future = Mockito.mock(Future.class);
        BaseRuntimeFlowableDelegate delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance instance = new FlowTaskInstance();
        
        // Define the behavior
        Mockito.when(future.get()).thenReturn("Something");
        Mockito.when(delegate.get()).thenReturn(future.get());
        
        // Bind delegate to instance
        instance.bindServiceTask(delegate);
        
        // Get result
        assertEquals("Something", instance.getResult());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance instance = Mockito.mock(FlowTaskInstance.class);
        Mockito.when(instance.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        instance.confirmExecute();
        Mockito.verify(instance, Mockito.times(1)).complete(false);
    }

    @Test
    public void testAbort() {
        FlowTaskInstance instance = Mockito.mock(FlowTaskInstance.class);
        instance.abort();
        Mockito.verify(instance, Mockito.times(1)).complete(true);
    }
}
