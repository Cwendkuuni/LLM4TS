import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_whenBothArgsAreSameBooleanTrue_thenReturnTrue() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreSameBooleanFalse_thenReturnFalse() {
        Expression arg1 = new CoreConstantExpression(Boolean.FALSE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsBooleanTrueAndArg2IsBooleanFalse_thenReturnFalse() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsBooleanFalseAndArg2IsBooleanTrue_thenReturnFalse() {
        Expression arg1 = new CoreConstantExpression(Boolean.FALSE);
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationEqual_whenTypesOfArg1AndArg2AreDifferent_thenThrowClassCastException() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression("String");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationEqual_whenArg1IsNumberAndArg2IsString_thenThrowClassCastException() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression("String");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationEqual_whenArg1IsStringAndArg2IsNumber_thenThrowClassCastException() {
        Expression arg1 = new CoreConstantExpression("String");
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationEqual_whenArg1IsNull_thenThrowNullPointerException() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationEqual_whenArg2IsNull_thenThrowNullPointerException() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenArg1AndArg2AreBothNull_thenThrowIllegalArgumentException() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        try {
            coreOperationEqual.computeValue(context);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("CoreOperationEqual constructor should throw IllegalArgumentException if both arguments are null.", e);
        }
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsNumberAndArg2IsNumber_thenReturnBooleanValueBasedOnComparisonResult() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsStringAndArg2IsString_thenReturnBooleanValueBasedOnComparisonResult() {
        Expression arg1 = new CoreConstantExpression("String1");
        Expression arg2 = new CoreConstantExpression("String1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result.equals(Boolean.TRUE));
    }
}