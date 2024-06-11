import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new SomeExpression();
        Expression arg2 = new AnotherExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new SomeEvalContext();
        
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        Expression arg1 = new SomeExpression();
        Expression arg2 = new AnotherExpression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        // Test for null arguments
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        EvalContext context = new SomeEvalContext();
        try {
            coreOperationEqual.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Exception caught, test passed
        }
    }
}