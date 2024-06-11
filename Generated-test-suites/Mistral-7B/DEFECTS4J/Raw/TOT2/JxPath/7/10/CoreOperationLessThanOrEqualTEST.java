import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        Expression arg1 = new ConstantExpression(1.0); // basic test
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);
    }

    // Edge cases and exception handling scenarios:

    @Test(expected = ClassCastException.class)
    public void testArg1NotNumberType() {
        Expression arg1 = new StringExpression("1"); // arg1 is not a number type
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationLessThanOrEqual.computeValue(context); // should throw an exception
    }

    @Test(expected = ClassCastException.class)
    public void testArg2NotNumberType() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new StringExpression("2"); // arg2 is not a number type
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationLessThanOrEqual.computeValue(context); // should throw an exception
    }

    @Test
    public void testLessThan() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.1);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testEqual() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result); // this should be an equal sign test, but since the given class does less than or equal, it should still pass
    }
}