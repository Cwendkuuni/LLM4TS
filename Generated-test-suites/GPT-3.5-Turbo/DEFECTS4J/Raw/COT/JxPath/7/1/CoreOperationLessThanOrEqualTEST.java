
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {
  
    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual();
        
        // Test case 1: arg1 < arg2
        Expression arg1 = new Expression();
        arg1.setValue(5);
        Expression arg2 = new Expression();
        arg2.setValue(10);
        coreOperationLessThanOrEqual.setArgs(new Expression[] { arg1, arg2 });
        
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Test case 2: arg1 = arg2
        arg1.setValue(10);
        arg2.setValue(10);
        
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Test case 3: arg1 > arg2
        arg1.setValue(10);
        arg2.setValue(5);
        
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual();
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        
        assertEquals("<=", symbol);
    }
    
    @Test
    public void testComputeValueWithNullArgs() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual();
        
        coreOperationLessThanOrEqual.setArgs(null);
        
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        
        assertNull(result);
    }
    
    @Test
    public void testComputeValueWithNullContext() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual();
        
        Expression arg1 = new Expression();
        arg1.setValue(5);
        Expression arg2 = new Expression();
        arg2.setValue(10);
        coreOperationLessThanOrEqual.setArgs(new Expression[] { arg1, arg2 });
        
        Object result = coreOperationLessThanOrEqual.computeValue(null);
        
        assertNull(result);
    }
}
