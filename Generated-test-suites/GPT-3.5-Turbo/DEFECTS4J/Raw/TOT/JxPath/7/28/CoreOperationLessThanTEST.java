import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression("5"), new Expression("10"));
        EvalContext context = new EvalContext();
        
        // Typical use case: left value is less than right value
        Object result = operation.computeValue(context);
        assertTrue((boolean) result);
        
        // Typical use case: left value is equal to right value
        CoreOperationLessThan operation2 = new CoreOperationLessThan(new Expression("5"), new Expression("5"));
        result = operation2.computeValue(context);
        assertFalse((boolean) result);
        
        // Typical use case: left value is greater than right value
        CoreOperationLessThan operation3 = new CoreOperationLessThan(new Expression("10"), new Expression("5"));
        result = operation3.computeValue(context);
        assertFalse((boolean) result);
        
        // Edge case: left value is negative
        CoreOperationLessThan operation4 = new CoreOperationLessThan(new Expression("-5"), new Expression("10"));
        result = operation4.computeValue(context);
        assertTrue((boolean) result);
        
        // Edge case: right value is negative
        CoreOperationLessThan operation5 = new CoreOperationLessThan(new Expression("5"), new Expression("-10"));
        result = operation5.computeValue(context);
        assertFalse((boolean) result);
        
        // Error scenario: left value is null
        CoreOperationLessThan operation6 = new CoreOperationLessThan(null, new Expression("10"));
        result = operation6.computeValue(context);
        assertFalse((boolean) result);
        
        // Error scenario: right value is null
        CoreOperationLessThan operation7 = new CoreOperationLessThan(new Expression("5"), null);
        result = operation7.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression("5"), new Expression("10"));
        String symbol = operation.getSymbol();
        
        assertEquals("<", symbol);
    }
}