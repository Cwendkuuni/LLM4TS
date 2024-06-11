import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueWithLessThan() {
        Expression arg1 = new Constant(5.0);
        Expression arg2 = new Constant(10.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);
        assertTrue((Boolean) result);
    }

   @Test
    public void testComputeValueWithNotLessThan() {
        Expression arg1 = new Constant(10.0);
        Expression arg2 = new Constant(5.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = lessThan.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5.0);
        Expression arg2 = new Constant(10.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", lessThan.getSymbol());
    }

    @Test
    public void testComputeValueWithNullContext() {
        Expression arg1 = new Constant(5.0);
        Expression arg2 = new Constant(10.0);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = lessThan.computeValue(null);
        assertNull(result);
    }

}