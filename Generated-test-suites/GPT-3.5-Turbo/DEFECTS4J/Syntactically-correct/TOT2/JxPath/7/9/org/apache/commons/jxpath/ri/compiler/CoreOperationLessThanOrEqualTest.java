import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    CoreOperationLessThanOrEqual coreOperation;

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5); // Edge case: arg1 is a constant value
        Expression arg2 = new Constant(10); // Edge case: arg2 is a constant value
        coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Typical use case
        Object result = coreOperation.computeValue(context);
        assertTrue((boolean) result);

        // Edge case: arg1 is greater than arg2
        arg1 = new Constant(15);
        coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        
        result = coreOperation.computeValue(context);
        assertFalse((boolean) result);

        // Edge case: arg1 and arg2 are equal
        arg1 = new Constant(10);
        coreOperation = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        
        result = coreOperation.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        coreOperation = new CoreOperationLessThanOrEqual(null, null);

        // Typical use case
        String symbol = coreOperation.getSymbol();
        assertEquals("<=", symbol);

        // Edge case: Testing if getSymbol always returns "<="
        coreOperation = new CoreOperationLessThanOrEqual(null, null);
        symbol = coreOperation.getSymbol();
        assertEquals("<=", symbol);
    }
}