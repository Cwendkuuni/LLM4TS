import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        // Typical use case
        EvalContext context1 = new EvalContext();
        Object result1 = coreOperationNotEqual.computeValue(context1);
        assertEquals(Boolean.TRUE, result1);
        
        // Null values
        EvalContext context2 = new EvalContext();
        arg1 = null;
        arg2 = null;
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result2 = coreOperationNotEqual.computeValue(context2);
        assertEquals(Boolean.FALSE, result2);
        
        // Edge case: arg1 and arg2 are equal
        EvalContext context3 = new EvalContext();
        arg1 = new Expression();
        arg2 = arg1;
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result3 = coreOperationNotEqual.computeValue(context3);
        assertEquals(Boolean.FALSE, result3);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }

}