package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqualOperation() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Boolean result = notEqual.computeValue(context);

        assertTrue(result == Boolean.FALSE);
    }

    @Test
    public void testNotEqualWithSameDataAndValue() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Boolean result = notEqual.computeValue(context);

        assertFalse(result == Boolean.TRUE); // The result should be false, as per the contract of the "!=" operator
    }

    @Test
    public void testNotEqualWithSameDataButDifferentValues() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Boolean result = notEqual.computeValue(context);

        assertTrue(result == Boolean.TRUE); // The result should be true, as per the contract of the "!=" operator
    }

    @Test
    public void testNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullContext() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = null;
        notEqual.computeValue(context);
    }
}