import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.setValue(10);
        arg2.setValue(5);
        assertTrue((Boolean) op.computeValue(context));

        // Edge case: Both arguments are equal
        arg1.setValue(10);
        arg2.setValue(10);
        assertTrue((Boolean) op.computeValue(context));

        // Edge case: Both arguments are zero
        arg1.setValue(0);
        arg2.setValue(0);
        assertTrue((Boolean) op.computeValue(context));

        // Error scenario: First argument is null
        arg1 = null;
        arg2.setValue(5);
        assertNull(op.computeValue(context));
        
        // Error scenario: Second argument is null
        arg1.setValue(10);
        arg2 = null;
        assertNull(op.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        assertEquals(">=", op.getSymbol());
    }
}