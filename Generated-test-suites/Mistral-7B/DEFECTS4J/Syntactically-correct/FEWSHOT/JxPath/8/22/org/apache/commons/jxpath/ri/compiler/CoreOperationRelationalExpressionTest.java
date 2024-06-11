package org.apache.commons.jxpath.ri.compiler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new VariableExpression("x"), new NumericLiteralExpression(2.0f));
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThan(new VariableExpression("x"), new NumericLiteralExpression(3.0f));
        context = new EvalContext(new InitialContext(), new SelfContext());
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.FALSE));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new VariableExpression("x"), new NumericLiteralExpression(2.0f));
        context = new EvalContext(new InitialContext(), new SelfContext());
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new VariableExpression("x"), new NumericLiteralExpression(1.5f));
        context = new EvalContext(new InitialContext(), new SelfContext());
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new VariableExpression("x"), new NumericLiteralExpression(2.0f));
        context = new EvalContext(new InitialContext(), new SelfContext());
        result = expression.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new NumericLiteralExpression(2.0f));
        context = new EvalContext(new InitialContext(), new SelfContext());
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new VariableExpression("x"), new NumericLiteralExpression(2.0f));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationGreaterThanOrEqual(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationLessThan(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expression.isSymmetric());

        expression = new CoreOperationLessThanOrEqual(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(2));
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));

        expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        assertFalse(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(-1));
        assertTrue(expression.evaluateCompare(-2));

        expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(-0));
        assertFalse(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(3.0f));
        assertFalse(expression.compute(null, null));

        expression = new CoreOperationGreaterThan(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(2.0f));
        assertFalse(expression.compute(null, null));

        expression = new CoreOperationGreaterThan(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(1.5f));
        assertTrue(expression.compute(null, null));

        expression = new CoreOperationGreaterThanOrEqual(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(2.0f));
        assertTrue(expression.compute(null, null));

        expression = new CoreOperationGreaterThanOrEqual(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(1.5f));
        assertTrue(expression.compute(null, null));

        expression = new CoreOperationLessThan(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(3.0f));
        assertFalse(expression.compute(null, null));

        expression = new CoreOperationLessThanOrEqual(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(3.0f));
        assertFalse(expression.compute(null, null));

        expression = new CoreOperationLessThanOrEqual(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(2.0f));
        assertTrue(expression.compute(null, null));

        expression = new CoreOperationLessThanOrEqual(new NumericLiteralExpression(2.0f), new NumericLiteralExpression(1.5f));
        assertFalse(expression.compute(null, null));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            public Object computeValue(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        expression = expression.reduce(new SelfContext());
        assertSame(expression, expression.reduce(expression));
        assertTrue(expression instanceof SelfContext);

        expression = new CoreOperationRelationalExpression(new CoreExpression[0]) {
            @Override
            public Object computeValue(EvalContext context) {
                return new InitialContext();
            }
        };
        expression = expression.reduce(new InitialContext());
        assertSame(expression, expression.reduce(expression));
        assertTrue(expression instanceof InitialContext);

        expression = new CoreOperationRelationalExpression(new IteratorExpression(new HashSet<Object>().iterator())) {
            @Override
            public Object computeValue(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        expression = expression.reduce(new Iterator<Object>(new HashSet<Object>().iterator()));
        assertSame(expression, expression.reduce(expression));
        assertTrue(expression instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new IteratorExpression(new HashSet<Object>().iterator()), new NumericLiteralExpression(2.0f));
        Iterator<Object> it = new HashSet<Object>().iterator();
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(1.5f)));
        assertFalse(expression.containsMatch(it, new NumericLiteralExpression(2.0f)));
        assertFalse(expression.containsMatch(it, new NumericLiteralExpression(3.0f)));

        expression = new CoreOperationLessThan(new IteratorExpression(new HashSet<Object>().iterator()), new NumericLiteralExpression(2.0f));
        it = new HashSet<Object>().iterator();
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(1.0f)));
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(1.5f)));
        assertFalse(expression.containsMatch(it, new NumericLiteralExpression(2.0f)));

        expression = new CoreOperationLessThanOrEqual(new IteratorExpression(new HashSet<Object>().iterator()), new NumericLiteralExpression(2.0f));
        it = new HashSet<Object>().iterator();
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(1.0f)));
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(1.5f)));
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(2.0f)));

        expression = new CoreOperationGreaterThanOrEqual(new IteratorExpression(new HashSet<Object>().iterator()), new NumericLiteralExpression(2.0f));
        it = new HashSet<Object>().iterator();
        assertFalse(expression.containsMatch(it, new NumericLiteralExpression(1.5f)));
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(2.0f)));
        assertTrue(expression.containsMatch(it, new NumericLiteralExpression(3.0f)));

        expression = new CoreOperationGreaterThan(new IteratorExpression(new HashSet<Object>().iterator()), new IteratorExpression(new HashSet<Object>().iterator()));
        it = new HashSet<Object>().iterator();
        it = new HashSet<Object>().iterator();
        assertFalse(expression.containsMatch(it, it));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new IteratorExpression(new HashSet<Object>().iterator()), new IteratorExpression(new HashSet<Object>().iterator()));
        Iterator<Object> lit = new HashSet<Object>().iterator();
        Iterator<Object> rit = new HashSet<Object>().iterator();
        assertTrue(expression.findMatch(lit, rit));
        assertFalse(expression.findMatch(rit, lit));
    }
}