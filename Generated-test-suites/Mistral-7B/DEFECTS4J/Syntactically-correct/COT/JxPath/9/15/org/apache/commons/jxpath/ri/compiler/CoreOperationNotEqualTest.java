import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_whenArg1IsNotNullAndArg2IsNotNull_thenResultIsExpected() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsNullAndArg2IsNotNull_thenResultIsExpected() {
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsNotNullAndArg2IsNull_thenResultIsExpected() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenBothArgIsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = null;
        Expression arg2 = null;
        new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqual_whenBothArgAreSameInstance_thenResultIsExpected() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsNumberAndArg2IsNumber_thenResultIsExpected() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsStringAndArg2IsString_thenResultIsExpected() {
        Expression arg1 = new ConstantExpression("test1");
        Expression arg2 = new ConstantExpression("test2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsNumberAndArg2IsString_thenResultIsExpected() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression("test");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_whenArg1IsStringAndArg2IsNumber_thenResultIsExpected() {
        Expression arg1 = new ConstantExpression("test");
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }
}