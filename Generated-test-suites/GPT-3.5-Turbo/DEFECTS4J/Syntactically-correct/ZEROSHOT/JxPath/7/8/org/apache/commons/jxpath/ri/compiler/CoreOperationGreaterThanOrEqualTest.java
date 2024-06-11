import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    private CoreOperationGreaterThanOrEqual operation;
    
    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
    }
    
    @Test
    public void testComputeValue() {
        // Test case 1
        EvalContext context1 = new EvalContext();
        Double arg1_1 = 5.0;
        Double arg2_1 = 3.0;
        operation.args[0] = new ConstantExpression(arg1_1);
        operation.args[1] = new ConstantExpression(arg2_1);
        Object result1 = operation.computeValue(context1);
        assertEquals(Boolean.TRUE, result1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        Double arg1_2 = 3.0;
        Double arg2_2 = 5.0;
        operation.args[0] = new ConstantExpression(arg1_2);
        operation.args[1] = new ConstantExpression(arg2_2);
        Object result2 = operation.computeValue(context2);
        assertEquals(Boolean.FALSE, result2);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
    
}