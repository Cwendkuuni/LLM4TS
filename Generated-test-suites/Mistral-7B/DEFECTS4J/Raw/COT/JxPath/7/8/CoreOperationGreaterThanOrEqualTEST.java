import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual_WhenTwoNumbersAreEqual_ShouldReturnTrue() {
        Expression arg1 = new CoreConstantExpression(5.0);
        Expression arg2 = new CoreConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue("Two equal numbers should return true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_WhenFirstNumberIsGreaterThanSecond_ShouldReturnTrue() {
        Expression arg1 = new CoreConstantExpression(5.0);
        Expression arg2 = new CoreConstantExpression(3.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue("First number greater than second should return true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_WhenFirstNumberIsLessThanSecond_ShouldReturnFalse() {
        Expression arg1 = new CoreConstantExpression(3.0);
        Expression arg2 = new CoreConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse("First number less than second should return false", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_WhenArgumentsAreOfDifferentTypes_ShouldThrowClassCastException() {
        Expression arg1 = new CoreConstantExpression(5.0);
        Expression arg2 = new CoreConstantStringExpression("five");
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_WhenFirstArgumentIsNull_ShouldReturnFalse() {
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreConstantExpression(5.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse("Null argument should return false", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_WhenSecondArgumentIsNull_ShouldReturnTrue() {
        Expression arg1 = new CoreConstantExpression(5.0);
        Expression arg2 = new CoreNullExpression();
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue("Non-null argument should be greater than null", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_WhenBothArgumentsAreNaN_ShouldReturnTrue() {
        Expression arg1 = new CoreConstantExpression(Double.NaN);
        Expression arg2 = new CoreConstantExpression(Double.NaN);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertTrue("Two NaN values should be equal", result instanceof Boolean && ((Boolean) result).booleanValue());
    }
}