package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGreaterThanOrEqual;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest {

    private InitialContext initialContext;
    private SelfContext selfContext;

    @Before
    public void setUp() {
        initialContext = new InitialContext();
        selfContext = new SelfContext();
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     */
    @Test
    public void testCoreOperationRelationalExpression() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNull(expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}
     */
    @Test
    public void testComputeValue() {
        Expression left = new CoreOperationConstantExpression(new Double(5.0));
        Expression right = new CoreOperationConstantExpression(new Double(10.0));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperationRelationalExpression(new CoreOperation[] { left, right }));

        EvalContext context = new EvalContext(initialContext, selfContext);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new CoreOperationConstantExpression(new Double(10.0));
        right = new CoreOperationConstantExpression(new Double(5.0));
        expression = new CoreOperationLessThan(new CoreOperationRelationalExpression(new CoreOperation[] { left, right }));

        context = new EvalContext(initialContext, selfContext);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new CoreOperationRelationalExpression(new CoreOperation[] {}));
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(new CoreOperationRelationalExpression(new CoreOperation[] {}));
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}
     */
    @Test(expected = AssertionError.class)
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[] {});
        expression.evaluateCompare(0);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}
     */
    @Test
    public void testCompute() {
        Expression left = new CoreOperationConstantExpression(new Double(5.0));
        Expression right = new CoreOperationConstantExpression(new Double(10.0));
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(new CoreOperationRelationalExpression(new CoreOperation[] { left, right }));

        Object leftReduced = expression.reduce(left);
        Object rightReduced = expression.reduce(right);
        boolean result = expression.compute(leftReduced, rightReduced);
        assertFalse(result);

        left = new CoreOperationConstantExpression(new Double(10.0));
        right = new CoreOperationConstantExpression(new Double(5.0));
        expression = new CoreOperationLessThan(new CoreOperationRelationalExpression(new CoreOperation[] { left, right }));

        leftReduced = expression.reduce(left);
        rightReduced = expression.reduce(right);
        result = expression.compute(leftReduced, rightReduced);
        assertTrue(result);

        left = new CoreOperationConstantExpression(new Double(5.0));
        right = new CoreOperationConstantExpression(new Double(5.0));
        expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpression(new CoreOperation[] { left, right }));

        leftReduced = expression.reduce(left);
        rightReduced = expression.reduce(right);
        result = expression.compute(leftReduced, rightReduced);
        assertFalse(result);

        left = new CoreOperationConstantExpression(new Double(5.0));
        right = new CoreOperationConstantExpression(new Double(6.0));
        expression = new CoreOperationGreaterThan(new CoreOperationRelationalExpression(new CoreOperation[] { left, right }));

        leftReduced = expression.reduce(left);
        rightReduced = expression.reduce(right);
        result = expression.compute(leftReduced, rightReduced);
        assertTrue(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)}
     */
    @Test
    public void testReduce() {
        Expression expression = new CoreOperationConstantExpression(new Double(5.0));
        Object reduced = expression.reduce(expression);
        assertTrue(reduced instanceof Number);
        assertEquals(5.0, ((Number) reduced).doubleValue(), 0.001);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)}
     */
    @Test
    public void testContainsMatch() {
        Collection collection = Arrays.asList(new Object[] { 5, 6, 7, 8, 9 });
        Iterator iterator = collection.iterator();

        Object value = new Integer(6);

        boolean result = expression.containsMatch(iterator, value);
        assertFalse(result);

        value = new Integer(5);

        iterator = collection.iterator();
        result = expression.containsMatch(iterator, value);
        assertTrue(result);

        value = new Integer(8);

        iterator = collection.iterator();
        result = expression.containsMatch(iterator, value);
        assertFalse(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)}
     */
    @Test
    public void testFindMatch() {
        Collection collection1 = Arrays.asList(new Object[] { 5, 6, 7, 8, 9 });
        Iterator iterator1 = collection1.iterator();

        Collection collection2 = Arrays.asList(new Object[] { 5, 6, 7, 8, 9 });
        Iterator iterator2 = collection2.iterator();

        boolean result = expression.findMatch(iterator1, iterator2);
        assertFalse(result);

        boolean result2 = expression.findMatch(iterator2, iterator1);
        assertFalse(result2);

        iterator1 = collection1.iterator();
        iterator2 = Arrays.asList(new Object[] { 5 }).iterator();

        result = expression.findMatch(iterator1, iterator2);
        assertFalse(result);

        iterator2 = collection1.iterator();
        iterator1 = Arrays.asList(new Object[] { 5 }).iterator();

        result = expression.findMatch(iterator1, iterator2);
        assertTrue(result);
    }
}