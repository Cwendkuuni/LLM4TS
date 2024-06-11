import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        arg1 = new Constant(10);
        arg2 = new Constant(5);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals("<=", operation.getSymbol());
    }
    
    @Test
    public void testEdgeCases() {
        Expression arg1 = new Constant(Double.MIN_VALUE);
        Expression arg2 = new Constant(Double.MAX_VALUE);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        arg1 = new Constant(Double.MAX_VALUE);
        arg2 = new Constant(Double.MIN_VALUE);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);
    }

}