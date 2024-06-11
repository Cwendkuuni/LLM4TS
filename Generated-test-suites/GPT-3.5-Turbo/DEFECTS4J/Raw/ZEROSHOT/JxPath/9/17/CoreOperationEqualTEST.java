import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equalArguments_true() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = coreOperationEqual.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_nonEqualArguments_false() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg2.setProperty("testProperty", "testValue");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = coreOperationEqual.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        String symbol = coreOperationEqual.getSymbol();
        
        assertEquals("=", symbol);
    }

}