package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

/**
 * Test cases for the Expression class.
 */
public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        AbstractExpression exp = new DummyExpression();
        assertFalse(exp.isContextDependent());
        exp = new ContextDependentExpression();
        assertTrue(exp.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        AbstractExpression exp = new DummyExpression();
        assertEquals(1.0, exp.computeValue(new EvalContext(null, null)).doubleValue());

        exp = new ConstantExpression(10.0);
        assertEquals(10.0, exp.computeValue(new EvalContext(null, null)).doubleValue());

        exp = new ContextDependentExpression();
        assertNull(exp.computeValue(new EvalContext(null, null)));
    }

    @Test
    public void testIterate() {
        AbstractExpression exp = new DummyExpression();
        Iterator it = exp.iterate(new EvalContext(null, null));
        assertTrue(it.hasNext());
        assertEquals(1.0, it.next().doubleValue());
        assertFalse(it.hasNext());

        exp = new ConstantExpression(new Integer[] {1, 2, 3});
        it = exp.iterate(new EvalContext(null, null));
        assertTrue(it.hasNext());
        assertEquals(1, it.next().intValue());
        assertTrue(it.hasNext());
        assertEquals(2, it.next().intValue());
        assertTrue(it.hasNext());
        assertEquals(3, it.next().intValue());
        assertFalse(it.hasNext());

        exp = new ContextDependentExpression();
        it = exp.iterate(new EvalContext(null, null));
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratePointers() {
        AbstractExpression exp = new DummyExpression();
        Iterator it = exp.iteratePointers(new EvalContext(null, null));
        assertFalse(it.hasNext());

        exp = new ConstantExpression(new Pointer[] {new NodePointer("root", 1), new NodePointer("root", 2)});
        it = exp.iteratePointers(new EvalContext(null, null));
        assertTrue(it.hasNext());
        assertTrue(it.next() instanceof Pointer);
        assertTrue(it.next() instanceof Pointer);
        assertFalse(it.hasNext());

        exp = new ContextDependentExpression();
        it = exp.iteratePointers(new EvalContext(null, null));
        assertFalse(it.hasNext());
    }

    private static class DummyExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return ONE;
        }
    }

    private static class ConstantExpression extends Expression {
        private Object value;

        public ConstantExpression(Object value) {
            this.value = value;
        }

        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return new ValueIterator(Collections.singletonList(value).iterator());
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return new PointerIterator(Collections.singletonList(new NodePointer("dummy", 0)).iterator(),
                    new QName(null, "dummy"),
                    Locale.ENGLISH);
        }
    }

    private static class ContextDependentExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false; // place holder, should be implemented by subclass
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}