import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = greaterThanOrEqual.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new PropertyPointer("x");
        Expression arg2 = new Constant(5);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = greaterThanOrEqual.getSymbol();
        
        assertEquals(">=", symbol);
    }
    
    @Test
    public void testComputeValueWithNegativeValues() {
        Expression arg1 = new Constant(-5);
        Expression arg2 = new Constant(-3);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = greaterThanOrEqual.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullContextComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        greaterThanOrEqual.computeValue(null);
    }
}