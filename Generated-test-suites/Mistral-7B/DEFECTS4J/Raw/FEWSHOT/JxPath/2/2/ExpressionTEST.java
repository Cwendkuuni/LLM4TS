package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

    @Test
    public void testAbstractExpressionIsContextDependent() {
        Expression expression = new AbstractExpression();
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testAbstractExpressionComputeContextDependentAlwaysReturnsTrue() {
        Expression expression = new AbstractExpression();
        assertTrue(expression.computeContextDependent());
    }

    @Test
    public void testAbstractExpressionComputeValueReturnsNull() {
        Expression expression = new AbstractExpression();
        assertNull(expression.computeValue(new EvalContext(null)));
    }

    @Test
    public void testAbstractExpressionIterateReturnsEmptyIterator() {
        Expression expression = new AbstractExpression();
        Iterator iterator = expression.iterate(null);
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testAbstractExpressionIteratePointersReturnsEmptyIterator() {
        Expression expression = new AbstractExpression();
        Iterator<Iterator<Pointer>> iterator = expression.iteratePointers(null);
        assertTrue(iterator.isEmpty());
    }

    @Test
    public void testAbstractExpressionHasNextInIterateReturnsFalse() {
        Expression expression = new AbstractExpression();
        Iterator<Iterator<Pointer>> iterator = expression.iteratePointers(null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAbstractExpressionNextInIterateReturnsNull() {
        Expression expression = new AbstractExpression();
        Iterator<Iterator<Pointer>> iterator = expression.iteratePointers(null);
        assertNull(iterator.next().next());
    }

    private static class AbstractExpression extends Expression {
        // empty implementation
    }
}