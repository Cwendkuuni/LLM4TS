
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class BaseRuntimeFlowableDelegateTest {

    BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegateTest;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegateTest = Mockito.spy(BaseRuntimeFlowableDelegate.class);
        assertThatThrownBy(() -> baseRuntimeFlowableDelegateTest.initCallable(null))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Callable");
    }
        
    @Test
    public void executeAndRunTest() throws Exception {
        DelegateExecution execution = Mockito.mock(ExecutionEntity.class);
        Callable<String> callableMock = Mockito.mock(Callable.class);
        Mockito.when(baseRuntimeFlowableDelegateTest.initCallable(execution)).thenReturn(callableMock);
        Mockito.when(callableMock.call()).thenReturn("Test String");
        baseRuntimeFlowableDelegateTest.execute(execution);
        Assert.assertEquals(baseRuntimeFlowableDelegateTest.get(), "Test String");
    }
   
    @Test
    public void runExceptionTest() throws Exception {
        DelegateExecution executionMock = Mockito.mock(ExecutionEntity.class);
        Mockito.doThrow(ServiceTaskError.class).when(baseRuntimeFlowableDelegateTest).run(executionMock);
        assertThatThrownBy(() -> baseRuntimeFlowableDelegateTest.run(executionMock))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasCauseExactlyInstanceOf(ServiceTaskError.class);
    }
    
    @Test
    public void isDoneTest() {
        Assert.assertFalse(baseRuntimeFlowableDelegateTest.isDone());
    }

    @Test
    public void getTest() throws InterruptedException, ExecutionException {
        // Since we will not be able to actually run the Future's run method in a test,
        // we test the InterruptedException.
        assertThatThrownBy(() -> baseRuntimeFlowableDelegateTest.get())
                .isExactlyInstanceOf(ExecutionException.class)
                .hasCauseExactlyInstanceOf(InterruptedException.class);
    }
    
    @Test
    public void getWithTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        assertThatThrownBy(() -> baseRuntimeFlowableDelegateTest.get(1000, TimeUnit.MILLISECONDS))
                .isExactlyInstanceOf(TimeoutException.class);
    }
}
