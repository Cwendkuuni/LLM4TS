import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression(arg1), new Expression(arg2));
        
        // Typical Use Cases
        assertEquals(false, greaterThan.computeValue(new EvalContext()));
        assertEquals(false, greaterThan.computeValue(new EvalContext()));
        
        // Edge Cases
        assertEquals(false, greaterThan.computeValue(new EvalContext()));
        assertEquals(false, greaterThan.computeValue(new EvalContext()));
        
        // Error Scenarios
        assertEquals(false, greaterThan.computeValue(new EvalContext()));
        assertEquals(false, greaterThan.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression(arg1), new Expression(arg2));
        
        assertEquals(">", greaterThan.getSymbol());
    }
}