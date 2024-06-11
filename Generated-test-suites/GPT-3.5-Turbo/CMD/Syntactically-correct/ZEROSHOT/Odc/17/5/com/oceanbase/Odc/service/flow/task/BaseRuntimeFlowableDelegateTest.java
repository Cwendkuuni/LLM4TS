import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Test
    public void testInit() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Success";
            }
        };
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);

        baseRuntimeFlowableDelegate.init(eventPublisher);

        verify(eventPublisher, times(1)).addEventListener(any());
        verify(eventPublisher, times(1)).addEventListener(any());
    }

    @Test
    public void testRun() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Success";
            }
        };
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);

        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));

        baseRuntimeFlowableDelegate.run(execution);

        Assert.assertTrue(baseRuntimeFlowableDelegate.isDone());
        Assert.assertEquals("Test Success", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Success";
            }
        };

        baseRuntimeFlowableDelegate.run(execution);

        Assert.assertTrue(baseRuntimeFlowableDelegate.get().equals("Test Success"));
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Success";
            }
        };

        baseRuntimeFlowableDelegate.run(execution);

        Assert.assertTrue(baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS).equals("Test Success"));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Success";
            }
        };
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        Assert.assertEquals(baseRuntimeFlowableDelegate.getTargetTaskInstanceId(), 1L);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Success";
            }
        };
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);

        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.RUNNING);

        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.RUNNING);
    }

}