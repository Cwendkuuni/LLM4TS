package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.apache.commons.jxpath.ri.iter.StandardIter;
import org.apache.commons.jxpath.ri.iter.VariableIter;
import org.junit.Test;

/**
 * Test for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructors() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionLiteral(42), new CoreExpressionLiteral(3));
        assertNotNull(exp);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionVariable("x"), new CoreExpressionLiteral(5));
        EvalContext ctx = new EvalContext(new SelfContext(), new InitialContext());
        Node node = new SelfContext();
        ctx.setNode(node);
        ctx.setVariable("x", new SelfContext());
        ctx.pushVariable("x", new Iterate() {
            @Override
            public Iterator iterate() {
                return new ArrayIter(new Object[]{1, 2, 3, 4, 5, 6});
            }
        });
        Object result = exp.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertTrue(Boolean.TRUE.equals(result));
    }

    @Test
    public void testComputeValue_withFalse() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionVariable("x"), new CoreExpressionLiteral(3));
        EvalContext ctx = new EvalContext(new SelfContext(), new InitialContext());
        Node node = new SelfContext();
        ctx.setNode(node);
        ctx.setVariable("x", new SelfContext());
        ctx.pushVariable("x", new Iterate() {
            @Override
            public Iterator iterate() {
                return new ArrayIter(new Object[]{1, 2, 3, 4, 5, 6});
            }
        });
        Object result = exp.computeValue(ctx);
        assertTrue(result instanceof Boolean);
        assertFalse(Boolean.TRUE.equals(result));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionLiteral(42), new CoreExpressionLiteral(3));
        int precedence = exp.getPrecedence();
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrecedence_withNull() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(null, new CoreExpressionLiteral(3));
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionLiteral(42), new CoreExpressionLiteral(3));
        assertFalse(exp.isSymmetric());
    }

    @Test
    public void testEvaluateCompare_equal() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_less() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greater() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_lessEqual() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_lessEqual_equal() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_lessEqual_less() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_lessEqual_greater() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqual() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqual_equal() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqual_less() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqual_greater() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_lessLess() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_lessLess_less() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_lessLess_equal() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_lessLess_greater() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_lessEqualLess() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_lessEqualLess_equal() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_lessEqualLess_less() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_lessEqualLess_greater() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterLess() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterLess_less() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterLess_equal() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterLess_greater() {
        CoreOperationRelationalExpression exp = new CoreOperationLessThan(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqualLess() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqualLess_equal() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(3.0));
        boolean result = exp.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqualLess_less() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(2.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }

    @Test
    public void testEvaluateCompare_greaterEqualLess_greater() {
        CoreOperationRelationalExpression exp = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(3.0), new CoreExpressionLiteral(4.0));
        boolean result = exp.evaluateCompare(0);
        assertFalse(result);
    }
}