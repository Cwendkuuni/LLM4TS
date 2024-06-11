import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.expressions.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        Boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithInvalidInput() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double("invalid")),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        try {
            expression.computeValue(context);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNotNumber() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new String("5")),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        try {
            expression.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithUnequalValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(3.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        Boolean result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithEqualValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        Boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(-3.0)),
                new ConstantExpression(new Double(-2.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        Boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZero() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(0.0)),
                new ConstantExpression(new Double(5.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        Boolean result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithLargeValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(Double.MAX_VALUE)),
                new ConstantExpression(new Double(Double.MAX_VALUE + 1.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        Boolean result = (Boolean) expression.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeLargeValues() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(-Double.MAX_VALUE)),
                new ConstantExpression(new Double(-Double.MAX_VALUE + 1.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(InfoSet.EMPTY_INFOSET);

        Boolean result = (Boolean) expression.computeValue(context);
        assertTrue(result);
    }
}