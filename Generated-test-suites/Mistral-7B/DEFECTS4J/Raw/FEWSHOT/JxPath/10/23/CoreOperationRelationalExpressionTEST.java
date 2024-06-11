package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(new Integer(10));
        Expression right = new LiteralExpression(new Integer(5));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext(new InfoSetSupport());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(new Integer(10));
        right = new LiteralExpression(new Integer(10));
        expression = new GreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(new Integer(11));
        right = new LiteralExpression(new Integer(10));
        expression = new GreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(new Integer(11));
        right = new LiteralExpression(new Integer(11));
        expression = new GreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(new Double(10.5));
        right = new LiteralExpression(new Double(5.0));
        expression = new GreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(new Double(10.5));
        right = new LiteralExpression(new Double(10.5));
        expression = new GreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        left = new LiteralExpression(new Double(11.5));
        right = new LiteralExpression(new Double(10.5));
        expression = new GreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        left = new LiteralExpression(new Double(11.5));
        right = new LiteralExpression(new Double(11.5));
        expression = new GreaterThanExpression(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithIterators() {
        Iterator leftIter = new IteratorSupport(new Object[] { new Integer(1), new Integer(2), new Integer(3) });
        Iterator rightIter = new IteratorSupport(new Object[] { new Integer(1), new Integer(2), new Integer(4) });
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        EvalContext context = new EvalContext(new InfoSetSupport());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftIter = new IteratorSupport(new Object[] { new Integer(5), new Integer(6), new Integer(7) });
        rightIter = new IteratorSupport(new Object[] { new Integer(1), new Integer(2), new Integer(4) });
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftIter = new IteratorSupport(new Object[] { new Integer(5), new Integer(6), new Integer(7) });
        rightIter = new IteratorSupport(new Object[] { new Integer(5), new Integer(6), new Integer(7) });
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftIter = new IteratorSupport(new Object[] { new Integer(5), new Integer(6), new Integer(7) });
        rightIter = new IteratorSupport(new Object[] { new Integer(5), new Integer(4), new Integer(7) });
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    @Test
    public void testComputeValueWithSingletonIterators() {
        Iterator leftIter = new SingletonIterator(new Integer(1));
        Iterator rightIter = new SingletonIterator(new Integer(2));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        EvalContext context = new EvalContext(new InfoSetSupport());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftIter = new SingletonIterator(new Integer(5));
        rightIter = new SingletonIterator(new Integer(2));
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftIter = new SingletonIterator(new Integer(5));
        rightIter = new SingletonIterator(new Integer(5));
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftIter = new SingletonIterator(new Integer(5));
        rightIter = new SingletonIterator(new Integer(4));
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

    @Test
    public void testComputeValueWithSelfContext() {
        SelfContext leftSelfContext = new SelfContext();
        InitialContext rightInitialContext = new InitialContext();
        CoreOperationRelationalExpression expression = new GreaterThanExpression(leftSelfContext, rightInitialContext);
        EvalContext context = new EvalContext(new InfoSetSupport());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithCollections() {
        Collection leftCollection = new HashSet() {{
            add(new Integer(1));
            add(new Integer(2));
            add(new Integer(3));
        }};
        Collection rightCollection = new HashSet() {{
            add(new Integer(1));
            add(new Integer(2));
            add(new Integer(4));
        }};
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        EvalContext context = new EvalContext(new InfoSetSupport());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftCollection = new HashSet() {{
            add(new Integer(5));
            add(new Integer(6));
            add(new Integer(7));
        }};
        rightCollection = new HashSet() {{
            add(new Integer(1));
            add(new Integer(2));
            add(new Integer(4));
        }};
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        leftCollection = new HashSet() {{
            add(new Integer(5));
            add(new Integer(6));
            add(new Integer(7));
        }};
        rightCollection = new HashSet() {{
            add(new Integer(5));
            add(new Integer(4));
            add(new Integer(7));
        }};
        expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(result);
    }

}

class InfoSetSupport implements EvalContext {
    @Override
    public Object getSingleNodePointer() {
        return null;
    }

    @Override
    public Collection getNodeSet() {
        return new HashSet();
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public Iterator iterate() {
        return null;
    }
}