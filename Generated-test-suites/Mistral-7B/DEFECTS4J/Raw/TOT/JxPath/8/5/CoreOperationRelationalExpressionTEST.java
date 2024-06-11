package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("a"), new VariableExpression("b"));
        InfoSet is = new InfoSet();
        is.addNode(new SingletonIter(1.1));
        is.addNode(new SingletonIter(2.1));
        JXPathContext context = new JXPathContext(is);

        EvalContext evalContext = new EvalContext(context, new InitialContext(is));
        Object value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);

        is = new InfoSet();
        is.addNode(new SingletonIter(2.1));
        is.addNode(new SingletonIter(1.1));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(null));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(1.1)));
        is.addNode(new SingletonIter(null));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(Double.NaN)));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new VariableExpression("a"), new VariableExpression("b"));
        InfoSet is = new InfoSet();
        is.addNode(new SingletonIter(1.1));
        is.addNode(new SingletonIter(2.1));
        JXPathContext context = new JXPathContext(is);

        EvalContext evalContext = new EvalContext(context, new InitialContext(is));
        Object value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);

        is = new InfoSet();
        is.addNode(new SingletonIter(2.1));
        is.addNode(new SingletonIter(2.1));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);

        is = new InfoSet();
        is.addNode(new SingletonIter(1.1));
        is.addNode(new SingletonIter(2.1));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);

        is = new InfoSet();
        is.addNode(new SingletonIter(null));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(1.1)));
        is.addNode(new SingletonIter(null));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(Double.NaN)));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(1.1)));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new VariableExpression("a"), new VariableExpression("b"));
        InfoSet is = new InfoSet();
        is.addNode(new SingletonIter(1.1));
        is.addNode(new SingletonIter(2.1));
        JXPathContext context = new JXPathContext(is);

        EvalContext evalContext = new EvalContext(context, new InitialContext(is));
        Object value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(2.1));
        is.addNode(new SingletonIter(1.1));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);

        is = new InfoSet();
        is.addNode(new SingletonIter(null));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(1.1)));
        is.addNode(new SingletonIter(null));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(Double.NaN)));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new VariableExpression("a"), new VariableExpression("b"));
        InfoSet is = new InfoSet();
        is.addNode(new SingletonIter(1.1));
        is.addNode(new SingletonIter(2.1));
        JXPathContext context = new JXPathContext(is);

        EvalContext evalContext = new EvalContext(context, new InitialContext(is));
        Object value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(2.1));
        is.addNode(new SingletonIter(2.1));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);

        is = new InfoSet();
        is.addNode(new SingletonIter(1.1));
        is.addNode(new SingletonIter(2.1));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertFalse((Boolean) value);

        is = new InfoSet();
        is.addNode(new SingletonIter(null));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(1.1)));
        is.addNode(new SingletonIter(null));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(Double.NaN)));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new SingletonIter(new Double(1.1)));
        is.addNode(new SingletonIter(new Double(1.1)));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }

    @Test
    public void testIteratorComparison() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("a"), new VariableExpression("b"));

        InfoSet is = new InfoSet();
        is.addNode(new ArrayIter(new Object[] { 1, 2, 3 }));
        is.addNode(new ArrayIter(new Object[] { 1, 2, 3, 4 }));
        JXPathContext context = new JXPathContext(is);

        EvalContext evalContext = new EvalContext(context, new InitialContext(is));
        Object value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);

        is = new InfoSet();
        is.addNode(new ArrayIter(new Object[] { 1, 2, 3 }));
        is.addNode(new ArrayIter(new Object[] { 1, 2, 3, 4 }));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new ArrayIter(new Object[] { 1 }));
        is.addNode(new ArrayIter(new Object[] { 1, 2, 3 }));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new ArrayIter(new Object[] { 1, 1, 1, 1, 1, 1 }));
        is.addNode(new ArrayIter(new Object[] { 2, 1, 1, 1, 1, 1 }));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);

        is = new InfoSet();
        is.addNode(new ArrayIter(Collections.emptyList().iterator()));
        is.addNode(new ArrayIter(new Object[] { 1, 2, 3 }));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);

        is = new InfoSet();
        is.addNode(new ArrayIter(new Object[] { 1, 2, 3 }));
        is.addNode(new ArrayIter(Collections.emptyList().iterator()));
        context = new JXPathContext(is);
        evalContext = new EvalContext(context, new InitialContext(is));
        value = expression.computeValue(evalContext);
        assertFalse(value instanceof Boolean);
    }
}