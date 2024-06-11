package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.utils.TestUtils;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testExpressionContextDependency() {
        AbstractExpression expr1 = new FooExpression();
        AbstractExpression expr2 = new FooExpression();

        EvalContext context1 = new EvalContext(null, null);
        EvalContext context2 = new EvalContext(null, null);

        assertTrue(expr1.isContextDependent());
        assertTrue(expr2.isContextDependent());

        context1.setCurrentNodePointer(new NodePointer(null, "foo", Locale.ENGLISH));
        context2.setCurrentNodePointer(new NodePointer(null, "bar", Locale.ENGLISH));

        assertFalse(expr1.isContextDependent());
        assertFalse(expr2.isContextDependent());
    }

    @Test
    public void testExpressionValue() {
        AbstractExpression expr = new FooExpression();

        EvalContext context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "foo", Locale.ENGLISH));

        assertEquals("expected value", expr.computeValue(context), expr.compute(context));
    }

    @Test
    public void testExpressionIterate() {
        IterableExpression expr = new IterableExpression();

        EvalContext context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "foo", Locale.ENGLISH));

        Iterator iterator = expr.iterate(context);

        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("expected value", iterator.next(), "value");
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testExpressionIteratePointers() {
        IterableExpression expr = new IterableExpression();

        EvalContext context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "foo", Locale.ENGLISH));

        Iterator<Pointer> pointerIterator = expr.iteratePointers(context);

        assertNotNull(pointerIterator);
        assertTrue(pointerIterator.hasNext());
        Pointer pointer = pointerIterator.next();
        assertEquals("expected value", pointer.getQName(), new QName("expectedQName"));
        assertFalse(pointerIterator.hasNext());
    }

    private static class FooExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            // Implementation of this method should be done in a subclass
            // This test only checks that the method is called once
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return "value";
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }
    }

    private static class IterableExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return Arrays.asList("value1", "value2").contains(context.getCurrentNodePointer().getValue());
        }

        @Override
        public Object computeValue(EvalContext context) {
            return Arrays.asList("value1", "value2").iterator();
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }
    }
}