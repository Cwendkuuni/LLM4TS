import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTask.getTargetTaskInstanceId()).thenReturn(1L);
        
        flowTaskInstance.bindServiceTask(targetTask);
        
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        exceptionRule.expect(UnsupportedOperationException.class);
        exceptionRule.expectMessage("Task isn't on this machine");

        flowTaskInstance.abort();
    }

    @Test
    public void testIsDone() {
        exceptionRule.expect(UnsupportedOperationException.class);
        exceptionRule.expectMessage("Task isn't on this machine");

        flowTaskInstance.isDone();
    }

    @Test
    public void testGetResult() {
        exceptionRule.expect(UnsupportedOperationException.class);
        exceptionRule.expectMessage("Task isn't on this machine");

        flowTaskInstance.getResult();
    }

    @Test
    public void testGetResultWithTimeout() {
        exceptionRule.expect(UnsupportedOperationException.class);
        exceptionRule.expectMessage("Task isn't on this machine");

        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testComplete() {
        exceptionRule.expect(UnsupportedOperationException.class);
        exceptionRule.expectMessage("Automatic tasks do not support this execution");

        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.setStrategyConfig(ExecutionStrategyConfig.AUTO);

        flowTaskInstance.complete(false);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.save(any())).thenReturn(entity);

        flowTaskInstance.create();

        Mockito.verify(serviceTaskInstanceRepository, times(1)).save(any());
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.update(any())).thenReturn(1);

        flowTaskInstance.update();
        
        Mockito.verify(serviceTaskInstanceRepository, times(1)).update(any());
    }

    @Test
    public void testDoDelete() {
        Long instanceId = 1L;

        flowTaskInstance.doDelete(instanceId);

        Mockito.verify(serviceTaskInstanceRepository, times(1)).deleteById(instanceId);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);
        
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        when(serviceTaskInstanceRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(entity));

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        Mockito.verify(serviceTaskInstanceRepository, times(1)).batchCreate(anyList());
        assertEquals(entity.getId(), createdInstances.get(0).getId());
    }

}