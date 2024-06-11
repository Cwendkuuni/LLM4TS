package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                // TODO: implement computeContextDependent()
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // TODO: implement computeValue()
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                // TODO: implement compute()
                return null;
            }
        };

        boolean result = expression.isContextDependent();

        assertTrue(result);
    }

    @Test
    public void testIsContextDependent_WithKnownContextDependency() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                // TODO: implement computeContextDependent()
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // TODO: implement computeValue()
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                // TODO: implement compute()
                return null;
            }
        };

        expression.isContextDependent(); // Set the contextDependencyKnown flag to true

        boolean result = expression.isContextDependent();

        assertTrue(result);
    }

    @Test
    public void testIsContextDependent_WithNoContextDependency() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                // TODO: implement computeContextDependent()
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // TODO: implement computeValue()
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                // TODO: implement compute()
                return null;
            }
        };

        boolean result = expression.isContextDependent();

        assertFalse(result);
    }

    @Test
    public void testComputeValue() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                // TODO: implement computeContextDependent()
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // TODO: implement computeValue()
                return "computeValueResult";
            }

            @Override
            public Object compute(EvalContext context) {
                // TODO: implement compute()
                return null;
            }
        };

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertEquals("computeValueResult", result);
    }

    @Test
    public void testCompute() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                // TODO: implement computeContextDependent()
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // TODO: implement computeValue()
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                // TODO: implement compute()
                return "computeResult";
            }
        };

        EvalContext context = new EvalContext();

        Object result = expression.compute(context);

        assertEquals("computeResult", result);
    }

    @Test
    public void testIterate() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                // TODO: implement computeContextDependent()
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // TODO: implement computeValue()
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                // TODO: implement compute()
                return Collections.singletonList("iterateResult").iterator();
            }
        };

        EvalContext context = new EvalContext();

        Iterator result = expression.iterate(context);

        assertTrue(result.hasNext());
        assertEquals("iterateResult", result.next());
        assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                // TODO: implement computeContextDependent()
                return true;
            }

            @Override
            public Object computeValue(EvalContext context) {
                // TODO: implement computeValue()
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                // TODO: implement compute()
                return Collections.singletonList("iteratePointersResult").iterator();
            }
        };

        EvalContext context = new EvalContext();

        Iterator result = expression.iteratePointers(context);

        assertTrue(result.hasNext());
        assertEquals("iteratePointersResult", result.next());
        assertFalse(result.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Iterator iterator = Collections.singletonList("value").iterator();
        QName qname = new QName(null, "value");
        Locale locale = Locale.getDefault();

        Expression.PointerIterator pointerIterator = new Expression.PointerIterator(iterator, qname, locale);

        assertTrue(pointerIterator.hasNext());
        assertTrue(pointerIterator.next() instanceof NodePointer);
        assertFalse(pointerIterator.hasNext());
    }

    @Test
    public void testValueIterator() {
        Iterator iterator = Collections.singletonList("value").iterator();

        Expression.ValueIterator valueIterator = new Expression.ValueIterator(iterator);

        assertTrue(valueIterator.hasNext());
        assertEquals("value", valueIterator.next());
        assertFalse(valueIterator.hasNext());
    }
}