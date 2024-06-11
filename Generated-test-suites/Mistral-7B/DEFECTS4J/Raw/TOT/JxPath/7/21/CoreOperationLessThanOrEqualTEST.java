import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.NumberInfo;
import org.apache.commons.jxpath.ri.info.StringInfo;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithNumbers() {
        Expression arg1 = new ConstantExpression(new NumberInfo(1.0));
        Expression arg2 = new ConstantExpression(new NumberInfo(2.0));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg2 = new ConstantExpression(new NumberInfo(1.0));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new ConstantExpression(new NumberInfo(2.0));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithStrings() {
        Expression arg1 = new ConstantExpression(new StringInfo("A"));
        Expression arg2 = new ConstantExpression(new StringInfo("B"));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        Object result = operation.computeValue(context);
        assertNull(result);

        arg1 = new ConstantExpression(new StringInfo("A"));
        arg2 = new ConstantExpression(new StringInfo("Z"));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new ConstantExpression(new StringInfo("Z"));
        arg2 = new ConstantExpression(new StringInfo("A"));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new ConstantExpression(new StringInfo("A"));
        arg2 = new ConstantExpression(new StringInfo("A"));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testLessThanOrEqualWithMixedTypes() {
        Expression arg1 = new ConstantExpression(new NumberInfo(1.0));
        Expression arg2 = new ConstantExpression(new StringInfo("2"));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithNullArg() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(new NumberInfo(1.0));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        operation.computeValue(context);
    }
}