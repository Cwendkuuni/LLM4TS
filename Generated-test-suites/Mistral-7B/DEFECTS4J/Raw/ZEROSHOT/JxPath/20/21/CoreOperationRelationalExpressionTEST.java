package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.test.AbstractTest;

public class CoreOperationRelationalExpressionTest extends AbstractTest {

    /**
     * Test constructor.
     */
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertNotNull(expr);
    }

    /**
     * Test computeValue method.
     */
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(5),
                new ConstantExpression(10));
        assertFalse(expr.computeValue(new EvalContext(null, new InfoSet())));
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        assertTrue(expr.computeValue(new EvalContext(null, new InfoSet())));
        expr = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(4.5));
        assertTrue(expr.computeValue(new EvalContext(null, new InfoSet())));
        expr = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        assertTrue(expr.computeValue(new EvalContext(null, new InfoSet())));
    }

    /**
     * Test getPrecedence method.
     */
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(5),
                new ConstantExpression(10));
        assertEquals(RELATIONAL_EXPR_PRECEDENCE, expr.getPrecedence());
    }

    /**
     * Test isSymmetric method.
     */
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new ConstantExpression(5),
                new ConstantExpression(10));
        assertFalse(expr.isSymmetric());
    }

    /**
     * Test evaluateCompare method.
     */
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual();
        expr.evaluateCompare(0);
        assertTrue(expr.evaluateCompare(0));
        expr.evaluateCompare(1);
        assertFalse(expr.evaluateCompare(1));
        expr.evaluateCompare(-1);
        assertFalse(expr.evaluateCompare(-1));
    }

    /**
     * Test compute method.
     */
    public void testCompute() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual();
        Object left = new ConstantExpression(5);
        Object right = new ConstantExpression(5);
        assertFalse(expr.compute(left, right));
        left = new ConstantExpression(5.0);
        right = new ConstantExpression(5.0);
        assertTrue(expr.compute(left, right));
        left = new ConstantExpression(new SelfContext());
        right = new ConstantExpression(new SelfContext());
        assertTrue(expr.compute(left, right));

        left = new ConstantExpression(5);
        right = new ConstantExpression(10);
        assertFalse(expr.compute(left, right));
        left = new ConstantExpression(5.0);
        right = new ConstantExpression(4.5);
        assertTrue(expr.compute(left, right));
        left = new ConstantExpression(new InitialContext());
        right = new ConstantExpression(new InitialContext());
        assertTrue(expr.compute(left, right));

        left = new ConstantExpression(Arrays.asList(1, 2, 3));
        right = new ConstantExpression(Arrays.asList(1, 2, 3, 3));
        assertTrue(expr.compute(left, right));
        left = new ConstantExpression(Arrays.asList(1, 2, 3));
        right = new ConstantExpression(Arrays.asList(1, 2, 3, 4));
        assertFalse(expr.compute(left, right));

        left = Arrays.asList(1.0, 2.0, 3.0).iterator();
        right = new ConstantExpression(4.5);
        assertFalse(expr.compute(left, right));
        left = Arrays.asList(1.0, 2.0, 3.0).iterator();
        right = Arrays.asList(4.5, 5.5, 6.5).iterator();
        assertFalse(expr.compute(left, right));

        left = Arrays.asList(1.0, 2.0, 3.0).iterator();
        right = Arrays.asList(3.0, 2.0, 1.0).iterator();
        assertTrue(expr.compute(left, right));
    }

    /**
     * Test reduce method.
     */
    public void testReduce() {
        Object o = new ConstantExpression(10);
        Object reduced = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(
                new CoreOperationNot(new ConstantExpression(new SelfContext())), o))
                .reduce(o);
        assertSame(new SelfContext(), reduced);

        o = Arrays.asList(1, 2, 3).iterator();
        reduced = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(
                new CoreOperationNot(new ConstantExpression(new InitialContext())), o))
                .reduce(o);
        assertTrue(o instanceof Iterator);
        assertEquals(1, ((Iterator) o).next().intValue());

        o = Arrays.asList(1.0, 2.0, 3.0).iterator();
        reduced = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(
                new CoreOperationNot(new ConstantExpression(new InfoSet())), o))
                .reduce(o);
        assertTrue(o instanceof Iterator);
        assertEquals(1.0, InfoSetUtil.doubleValue(((Iterator) o).next()), 0);
    }

    /**
     * Test containsMatch method.
     */
    public void testContainsMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual();
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        Object value = new ConstantExpression(2);
        assertTrue(expr.containsMatch(it, value));
        it = Arrays.asList(1, 2, 3).iterator();
        value = new ConstantExpression(4);
        assertFalse(expr.containsMatch(it, value));
        it = Arrays.asList(1.0, 2.0, 3.0).iterator();
        value = new ConstantExpression(2.5);
        assertTrue(expr.containsMatch(it, value));
        it = Arrays.asList(1.0, 2.0, 3.0).iterator();
        value = new ConstantExpression(4.5);
        assertFalse(expr.containsMatch(it, value));
    }

    /**
     * Test findMatch method.
     */
    public void testFindMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThanOrEqual();
        Iterator lit = Arrays.asList(1, 2).iterator();
        Iterator rit = Arrays.asList(1, 2, 3).iterator();
        assertTrue(expr.findMatch(lit, rit));
        lit = Arrays.asList(1, 2).iterator();
        rit = Arrays.asList(1, 2, 4).iterator();
        assertFalse(expr.findMatch(lit, rit));
        lit = Arrays.asList(1.0, 2.0).iterator();
        rit = Arrays.asList(1.0, 2.0, 3.0).iterator();
        assertTrue(expr.findMatch(lit, rit));
        lit = Arrays.asList(1.0, 2.0).iterator();
        rit = Arrays.asList(1.0, 2.0, 4.0).iterator();
        assertFalse(expr.findMatch(lit, rit));
    }

    private static final int RELATIONAL_EXPR_PRECEDENCE = 2;

}