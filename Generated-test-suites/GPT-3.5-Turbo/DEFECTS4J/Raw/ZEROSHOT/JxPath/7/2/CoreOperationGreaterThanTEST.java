import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        expression.args[0] = new Expression();
        expression.args[1] = new Expression();
        double value1 = 10.5;
        double value2 = 5.5;
        context1.setVal(value1, value2);
        Object result1 = expression.computeValue(context1);
        assertTrue("Test case 1 failed", result1.equals(Boolean.TRUE));
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        expression.args[0] = new Expression();
        expression.args[1] = new Expression();
        double value3 = 5.5;
        double value4 = 10.5;
        context2.setVal(value3, value4);
        Object result2 = expression.computeValue(context2);
        assertTrue("Test case 2 failed", result2.equals(Boolean.FALSE));
        
        // Additional test cases can be added here
        
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        assertEquals(">", symbol);
    }
}