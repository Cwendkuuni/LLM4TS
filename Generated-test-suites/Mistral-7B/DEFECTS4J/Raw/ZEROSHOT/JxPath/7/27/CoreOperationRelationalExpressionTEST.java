package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new CoreOperationTestDouble("1");
        Expression arg2 = new CoreOperationTestDouble("2");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});
        assertEquals("expected CoreOperationRelationalExpression instance", CoreOperationRelationalExpression.class, expression.getClass());
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] {new CoreOperationTestDouble("1"), new CoreOperationTestDouble("2")});
        int precedence = expression.getPrecedence();
        assertEquals("expected precedence value", 3, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperation[] {new CoreOperationTestDouble("1"), new CoreOperationTestDouble("2")});
        assertFalse("expected false", expression.isSymmetric());
    }

    /**
     * Test CoreOperationGreaterThan method.
     */
    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new CoreOperationTestDouble("3");
        Expression arg2 = new CoreOperationTestDouble("2");
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});

        JXPathContext context = new JXPathContext();
        Object result = expression.compile(context).evaluate(context);

        assertTrue("expected true", result instanceof Boolean);
        assertTrue("expected true", ((Boolean)result).booleanValue());

        arg2 = new CoreOperationTestDouble("3");
        result = expression.compile(context).evaluate(context);

        assertFalse("expected false", result instanceof Boolean);
        assertFalse("expected false", ((Boolean)result).booleanValue());

        arg1 = new CoreOperationTestDouble("2");
        arg2 = new CoreOperationTestDouble("3");
        expression = new CoreOperationGreaterThan(new CoreOperation[] {arg1, arg2});

        result = expression.compile(context).evaluate(context);

        assertFalse("expected false", result instanceof Boolean);
        assertFalse("expected false", ((Boolean)result).booleanValue());
    }

    /**
     * Test CoreOperationGreaterThanOrEqual method.
     */
    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new CoreOperationTestDouble("3");
        Expression arg2 = new CoreOperationTestDouble("2");
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] {arg1, arg2});

        JXPathContext context = new JXPathContext();
        Object result = expression.compile(context).evaluate(context);

        assertTrue("expected true", result instanceof Boolean);
        assertTrue("expected true", ((Boolean)result).booleanValue());

        arg2 = new CoreOperationTestDouble("3");
        result = expression.compile(context).evaluate(context);

        assertTrue("expected true", result instanceof Boolean);
        assertTrue("expected true", ((Boolean)result).booleanValue());

        arg1 = new CoreOperationTestDouble("2");
        arg2 = new CoreOperationTestDouble("3");
        expression = new CoreOperationGreaterThanOrEqual(new CoreOperation[] {arg1, arg2});

        result = expression.compile(context).evaluate(context);

        assertFalse("expected false", result instanceof Boolean);
        assertFalse("expected false", ((Boolean)result).booleanValue());
    }

    /**
     * Test CoreOperationLessThan method.
     */
    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new CoreOperationTestDouble("1");
        Expression arg2 = new CoreOperationTestDouble("2");
        CoreOperationLessThan expression = new CoreOperationLessThan(new CoreOperation[] {arg1, arg2});

        JXPathContext context = new JXPathContext();
        Object result = expression.compile(context).evaluate(context);

        assertTrue("expected true", result instanceof Boolean);
        assertTrue("expected true", ((Boolean)result).booleanValue());

        arg2 = new CoreOperationTestDouble("1");
        result = expression.compile(context).evaluate(context);

        assertFalse("expected false", result instanceof Boolean);
        assertFalse("expected false", ((Boolean)result).booleanValue());

        arg1 = new CoreOperationTestDouble("2");
        arg2 = new CoreOperationTestDouble("1");
        expression = new CoreOperationLessThan(new CoreOperation[] {arg1, arg2});

        result = expression.compile(context).evaluate(context);

        assertTrue("expected true", result instanceof Boolean);
        assertTrue("expected true", ((Boolean)result).booleanValue());
    }

    /**
     * Test CoreOperationLessThanOrEqual method.
     */
    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new CoreOperationTestDouble("1");
        Expression arg2 = new CoreOperationTestDouble("2");
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new CoreOperation[] {arg1, arg2});

        JXPathContext context = new JXPathContext();
        Object result = expression.compile(context).evaluate(context);

        assertFalse("expected false", result instanceof Boolean);
        assertFalse("expected false", ((Boolean)result).booleanValue());

        arg2 = new CoreOperationTestDouble("1");
        result = expression.compile(context).evaluate(context);

        assertTrue("expected true", result instanceof Boolean);
        assertTrue("expected true", ((Boolean)result).booleanValue());

        arg1 = new CoreOperationTestDouble("2");
        arg2 = new CoreOperationTestDouble("1");
        expression = new CoreOperationLessThanOrEqual(new CoreOperation[] {arg1, arg2});

        result = expression.compile(context).evaluate(context);

        assertTrue("expected true", result instanceof Boolean);
        assertTrue("expected true", ((Boolean)result).booleanValue());
    }

}