package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationCompare comparison = new CoreOperationCompare(arg1, arg2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(comparison, new ConstantExpression(false));
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        Object result = notEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithSameValues() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        Object result = notEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNullArg1() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(null, new ConstantExpression(2));
        notEqual.computeValue(context);
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNullArg2() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), null);
        notEqual.computeValue(context);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        EvalContext context = new EvalContext();
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression("2"));
        Object result = notEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}