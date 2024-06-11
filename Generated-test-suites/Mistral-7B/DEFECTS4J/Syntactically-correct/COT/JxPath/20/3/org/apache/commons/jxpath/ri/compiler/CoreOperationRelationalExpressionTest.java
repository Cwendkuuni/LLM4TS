package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreConstantExpression(3));
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThan(new CoreConstantExpression(3), new CoreConstantExpression(5));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(5), new CoreConstantExpression(3));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(3), new CoreConstantExpression(5));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(5), new CoreConstantExpression(3));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationLessThan(new CoreConstantExpression(3), new CoreConstantExpression(5));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(5), new CoreConstantExpression(3));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(3), new CoreConstantExpression(5));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(1));
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(1));
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompareNullCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(1));
        expression.evaluateCompare(null);
    }

    @Test
    public void testComputeWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreConstantExpression(3));
        EvalContext context = new EvalContext();
        context.setInitialContext(new InitialContext());
        context.setCurrentNode(new SelfContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithCollections() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreConstantExpression(3));
        EvalContext context = new EvalContext();
        Collection<Object> left = new HashSet<>();
        left.add(new CoreConstantExpression(5));
        context.setCurrentNode(new InitialContext(left.iterator()));
        Collection<Object> right = new HashSet<>();
        right.add(new CoreConstantExpression(3));
        context.setInitialContext(new InitialContext(right.iterator()));

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeWithDoubleValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5.1), new CoreConstantExpression(3.1));
        EvalContext context = new EvalContext();
        context.setCurrentNode(new SelfContext());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(5.1), new CoreConstantExpression(3.1));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThan(new CoreConstantExpression(3.1), new CoreConstantExpression(5.1));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(3.1), new CoreConstantExpression(5.1));
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEvaluateCompareComparisonResult() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreConstantExpression(3));
        int compare = InfoSetUtil.compare(InfoSetUtil.doubleValue(expression.args[0].compute(new EvalContext())),
                InfoSetUtil.doubleValue(expression.args[1].compute(new EvalContext())));
        assertTrue(expression.evaluateCompare(compare) == true);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(5), new CoreConstantExpression(3));
        compare = InfoSetUtil.compare(InfoSetUtil.doubleValue(expression.args[0].compute(new EvalContext())),
                InfoSetUtil.doubleValue(expression.args[1].compute(new EvalContext())));
        assertTrue(expression.evaluateCompare(compare) == true);

        expression = new CoreOperationGreaterThan(new CoreConstantExpression(3), new CoreConstantExpression(5));
        compare = InfoSetUtil.compare(InfoSetUtil.doubleValue(expression.args[0].compute(new EvalContext())),
                InfoSetUtil.doubleValue(expression.args[1].compute(new EvalContext())));
        assertTrue(expression.evaluateCompare(compare) == false);

        expression = new CoreOperationGreaterThanOrEqual(new CoreConstantExpression(3), new CoreConstantExpression(5));
        compare = InfoSetUtil.compare(InfoSetUtil.doubleValue(expression.args[0].compute(new EvalContext())),
                InfoSetUtil.doubleValue(expression.args[1].compute(new EvalContext())));
        assertTrue(expression.evaluateCompare(compare) == false);
    }

    @Test
    public void testFindMatchWithIterators() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(new CoreConstantExpression(5), new CoreConstantExpression(3));
        Iterator<Object> left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreConstantExpression(4);
            }
        };
        Iterator<Object> right = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreConstantExpression(5);
            }
        };

        boolean result = expression.findMatch(left, right);
        assertFalse(result);

        left = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreConstantExpression(3);
            }
        };
        result = expression.findMatch(left, right);
        assertTrue(result);
    }

    @Test
    public void testContainsMatchWithIterators() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new CoreConstantExpression(5), new CoreConstantExpression(3));
        Iterator<Object> iter = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreConstantExpression(4);
            }
        };

        boolean result = expression.containsMatch(iter, new CoreConstantExpression(3));
        assertFalse(result);

        iter = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreConstantExpression(5);
            }
        };
        result = expression.containsMatch(iter, new CoreConstantExpression(3));
        assertFalse(result);

        iter = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreConstantExpression(4);
            }
        };
        result = expression.containsMatch(iter, new CoreConstantExpression(5));
        assertFalse(result);

        iter = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreConstantExpression(3);
            }
        };
        result = expression.containsMatch(iter, new CoreConstantExpression(5));
        assertTrue(result);
    }
}