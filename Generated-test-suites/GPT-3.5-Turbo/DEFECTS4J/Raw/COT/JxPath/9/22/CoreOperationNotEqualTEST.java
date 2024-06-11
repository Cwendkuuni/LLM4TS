import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_notEqual() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_equal() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(expression1, expression1);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(expression1, expression2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
    
    // Additional edge cases and exception handling scenarios
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_nullArg1() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, expression2);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullArg2() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(expression1, null);
    }
    
    @Test
    public void testComputeValue_nullContext() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(expression1, expression2);
        Object result = operation.computeValue(null);
        assertTrue((boolean) result);
    }
}


// All the test cases go here

