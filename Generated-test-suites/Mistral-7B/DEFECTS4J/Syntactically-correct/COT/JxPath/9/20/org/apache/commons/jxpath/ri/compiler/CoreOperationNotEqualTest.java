import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_whenArg1IsSameAsArg2_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse(result.toString(), result);
    }

    @Test
    public void testComputeValue_whenArg1IsNotSameAsArg2_thenReturnTrue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.toString(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenArg1IsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenArg2IsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test
    public void testComputeValue_whenArg1IsNumberAndArg2IsString_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new StringConstantExpression("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse(result.toString(), result);
    }

    @Test
    public void testComputeValue_whenArg1IsStringAndArg2IsNumber_thenReturnTrue() {
        Expression arg1 = new StringConstantExpression("1");
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.toString(), result);
    }

    @Test
    public void testComputeValue_whenArg1IsBooleanTrueAndArg2IsBooleanFalse_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse(result.toString(), result);
    }

    @Test
    public void testComputeValue_whenArg1IsBooleanFalseAndArg2IsBooleanTrue_thenReturnTrue() {
        Expression arg1 = new ConstantExpression(Boolean.FALSE);
        Expression arg2 = new ConstantExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.toString(), result);
    }

    @Test
    public void testComputeValue_whenArg1IsBooleanTrueAndArg2IsBooleanTrue_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse(result.toString(), result);
    }

    @Test
    public void testComputeValue_whenArg1IsBooleanFalseAndArg2IsBooleanFalse_thenReturnTrue() {
        Expression arg1 = new ConstantExpression(Boolean.FALSE);
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result.toString(), result);
    }
}