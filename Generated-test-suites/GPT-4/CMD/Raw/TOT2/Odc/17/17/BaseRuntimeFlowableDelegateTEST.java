import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {
    @Test
    public void testIsDone() {
        BaseRuntimeFlowableObjectDelegate instance = new BaseRuntimeFlowableObjectDelegateStub();
        assertFalse(instance.isDone());  // Assuming the instance is freshly created, it should not be done yet.
        instance.setDone(true);  // We assume setDone is a method in your Stub
        assertTrue(instance.isDone());
    }
}