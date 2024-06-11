
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private EventPublisher eventPublisher;
    
    @Test
    public void testExecute() {
        TestableBaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = new TestableBaseRuntimeFlowableDelegate(eventPublisher);
        
        try {
            baseRuntimeFlowableDelegate.execute(delegateExecution);
        } catch(Exception e){}
    
        verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.getTaskInstanceCreatedListener());
        verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.getActiveTaskstatisticsListener());
    }
    
    
    private static class TestableBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Object> {
        
        private TestableBaseRuntimeFlowableDelegate(EventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
        }
        
        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            // no implementation is required for the purpose of this test
            return null;
        }
        
        @Override
        protected void run(DelegateExecution execution) throws Exception {
            throw new Exception("Test Exception");
        }
    }
}
