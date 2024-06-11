import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 20);

        Object result = operation.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        context.setVariable("arg1", 20);
        context.setVariable("arg2", 10);

        Object result = operation.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_Equal() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        context.setVariable("arg1", 20);
        context.setVariable("arg2", 20);

        Object result = operation.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        assertEquals(">=", operation.getSymbol());
    }
}