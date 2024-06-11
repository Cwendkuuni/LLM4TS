import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_LessThan() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);
        
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_Equal() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(5);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);
        
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        
        assertEquals("<", lessThan.getSymbol());
    }
}