import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual_Double_Double_ReturnsTrue() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_Double_Double_ReturnsFalse() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_Double_Double_EdgeCase1() {
        Expression arg1 = new LiteralExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new LiteralExpression(Double.POSITIVE_INFINITY);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_Double_Double_EdgeCase2() {
        Expression arg1 = new LiteralExpression(Double.NEGATIVE_INFINITY);
        Expression arg2 = new LiteralExpression(Double.NEGATIVE_INFINITY);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_NonNumeric_NonNumeric() {
        Expression arg1 = new LiteralExpression("stringValue");
        Expression arg2 = new LiteralExpression("anotherStringValue");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        operation.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_NonNumeric_Numeric() {
        Expression arg1 = new LiteralExpression("stringValue");
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        operation.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_Numeric_NonNumeric() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression("stringValue");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqual_NullArgument() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqual_NullArgument_Handled() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }
}