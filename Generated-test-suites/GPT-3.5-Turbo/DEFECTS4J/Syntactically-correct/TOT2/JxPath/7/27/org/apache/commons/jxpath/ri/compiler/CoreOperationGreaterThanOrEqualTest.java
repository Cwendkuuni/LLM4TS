import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("3");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("5");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", coreOp.getSymbol());
    }

    @Test
    public void testComputeValueWithNaN() {
        Expression arg1 = new Constant("NaN");
        Expression arg2 = new Constant("5");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOp.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonNumericArgs() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("String");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOp.computeValue(context);
    }

}