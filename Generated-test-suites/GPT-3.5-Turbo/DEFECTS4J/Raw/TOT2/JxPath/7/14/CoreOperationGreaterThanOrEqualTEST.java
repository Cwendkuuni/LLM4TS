import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
   
    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        Object result = greaterThanOrEqual.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
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
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValueEqualValues() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(5);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        
        Object result = greaterThanOrEqual.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
}