import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    // Test constructor
    @Test
    public void testConstructor() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(operationGreaterThan);
    }
    
    // Test computeValue for l > r
    @Test
    public void testComputeValueGreater() {
        EvalContext context = new MockEvalContext();
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(3);
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.TRUE, operationGreaterThan.computeValue(context));
    }
    
    // Test computeValue for l <= r
    @Test
    public void testComputeValueNotGreater() {
        EvalContext context = new MockEvalContext();
        Expression arg1 = new ConstantExpression(2);
        Expression arg2 = new ConstantExpression(4);
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.FALSE, operationGreaterThan.computeValue(context));
    }
    
    // Test getSymbol
    @Test
    public void testGetSymbol() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", operationGreaterThan.getSymbol());
    }
    
    // Test for edge case with NaN
    @Test(expected = Exception.class)
    public void testComputeValueNaN() {
        EvalContext context = new MockEvalContext();
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(Double.NaN);
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        operationGreaterThan.computeValue(context);
    }
    
    // Test for edge case with Infinity
    @Test
    public void testComputeValueInfinity() {
        EvalContext context = new MockEvalContext();
        Expression arg1 = new ConstantExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new ConstantExpression(Double.NEGATIVE_INFINITY);
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.TRUE, operationGreaterThan.computeValue(context));
    }
}