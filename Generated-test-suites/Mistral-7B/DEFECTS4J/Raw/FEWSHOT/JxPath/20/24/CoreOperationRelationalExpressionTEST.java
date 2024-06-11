package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Node;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValueGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(5), new LiteralExpression(3));
        assertTrue(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new LiteralExpression(3), new LiteralExpression(5));
        assertFalse(expression.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new LiteralExpression(5), new LiteralExpression(3));
        assertTrue(expression.computeValue(new EvalContext()));
        assertTrue(expression.computeValue(new EvalContext(new Iterate(new Node("5"), new Node("3")))));
        assertTrue(expression.computeValue(new EvalContext(new Iterate(new Node("3"), new Node("5")))));
        assertFalse(expression.computeValue(new EvalContext(new Iterate(new Node("3"), new Node("4")))));
    }

    @Test
    public void testComputeValueLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new LiteralExpression(3), new LiteralExpression(5));
        assertFalse(expression.computeValue(new EvalContext()));
        assertTrue(expression.computeValue(new EvalContext(new Iterate(new Node("3"), new Node("3")))));
        assertTrue(expression.computeValue(new EvalContext(new Iterate(new Node("5"), new Node("5")))));
        assertFalse(expression.computeValue(new EvalContext(new Iterate(new Node("3"), new Node("4")))));
        assertFalse(expression.computeValue(new EvalContext(new Iterate(new Node("5"), new Node("3")))));
    }

    @Test
    public void testEvaluateCompareGreaterThan() {
        assertTrue(new CoreOperationRelationalExpression().evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompareLessThan() {
        assertFalse(new CoreOperationRelationalExpression().evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompareGreaterThanOrEqual() {
        assertTrue(new CoreOperationRelationalExpression().evaluateCompare(0));
    }

    @Test
    public void testEvaluateCompareLessThanOrEqual() {
        assertFalse(new CoreOperationRelationalExpression().evaluateCompare(-1));
    }
}