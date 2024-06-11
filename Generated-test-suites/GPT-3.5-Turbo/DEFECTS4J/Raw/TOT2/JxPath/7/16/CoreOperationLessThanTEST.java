import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Constant(10), new Constant(5));
        assertEquals("<", coreOperation.getSymbol());
    }

    @Test
    public void testEdgeCases() {
        // Test when one argument is null
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Constant(null), new Constant(10));
        EvalContext context = new EvalContext();
        
        // Exception handling scenario
        try {
            coreOperation.computeValue(context);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

}