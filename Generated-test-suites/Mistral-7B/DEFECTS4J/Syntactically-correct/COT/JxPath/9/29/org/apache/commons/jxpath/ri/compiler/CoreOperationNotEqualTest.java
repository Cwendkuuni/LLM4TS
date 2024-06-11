import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_whenBothArgsAreEqual_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testCoreOperationNotEqual_whenFirstArgIsNotEqualSecondArg_thenReturnTrue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);
        assertTrue(result.booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenOneArgIsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new NullExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_whenBothArgsAreNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new NullExpression();
        Expression arg2 = new NullExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationNotEqual_whenFirstArgTypeMismatchWithSecondArg_thenThrowRuntimeException() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new StringExpression("test");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationNotEqual_whenSecondArgTypeMismatchWithFirstArg_thenThrowRuntimeException() {
        Expression arg1 = new StringExpression("test");
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationNotEqual.computeValue(context);
    }

    @Test
    public void testCoreOperationNotEqual_whenFirstArgIsInstanceOfCoreOperationNotEqual_thenReturnFalse() {
        Expression arg1 = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        Expression arg2 = new CoreOperationNotEqual(new ConstantExpression(2), new ConstantExpression(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testCoreOperationNotEqual_whenFirstArgIsInstanceOfCoreOperationCompare_thenReturnFalse() {
        Expression arg1 = new CoreOperationCompare(new ConstantExpression(1), ComparisonOperator.EQ, new ConstantExpression(1));
        Expression arg2 = new CoreOperationNotEqual(new ConstantExpression(2), new ConstantExpression(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testCoreOperationNotEqual_whenSecondArgIsInstanceOfCoreOperationNotEqual_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new CoreOperationNotEqual(new ConstantExpression(2), new ConstantExpression(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testCoreOperationNotEqual_whenSecondArgIsInstanceOfCoreOperationCompare_thenReturnFalse() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new CoreOperationCompare(new ConstantExpression(2), ComparisonOperator.EQ, new ConstantExpression(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationNotEqual.computeValue(context);
        assertFalse(result.booleanValue());
    }

}