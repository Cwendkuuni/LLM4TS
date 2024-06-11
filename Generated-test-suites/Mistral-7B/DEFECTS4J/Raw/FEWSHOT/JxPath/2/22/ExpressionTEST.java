package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setup() {
        context = mock(EvalContext.class);
    }

    // Test for abstract methods that need to be implemented by subclasses
    // Tests for isContextDependent() and computeContextDependent() should be implemented in subclasses

    @Test(expected = AssertionError.class)
    public void testAbstractMethods() {
        Expression expression = new Expression() {
            // empty body
        };
        expression.computeValue(context);
    }

    // Test for iterate() method
    public void testIterate() {
        // Test with an empty iterator
        Iterator<Object> emptyIterator = Collections.emptyIterator();
        Expression expression = new AbstractExpression() {
            @Override
            public Iterator<Object> iterate(EvalContext context) {
                return emptyIterator;
            }
        };
        Iterator<Object> iterator = expression.iterate(context);
        assertFalse(iterator.hasNext());

        // Test with an iterator that returns a pointer
        Pointer pointer = new NodePointer("test", new Object());
        Iterator<Object> pointerIterator = ValueUtils.iterate(pointer);
        Expression pointerExpression = new AbstractExpression() {
            @Override
            public Iterator<Object> iterate(EvalContext context) {
                return pointerIterator;
            }
        };
        iterator = pointerExpression.iterate(context);
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertTrue(next instanceof Pointer);
        Pointer nextPointer = (Pointer) next;
        assertEquals(pointer, nextPointer);

        // Test with an iterator that returns a value
        Object value = new Object();
        Iterator<Object> valueIterator = ValueUtils.iterate(value);
        Expression valueExpression = new AbstractExpression() {
            @Override
            public Iterator<Object> iterate(EvalContext context) {
                return valueIterator;
            }
        };
        iterator = valueExpression.iterate(context);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertFalse(next instanceof Pointer);
        assertEquals(value, next);
    }

    // Test for iteratePointers() method
    public void testIteratePointers() {
        // Test with an empty iterator
        Expression expression = new AbstractExpression() {
            @Override
            public Iterator<Object> iteratePointers(EvalContext context) {
                return Collections.emptyList().iterator();
            }
        };
        Iterator<Pointer> iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());

        // Test with an iterator that returns a pointer
        Pointer pointer = new NodePointer("test", new Object());
        Expression pointerExpression = new AbstractExpression() {
            @Override
            public Iterator<Object> iteratePointers(EvalContext context) {
                return ValueUtils.iterate(pointer).<Iterator<Pointer>>map(o -> (Pointer) o);
            }
        };
        iterator = pointerExpression.iteratePointers(context);
        assertTrue(iterator.hasNext());
        Pointer nextPointer = iterator.next();
        assertEquals(pointer, nextPointer);
    }

    // Test for computeValue() method
    public void testComputeValue() {
        // Test with an expression that returns a value
        Object value = new Object();
        Expression expression = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return value;
            }

            @Override
            public Iterator<Object> iterate(EvalContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Object> iteratePointers(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        Object result = expression.computeValue(context);
        assertEquals(value, result);

        // Test with an expression that returns a pointer
        Pointer pointer = new NodePointer("test", new Object());
        Expression pointerExpression = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return pointer;
            }

            @Override
            public Iterator<Object> iterate(EvalContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Object> iteratePointers(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        result = expression.computeValue(context);
        assertTrue(result instanceof Pointer);
        Pointer nextPointer = (Pointer) result;
        assertEquals(pointer, nextPointer);
    }

    // Test for compute() method
    public void testCompute() {
        // Test with an expression that returns a value
        Object value = new Object();
        Expression expression = new AbstractExpression() {
            @Override
            public Object compute(EvalContext context) {
                return value;
            }

            @Override
            public Object computeValue(EvalContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Object> iterate(EvalContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Object> iteratePointers(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        Object result = expression.compute(context);
        assertEquals(value, result);

        // Test with an expression that returns a pointer
        Pointer pointer = new NodePointer("test", new Object());
        Expression pointerExpression = new AbstractExpression() {
            @Override
            public Object compute(EvalContext context) {
                return pointer;
            }

            @Override
            public Object computeValue(EvalContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Object> iterate(EvalContext context) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Iterator<Object> iteratePointers(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        result = expression.compute(context);
        assertTrue(result instanceof Pointer);
        Pointer nextPointer = (Pointer) result;
        assertEquals(pointer, nextPointer);
    }
}

class AbstractExpression extends Expression {
    // empty body
}