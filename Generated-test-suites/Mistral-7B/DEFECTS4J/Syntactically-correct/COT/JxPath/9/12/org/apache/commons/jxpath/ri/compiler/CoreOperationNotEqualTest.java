import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_whenArg1EqualsArg2_thenReturnFalse() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = coreOperationNotEqual.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1NotEqualsArg2_thenReturnTrue() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenArg1IsNull_thenThrowException() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenArg2IsNull_thenThrowException() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationNotEqual_whenArgsAreDifferentTypes_thenThrowException() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression("two");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationNotEqual_whenArgsAreBothNull_thenThrowException() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfNumberAndArg2IsInstanceOfString_thenReturnFalse() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression("two");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = coreOperationNotEqual.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsInstanceOfStringAndArg2IsInstanceOfNumber_thenReturnTrue() {
        Expression arg1 = new Expression("one");
        Expression arg2 = new Expression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.booleanValue());
    }
}