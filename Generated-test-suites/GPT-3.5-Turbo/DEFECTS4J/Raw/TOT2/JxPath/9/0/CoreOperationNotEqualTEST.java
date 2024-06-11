import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_NotEqual() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 5);
        
        Object result = coreOperation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_Equal() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 10);
        
        Object result = coreOperation.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("arg1"), new Expression("arg2"));
        
        String symbol = coreOperation.getSymbol();
        
        assertEquals("!=", symbol);
    }
}