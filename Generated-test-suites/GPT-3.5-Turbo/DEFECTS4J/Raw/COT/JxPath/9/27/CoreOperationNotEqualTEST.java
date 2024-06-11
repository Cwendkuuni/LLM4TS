
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual_ReturnsTrue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_Equal_ReturnsFalse() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Set equal values for arg1 and arg2
        arg1.setValue(1);
        arg2.setValue(1);
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol_ReturnsNotEqualSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals("!=", symbol);
    }

}
