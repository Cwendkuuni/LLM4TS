import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.CoreOperationAdd;
import org.apache.commons.jxpath.ri.CoreOperationConstant;
import org.apache.commons.jxpath.ri.CoreOperationNegate;
import org.apache.commons.jxpath.ri.core.Constants;

public class CoreOperationLessThanTest {

    // Test case for method computeValue with positive numbers
    @Test
    public void testComputeValuePositive() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    // Test case for method computeValue with negative numbers
    @Test
    public void testComputeValueNegative() {
        Expression arg1 = new ConstantExpression(-1.0);
        Expression arg2 = new ConstantExpression(-2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    // Test case for method computeValue with zero comparison
    @Test
    public void testComputeValueZero() {
        Expression arg1 = new ConstantExpression(0.0);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    // Test case for method computeValue with NaN comparison
    @Test
    public void testComputeValueNaN() {
        Expression arg1 = new ConstantExpression(Double.NaN);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        try {
            coreOperationLessThan.computeValue(context);
            fail("Expected IllegalStateException for NaN comparison");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    // Test case for method computeValue with different data types
    @Test(expected = ClassCastException.class)
    public void testComputeValueDifferentDataTypes() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        coreOperationLessThan.computeValue(context);
    }

    // Test case for method computeValue with empty InfoSet
    @Test
    public void testComputeValueEmptyInfoSet() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new CoreOperationConstant(Constants.EMPTY_NODE_KEY);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        try {
            coreOperationLessThan.computeValue(context);
            fail("Expected IllegalStateException for empty InfoSet");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}