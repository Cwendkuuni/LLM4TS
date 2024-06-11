package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.IterateResult;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * JUnit test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        // This method doesn't need testing since it's not a method of this class.
    }

    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreExpressionLiteral(left),
                new CoreExpressionLiteral(right));

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), true);
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new Double(3.5);
        right = new Double(5.0);
        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(left),
                new CoreExpressionLiteral(right));

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new Double(5.0);
        right = new Double(3.5);
        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(left),
                new CoreExpressionLiteral(right));

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new Integer(5);
        right = new Integer(5);
        expression = new CoreOperationGreaterThanOrEqual(new CoreExpressionLiteral(left),
                new CoreExpressionLiteral(right));

        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));

        int precedence = expression.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreExpressionVariable("x"), new CoreExpressionVariable("y"));

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreExpressionLiteral(new Integer(5)), new CoreExpressionLiteral(new Integer(10)));

        boolean result = expression.evaluateCompare(0);

        assertFalse(result);

        result = expression.evaluateCompare(-1);

        assertTrue(result);

        result = expression.evaluateCompare(1);

        assertFalse(result);
    }

    @Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(
                new CoreExpressionLiteral(left), new CoreExpressionLiteral(right));

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), true);

        Object result = expression.compute(left, right);

        assertFalse(result instanceof Boolean);

        left = new Double(3.5);
        right = new Double(5.0);

        expression = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionLiteral(left), new CoreExpressionLiteral(right));

        result = expression.compute(left, right);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new Double(5.0);
        right = new Double(3.5);

        expression = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionLiteral(left), new CoreExpressionLiteral(right));

        result = expression.compute(left, right);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new Integer(5);
        right = new Integer(5);

        expression = new CoreOperationGreaterThanOrEqual(
                new CoreExpressionLiteral(left), new CoreExpressionLiteral(right));

        result = expression.compute(left, right);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testReduce() {
        Object obj = new Integer(5);

        Object result = new CoreOperationRelationalExpression().reduce(obj);

        assertEquals(obj, result);

        obj = new SelfContext();

        result = new CoreOperationRelationalExpression().reduce(obj);

        assertTrue(result instanceof EvalContext);
        assertEquals(obj, ((EvalContext) result).getRoot());

        obj = new HashSet<>();
        obj.add(new Integer(5));

        result = new CoreOperationRelationalExpression().reduce(obj);

        assertTrue(result instanceof Iterator);
        assertEquals(1, ((Iterator) result).nextCount());
        assertEquals(5, ((Integer) ((Iterator) result).next()).intValue());
    }

    @Test
    public void testContainsMatch() {
        Iterator it = Collections.singletonList(new Integer(5)).iterator();
        Object value = new Integer(5);

        boolean result = new CoreOperationRelationalExpression().containsMatch(it, value);

        assertTrue(result);

        it = Collections.singletonList(new Integer(10)).iterator();
        result = new CoreOperationRelationalExpression().containsMatch(it, value);

        assertFalse(result);

        it = Arrays.asList(new Integer(5), new Integer(6), new Integer(7)).iterator();
        result = new CoreOperationRelationalExpression().containsMatch(it, value);

        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = Arrays.asList(new Integer(5), new Integer(6), new Integer(7)).iterator();
        Iterator rit = Arrays.asList(new Integer(5), new Integer(6), new Integer(8)).iterator();

        boolean result = new CoreOperationRelationalExpression().findMatch(lit, rit);

        assertFalse(result);

        lit = Arrays.asList(new Integer(5), new Integer(6), new Integer(7)).iterator();
        rit = Arrays.asList(new Integer(5), new Integer(6), new Integer(5)).iterator();

        result = new CoreOperationRelationalExpression().findMatch(lit, rit);

        assertTrue(result);

        lit = Collections.singletonList(new Integer(5)).iterator();
        rit = Arrays.asList(new Integer(5), new Integer(6), new Integer(7)).iterator();

        result = new CoreOperationRelationalExpression().findMatch(lit, rit);

        assertFalse(result);
    }
}