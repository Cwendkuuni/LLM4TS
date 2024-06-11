import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    @Test
    public void testEqualTrue() {
        Expression arg1 = ExpressionUtil.string("5");
        Expression arg2 = ExpressionUtil.string("5");
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testEqualFalse() {
        Expression arg1 = ExpressionUtil.number(5);
        Expression arg2 = ExpressionUtil.number(6);
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testEqualNull() {
        Expression arg1 = ExpressionUtil.number(5);
        Expression arg2 = ExpressionUtil.nullExpression();
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertNull(result);
    }

    @Test
    public void testEqualMixedTypes() {
        Expression arg1 = ExpressionUtil.number(5);
        Expression arg2 = ExpressionUtil.string("5");
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testEqualDifferentTypes() {
        Expression arg1 = ExpressionUtil.number(5);
        Expression arg2 = ExpressionUtil.string("six");
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
    }

    @Test
    public void testEqualSingleNull() {
        Expression arg1 = ExpressionUtil.nullExpression();
        Expression arg2 = ExpressionUtil.number(5);
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertNull(result);
    }

    @Test
    public void testEqualBothNull() {
        Expression arg1 = ExpressionUtil.nullExpression();
        Expression arg2 = ExpressionUtil.nullExpression();
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertNull(result);
    }

    @Test
    public void testEqualWithParenthesis() {
        Expression arg1 = ExpressionUtil.number(5);
        Expression arg2 = ExpressionUtil.string("(5)");
        CoreOperationEqual opEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testEqualWithCompareOperator() {
        Expression arg1 = ExpressionUtil.number(5);
        Expression arg2 = ExpressionUtil.number(6);
        CoreOperationCompare opCompare = new CoreOperationCompare(CoreOperationCompare.OP_NE, arg1, arg2);
        CoreOperationEqual opEqual = new CoreOperationEqual(ExpressionUtil.number(5), opCompare);
        EvalContext ctx = new EvalContext();
        Object result = opEqual.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }
}