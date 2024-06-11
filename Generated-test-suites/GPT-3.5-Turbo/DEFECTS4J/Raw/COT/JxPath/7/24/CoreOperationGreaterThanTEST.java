import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Basic functionality test
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);
        assertEquals(Boolean.TRUE, op.computeValue(context));  

        // Edge case: testing with equal values
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 5);
        assertEquals(Boolean.FALSE, op.computeValue(context));
        
        // Edge case: testing with negative values
        context.setVariable("arg1", -5);
        context.setVariable("arg2", -3);
        assertEquals(Boolean.FALSE, op.computeValue(context));
        
        // Edge case: testing with decimal values
        context.setVariable("arg1", 5.5);
        context.setVariable("arg2", 3.3);
        assertEquals(Boolean.TRUE, op.computeValue(context));

    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(new Expression(), new Expression());
        
        // Basic functionality test
        assertEquals(">", op.getSymbol());  
    }
}