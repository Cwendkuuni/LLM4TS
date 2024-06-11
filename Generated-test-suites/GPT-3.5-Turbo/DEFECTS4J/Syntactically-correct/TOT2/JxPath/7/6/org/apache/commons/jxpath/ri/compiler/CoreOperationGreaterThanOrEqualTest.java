import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new ConstantExpression(5); // Typical use case
        Expression arg2 = new ConstantExpression(3); // Typical use case
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(null);
        
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueEqualNumbers() {
        Expression arg1 = new ConstantExpression(5); // Edge case - equal numbers
        Expression arg2 = new ConstantExpression(5); // Edge case - equal numbers
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(null);
        
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNegativeNumbers() {
        Expression arg1 = new ConstantExpression(-2); // Edge case - negative numbers
        Expression arg2 = new ConstantExpression(-5); // Edge case - negative numbers
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(null);
        
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new ConstantExpression(10); // Typical use case
        Expression arg2 = new ConstantExpression(8); // Typical use case
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        EvalContext context = new EvalContext(null);
        
        operation.computeValue(context); // Error scenario - null arguments
    }
}