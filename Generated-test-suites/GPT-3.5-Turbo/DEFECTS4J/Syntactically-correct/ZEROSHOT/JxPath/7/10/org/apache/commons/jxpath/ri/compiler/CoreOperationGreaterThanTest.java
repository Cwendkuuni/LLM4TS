import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1
        expression.args[0].computeValue(context);
        expression.args[1].computeValue(context);
        boolean result1 = expression.computeValue(context);
        assertTrue(result1);

        // Test case 2
        expression.args[0].computeValue(context);
        expression.args[1].computeValue(context);
        boolean result2 = expression.computeValue(context);
        assertFalse(result2);
        
        // Add more test cases as needed
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        assertEquals(">", symbol);
    }
}