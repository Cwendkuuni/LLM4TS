import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        assertEquals(Boolean.TRUE, op.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(10);
        Expression arg2 = new Constant(10);
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        assertEquals(">=", op.getSymbol());
    }
}