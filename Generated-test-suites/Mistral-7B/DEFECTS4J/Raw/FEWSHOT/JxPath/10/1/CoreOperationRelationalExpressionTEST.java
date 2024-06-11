package org.apache.commons.jxpath.ri.compiler;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThan(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(2), new CoreConstantExpression(1));
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null, null);
        int precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);

        expression = new CoreOperationGreaterThanOrEqual(null, null);
        precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);

        expression = new CoreOperationLessThan(null, null);
        precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);

        expression = new CoreOperationLessThanOrEqual(null, null);
        precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(null, null);
        boolean symmetric = expression.isSymmetric();
        Assert.assertEquals(false, symmetric);

        expression = new CoreOperationGreaterThanOrEqual(null, null);
        symmetric = expression.isSymmetric();
        Assert.assertEquals(false, symmetric);

        expression = new CoreOperationLessThan(null, null);
        symmetric = expression.isSymmetric();
        Assert.assertEquals(false, symmetric);

        expression = new CoreOperationLessThanOrEqual(null, null);
        symmetric = expression.isSymmetric();
        Assert.assertEquals(false, symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(null, null);
        boolean result = expression.evaluateCompare(0);
        Assert.assertEquals(false, result);

        result = expression.evaluateCompare(1);
        Assert.assertEquals(true, result);

        result = expression.evaluateCompare(-1);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        Object left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        Object right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        boolean result = expression.compute(left, right);
        Assert.assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThan(new CoreConstantExpression(2), new CoreConstantExpression(1));
        left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        result = expression.compute(left, right);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        result = expression.compute(left, right);
        Assert.assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(2), new CoreConstantExpression(1));
        left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        result = expression.compute(left, right);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(1), new CoreConstantExpression(2));
        left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        result = expression.compute(left, right);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(2), new CoreConstantExpression(1));
        left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        result = expression.compute(left, right);
        Assert.assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        result = expression.compute(left, right);
        Assert.assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(2), new CoreConstantExpression(1));
        left = expression.args[0].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        right = expression.args[1].computeValue(new EvalContext(new InitialContext(), new SelfContext()));
        result = expression.compute(left, right);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testReduce() {
        Object value = new CoreConstantExpression(1);
        Object result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreConstantExpression(1), value)).reduce(value);
        Assert.assertEquals(value, result);

        value = new CoreConstantExpression(new InitialContext());
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreConstantExpression(1), value)).reduce(value);
        Assert.assertNotNull(result);

        value = new CoreConstantExpression(new HashSet<>());
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreConstantExpression(1), value)).reduce(value);
        Assert.assertNotNull(result);

        value = new CoreConstantExpression(new Iterator[] {new Object() {}});
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new CoreConstantExpression(1), value)).reduce(value);
        Assert.assertNotNull(result);
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = new Object() {}.iterator();
        boolean result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(iterator, new CoreConstantExpression(1))).containsMatch(iterator, new CoreConstantExpression(1));
        Assert.assertFalse(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(iterator, new CoreConstantExpression(2))).containsMatch(iterator, new CoreConstantExpression(2));
        Assert.assertTrue(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(iterator, new CoreConstantExpression(3))).containsMatch(iterator, new CoreConstantExpression(2));
        Assert.assertFalse(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThanOrEqual(iterator, new CoreConstantExpression(1))).containsMatch(iterator, new CoreConstantExpression(1));
        Assert.assertTrue(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThanOrEqual(iterator, new CoreConstantExpression(2))).containsMatch(iterator, new CoreConstantExpression(2));
        Assert.assertTrue(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThanOrEqual(iterator, new CoreConstantExpression(3))).containsMatch(iterator, new CoreConstantExpression(3));
        Assert.assertTrue(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThanOrEqual(iterator, new CoreConstantExpression(2))).containsMatch(iterator, new CoreConstantExpression(1));
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Object() {}.iterator();
        Iterator rit = new Object() {}.iterator();
        HashSet left = new HashSet();
        while (lit.hasNext()) {
            left.add(lit.next());
        }
        boolean result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(lit, rit)).findMatch(lit, rit);
        Assert.assertFalse(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(lit, lit)).findMatch(lit, rit);
        Assert.assertFalse(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThanOrEqual(lit, rit)).findMatch(lit, rit);
        Assert.assertFalse(result);

        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThanOrEqual(lit, lit)).findMatch(lit, rit);
        Assert.assertTrue(result);
    }
}