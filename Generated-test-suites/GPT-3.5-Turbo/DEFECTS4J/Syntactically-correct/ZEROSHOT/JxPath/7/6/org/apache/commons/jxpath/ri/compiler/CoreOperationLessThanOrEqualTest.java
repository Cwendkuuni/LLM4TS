import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        // Test with arg1 > arg2
        EvalContext context1 = new EvalContext();
        context1.setExpression(expression);
        context1.setVariable("arg1", 5);
        context1.setVariable("arg2", 3);
        Object result1 = expression.computeValue(context1);
        assertEquals(false, result1);
        
        // Test with arg1 < arg2
        EvalContext context2 = new EvalContext();
        context2.setExpression(expression);
        context2.setVariable("arg1", 3);
        context2.setVariable("arg2", 5);
        Object result2 = expression.computeValue(context2);
        assertEquals(true, result2);
        
        // Test with arg1 = arg2
        EvalContext context3 = new EvalContext();
        context3.setExpression(expression);
        context3.setVariable("arg1", 3);
        context3.setVariable("arg2", 3);
        Object result3 = expression.computeValue(context3);
        assertEquals(true, result3);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = expression.getSymbol();
        assertEquals("<=", symbol);
    }
}